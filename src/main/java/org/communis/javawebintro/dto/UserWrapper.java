package org.communis.javawebintro.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.communis.javawebintro.entity.User;
import org.communis.javawebintro.enums.UserRole;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class UserWrapper implements ObjectWrapper<User>, Serializable
{
//    private final String EMAIL_REGEXP = "(.+@.+)";

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 100)
    private String surname;

    @NotNull
    @Size(max = 20)
    private String login;

    @NotNull
    @Size(max = 256)
    private String mail;

    @JsonIgnore
    @Size(min = 8, max = 20)
    private String password;

    @JsonIgnore
    @Size(min = 8, max = 20)
    private String confirmPassword;

    private Date date_create;

    private UserRole role;

    public UserWrapper() {

    }

    public UserWrapper(User user)
    {
        toWrapper(user);
    }

    @Override
    public void toWrapper(User item)
    {
        if(item!=null)
        {
            id = item.getId();
            name = item.getName();
            surname = item.getSurname();
            login = item.getLogin();
            mail=item.getMail();
            password = item.getPassword();
            date_create = item.getDateCreate();
            role=item.getRole();
        }
    }

    @Override
    public void fromWrapper(User item) {
        if(item!=null) {
            item.setLogin(login);
            item.setRole(role);
            item.setMail(mail);
            item.setName(name);
            item.setSurname(surname);
            item.setPassword(password);
        }
    }

    public String getFio() {
        return surname + " " + name;
    }

    @AssertTrue
    public boolean isPasswordValid() {
        return (password == null && confirmPassword == null) ||
                (password != null && confirmPassword != null && password.equals(confirmPassword));
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("yyyy.MM.dd 'в' HH:mm:ss ").format(date_create);
    }
}