package com.railway.RailwayStation3;

import com.railway.RailwayStation3.model.User;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Класс для инициализации начальных данных при запуске приложения.
 * Создаёт тестовых пользователей, если они ещё не существуют.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    /**
     * Конструктор для внедрения зависимости.
     *
     * @param userService сервис для работы с пользователями
     */
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    /**
     * Точка входа для инициализации данных.
     * Проверяет, существуют ли пользователи, и создаёт их при необходимости.
     *
     * @param args аргументы командной строки (не используются)
     */
    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists(
                "admin",
                "admin",
                "admin@railway.com",
                "+79991112233",
                "ROLE_ADMIN"
        );

        createUserIfNotExists(
                "user1",
                "user1",
                "user@railway.com",
                "+79997778899",
                "ROLE_USER"
        );
    }

    /**
     * Создаёт пользователя, если он не существует.
     * Используется для инициализации тестовых данных.
     *
     * @param username имя пользователя
     * @param password пароль
     * @param email адрес электронной почты
     * @param phone номер телефона
     * @param role роль пользователя
     */
    private void createUserIfNotExists(String username, String password,
                                       String email, String phone, String role) {
        if (!userService.userExists(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);

            userService.registerUser(user);
            System.out.println("Created " + role + ": " + username);
        }
    }
}