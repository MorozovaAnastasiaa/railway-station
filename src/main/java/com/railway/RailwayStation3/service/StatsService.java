package com.railway.RailwayStation3.service;

import com.railway.RailwayStation3.repository.TrainRepository;
import com.railway.RailwayStation3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для сбора системной статистики.
 * Используется на странице /stats.
 */
@Service
public class StatsService {
    private final UserRepository userRepository;
    private final TrainRepository trainRepository;

    public StatsService(UserRepository userRepository, TrainRepository trainRepository) {
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
    }

    /**
     * Собирает данные о пользователях и поездах для отображения на странице статистики.
     *
     * @return Map со статистикой: общее число пользователей, популярные направления
     */
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();

        // Общее количество зарегистрированных пользователей
        stats.put("totalUsers", userRepository.count());

        // Получаем список популярных направлений из репозитория
        List<Object[]> popularDirections = trainRepository.findPopularDirections();

        // Ограничиваем вывод — только топ-5 направлений
        stats.put("popularDirections", popularDirections.stream()
                .limit(5)
                .collect(Collectors.toList()));

        return stats;
    }
}