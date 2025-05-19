package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.service.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public String statsPage(Model model) {
        model.addAttribute("stats", statsService.getSystemStats());
        return "stats";
    }
}
