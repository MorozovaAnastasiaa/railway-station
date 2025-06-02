package com.railway.RailwayStation3.service;

import com.railway.RailwayStation3.model.User;
import com.railway.RailwayStation3.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Содержит логику регистрации, аутентификации и управления ролями.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Загружает пользователя по имени для аутентификации.
     *
     * @param username имя пользователя
     * @return UserDetails объект для Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь '" + username + "' не найден"));
    }

    /**
     * Регистрирует нового пользователя после проверки данных.
     * Проверяет уникальность: имя пользователя, email, телефон.
     * Также шифрует пароль перед сохранением.
     *
     * @param user данные нового пользователя
     */
    public void registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Логин уже занят");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        if (!user.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Некорректный формат email");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }

        if (user.getPhone() == null || !user.getPhone().matches("\\d{10}")) {
            throw new IllegalArgumentException("Номер телефона должен содержать ровно 10 цифр без дополнительных символов");
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("Телефон уже используется");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        if ((user.getPassword().length() > 20) || (user.getPassword().length() < 4)){
            throw new IllegalArgumentException("Пароль должен составлять от 4 до 20 символов");
        }

        if (user.getUsername() != null && user.getUsername().length() > 20) {
            throw new IllegalArgumentException("Логин может составлять максимум 20 символов");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    /**
     * Проверяет, существует ли пользователь с указанным логином.
     *
     * @param username логин для проверки
     * @return true, если пользователь существует
     */
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Возвращает список всех зарегистрированных пользователей.
     *
     * @return список пользователей
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Обновляет роль указанного пользователя.
     * Администратор не может изменить свою собственную роль.
     *
     * @param userId ID пользователя
     * @param newRole новая роль
     * @param authentication текущий авторизованный пользователь
     */
    public void updateUserRole(Long userId, String newRole, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (currentUser.getId().equals(userId)) {
            throw new IllegalArgumentException("Администратор не может изменить свою собственную роль");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }
}