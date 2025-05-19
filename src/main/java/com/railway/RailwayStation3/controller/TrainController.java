package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.service.StatsService;
import com.railway.RailwayStation3.service.TrainService;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TrainController {
    private final TrainService trainService;
    private final UserService userService;
    private final StatsService statsService;

    public TrainController(TrainService trainService, UserService userService, StatsService statsService) {
        this.trainService = trainService;
        this.userService = userService;
        this.statsService = statsService;
    }

    @GetMapping
    public String findAll(
            @RequestParam(required = false) String fromCity,
            @RequestParam(required = false) String toCity,
            @RequestParam(required = false) LocalDate departureDate,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            Model model) {

        List<Train> trains = trainService.findByFilters(fromCity, toCity, departureDate, sortBy);

        model.addAttribute("trains", trains);
        model.addAttribute("fromCity", fromCity);
        model.addAttribute("toCity", toCity);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("sortBy", sortBy);

        return "index";
    }
}
