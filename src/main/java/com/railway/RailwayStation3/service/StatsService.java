package com.railway.RailwayStation3.service;

import com.railway.RailwayStation3.repository.TrainRepository;
import com.railway.RailwayStation3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {
    private final UserRepository userRepository;
    private final TrainRepository trainRepository;

    public StatsService(UserRepository userRepository, TrainRepository trainRepository) {
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
    }

    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());

        List<Object[]> popularDirections = trainRepository.findPopularDirections();
        stats.put("popularDirections", popularDirections.stream()
                .limit(5)
                .collect(Collectors.toList()));

        return stats;
    }
}