package com.railway.RailwayStation3.repository;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя (логин), используемое для аутентификации.
     */
    private String username;

    /**
     * Номер телефона пользователя.
     */
    private String phone;

    /**
     * Адрес электронной почты пользователя.
     */
    private String email;

    /**
     * Пароль пользователя в зашифрованном виде.
     */
    private String password;

    /**
     * Роль пользователя. По умолчанию — ROLE_USER.
     * Может быть изменена на ROLE_ADMIN для расширенных прав.
     */
    private String role = "ROLE_USER";

    /**
     * Константа для роли администратора.
     * Используется для проверки доступа к защищённым частям сайта.
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * Константа для роли обычного пользователя.
     */
    public static final String ROLE_USER = "ROLE_USER";

    /**
     * Возвращает список разрешений (ролей), связанных с этим пользователем.
     * Используется Spring Security для управления доступом.
     *
     * @return коллекция объектов GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}