package com.railway.RailwayStation3.repository;

import com.railway.RailwayStation3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * Предоставляет методы доступа к данным пользователей через Spring Data JPA.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Проверяет, существует ли пользователь с указанным именем.
     * Используется при регистрации для предотвращения дублирования логинов.
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с указанным email.
     * Используется при регистрации для проверки уникальности email.
     */
    boolean existsByEmail(String email);

    /**
     * Проверяет, существует ли пользователь с указанным телефоном.
     * Используется при регистрации для проверки уникальности номера телефона.
     */
    boolean existsByPhone(String phone);

    /**
     * Находит пользователя по имени.
     * Используется при аутентификации и проверке прав.
     */
    Optional<User> findByUsername(String username);
}