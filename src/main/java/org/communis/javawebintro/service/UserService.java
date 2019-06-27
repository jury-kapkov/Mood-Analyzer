package org.communis.javawebintro.service;

import org.communis.javawebintro.config.UserDetailsImp;
import org.communis.javawebintro.dto.UserPasswordWrapper;
import org.communis.javawebintro.dto.UserWrapper;
import org.communis.javawebintro.dto.filters.UserFilterWrapper;
import org.communis.javawebintro.entity.LdapUserAttributes;
import org.communis.javawebintro.entity.User;
import org.communis.javawebintro.enums.UserStatus;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.repository.PermissionRepository;
import org.communis.javawebintro.repository.UserRepository;
import org.communis.javawebintro.repository.specifications.UserSpecification;
import org.communis.javawebintro.utils.CredentialsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import java.util.*;


@Service
@Transactional(rollbackFor = ServerException.class)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRegistry sessionRegistry;

    @Autowired
    public UserService(UserRepository userRepository, PermissionRepository permissionRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       @Qualifier("sessionRegistry") SessionRegistry sessionRegistry) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Получает из базы пользователя по логину, его разрешения и формирует объект класса {@link UserDetailsImp}
     *
     * @param login логин пользователя
     * @return объект класса {@link UserDetailsImp}
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findFirstByLogin(login)
                .map(UserWrapper::new)
                .map(user -> new UserDetailsImp(user, permissionRepository.findActionsByRole(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCodeConstants.messages.get(ErrorCodeConstants.DATA_NOT_FOUND)));
    }

    /**
     * Обновляет инфомарцию о дате последнего входа в систему пользователя в базе
     *
     * @param id id пользователя
     */
    public void updateLastTimeOnline(Long id) {
        User user = userRepository.findOne(id);
        user.setDateLastOnline(new Date());
        userRepository.save(user);
    }

    /**
     * Получает пользователя из текущей сессии
     *
     * @return сущность пользователя
     */
    @Transactional(noRollbackFor = ServerException.class)
    public User getCurrentUser() throws ServerException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
            UserWrapper user = userDetails.getUser();
            return getUser(user.getId());
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_INFO_ERROR), ex);
        }
    }

    /**
     * Добавляет информацию о новом пользователе в базу из {@link UserWrapper}
     *
     * @param userWrapper инфомарция о новом пользователе
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void add(UserWrapper userWrapper) throws ServerException {
        try {
            User user = new User();
            userWrapper.fromWrapper(user);
            if (userRepository.findFirstByLogin(user.getLogin()).isPresent()) {
                throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LOGIN_ALREADY_EXIST));
            }

            user.setDateCreate(new Date());
            user.setStatus(UserStatus.ACTIVE);

            userRepository.save(user);
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_ADD_ERROR), ex);
        }
    }

    /**
     * Обновляет информацию о пользователе в базе из {@link UserWrapper}
     *
     * @param userWrapper инфомарция о пользователе
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void edit(UserWrapper userWrapper) throws ServerException {
        try {
            User user = getUser(userWrapper.getId());
            userWrapper.fromWrapper(user);
            userRepository.save(user);
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UPDATE_ERROR), ex);
        }
    }

    /**
     * Устанавливает пользователю с заданным идентификатором статус "BLOCK(Заблокирован)", если это не текущий пользователь системы
     *
     * @param id идентификатор пользователя
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void block(Long id) throws ServerException {
        try {
            User user = getUser(id);

            if (user == getCurrentUser()) {
                throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_BLOCK_SELF_ERROR));
            }

            user.setDateBlock(new Date());
            user.setStatus(UserStatus.BLOCK);
            userRepository.save(user);
            Optional<Object> userPrincipal = sessionRegistry.getAllPrincipals().stream()
                    .filter(p -> Objects.equals(((UserDetailsImp) p).getUser().getId(), user.getId()))
                    .findFirst();
            userPrincipal.ifPresent(o -> sessionRegistry.getAllSessions(o, false)
                    .forEach(SessionInformation::expireNow));
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_BLOCK_ERROR), ex);
        }
    }

    /**
     * Устанавливает пользователю с заданным идентификатором статус "ACTIVE(Активен)"
     *
     * @param id идентификатор пользователя
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void unblock(Long id) throws ServerException {
        try {
            User user = getUser(id);
            user.setDateBlock(null);
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UNBLOCK_ERROR), ex);
        }
    }

    /**
     * Изменяет пароль пользователя в соответсвии с информацией из {@link UserWrapper}
     *
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void changePassword(UserPasswordWrapper passwordWrapper)
            throws ServerException {
        try {
            User user = getUser(passwordWrapper.getId());
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_PASSWORD_ERROR), ex);
        }
    }

    /**
     * Изменяет пароль пользователя в соответсвии с информацией из {@link UserWrapper}.
     * Пароль не меняется если задан пароль короче 8 символом или пароль не совпадает с паролем подтвеждения
     *
     * @param passwordWrapper информация о пароле пользователя
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void changePassword(User user, UserPasswordWrapper passwordWrapper) throws ServerException {
        if (validatePassword(passwordWrapper)) {
            user.setPassword(bCryptPasswordEncoder.encode(passwordWrapper.getPassword()));
        }
    }

    public boolean validatePassword(UserPasswordWrapper passwordWrapper) throws ServerException {
        if (passwordWrapper.getPassword().length() < CredentialsUtil.PASSWORD_MIN_LENGTH) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_PASSWORD_LENGTH_ERROR));
        }
        if (!Objects.equals(passwordWrapper.getPassword(), passwordWrapper.getConfirmPassword())) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_PASSWORD_COMPARE_ERROR));
        }
        return true;
    }

    /**
     * Получает информацию о пользователе с заданным идентификатором из базы и пребразует ее в объект класса {@link UserWrapper}
     *
     * @param id идентификатор пользователя
     * @return объект, содержащий информацию о пользователе
     */
    public UserWrapper getById(Long id) throws ServerException {
        try {
            return new UserWrapper(getUser(id));
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_INFO_ERROR), ex);
        }
    }

    /**
     * Получает из базы страницу объектов {@link UserWrapper} в зависимости от информации о пагинаторе и параметрах фильтра
     *
     * @param pageable          информация о пагинаторе
     * @param filterUserWrapper информация о фильтре
     * @return страница объектов
     */
    public Page getPageByFilter(Pageable pageable, UserFilterWrapper filterUserWrapper) throws ServerException {
        try {
            return userRepository.findAll(UserSpecification.build(filterUserWrapper), pageable)
                    .map(UserWrapper::new);
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LIST_ERROR), ex);
        }
    }

    private User getUser(Long id) throws ServerException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_NOT_FOUND)));
    }

    private List<Attribute> createLdapUser(User user, LdapUserAttributes userAttributes) {
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new BasicAttribute(userAttributes.getLogin(), user.getLogin()));
        attributes.add(new BasicAttribute(userAttributes.getName(), user.getName()));
        attributes.add(new BasicAttribute(userAttributes.getSurname(), user.getSurname()));
        userAttributes.getSecondName().ifPresent(sn -> {
            if (!sn.isEmpty())
                attributes.add(new BasicAttribute(sn, user.getSecondName()));
        });
        attributes.add(new BasicAttribute(userAttributes.getMail(), user.getMail()));

        return attributes;
    }


}
