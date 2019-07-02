package org.communis.javawebintro.service;

import org.communis.javawebintro.config.UserDetailsImp;
import org.communis.javawebintro.dto.UserPasswordWrapper;
import org.communis.javawebintro.dto.TagWrapper;
import org.communis.javawebintro.dto.filters.UserFilterWrapper;
import org.communis.javawebintro.entity.Tag;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.repository.PermissionRepository;
import org.communis.javawebintro.repository.UserRepository;
import org.communis.javawebintro.repository.TagRepository;
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

import java.util.*;

@Service
@Transactional(rollbackFor = ServerException.class)

public class TagService {
    private final TagRepository tagRepository;
    private final SessionRegistry sessionRegistry;

    @Autowired
    public TagService(TagRepository tagRepository,
                       @Qualifier("sessionRegistry") SessionRegistry sessionRegistry) {
        this.tagRepository = tagRepository;
        this.sessionRegistry = sessionRegistry;
    }


    public void add(TagWrapper tagWrapper) throws ServerException {
        try {
            Tag tag = new Tag();
            tagWrapper.fromWrapper(tag);
            tag.setDateTimeCreate(new Date());
            tagRepository.save(tag);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.TAG_ADDITING_ERROR), exception);
        }
    }
    public void addList(List<TagWrapper> tagWrappers) throws ServerException {
        try {
            for (TagWrapper tagWrapper : tagWrappers) {
                Tag tag = new Tag();
                tagWrapper.fromWrapper(tag);
                tag.setDateTimeCreate(new Date());
                tagRepository.save(tag);
            }
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.TAG_ADDITING_ERROR), exception);
        }
    }
    public void delete(Long id) throws ServerException {
        try {
            Tag tag = getTagById(id);
            tagRepository.delete(tag);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.TAG_DELETE_ERROR), exception);
        }
    }
    public void edit(TagWrapper tagWrapper) throws ServerException {
        try {
            Tag tag = getTagById(tagWrapper.getId());
            Date date = tag.getDateTimeCreate();
            tagWrapper.fromWrapper(tag);
            if (tag.getDateTimeCreate() == null) {
                tag.setDateTimeCreate(date);
            }
            tagRepository.save(tag);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.TAG_DELETE_ERROR), exception);
        }
    }

    public Tag getTagById(Long id) throws ServerException {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_NOT_FOUND)));
    }
}
