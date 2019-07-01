package org.communis.javawebintro.dto.filters;

import org.communis.javawebintro.enums.UserRole;

public class UserFilterWrapper extends ObjectFilter
{
    private String mail;
    private UserRole role;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
