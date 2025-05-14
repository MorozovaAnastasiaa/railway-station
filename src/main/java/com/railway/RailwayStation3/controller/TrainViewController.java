package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.repository.User;
import com.railway.RailwayStation3.service.StatsService;
import com.railway.RailwayStation3.service.TrainService;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TrainViewController {
    private final TrainService trainService;
    private final UserService userService;
    private final StatsService statsService;

    public TrainViewController(TrainService trainService, UserService userService, StatsService statsService) {
        this.trainService = trainService;
        this.userService = userService;
        this.statsService = statsService;
    }

    @GetMapping
    public String findAll(
            @RequestParam(required = false) String fromCity,
            @RequestParam(required = false) String toCity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDatetime, @RequestParam(required = false, defaultValue = "id") String sortBy,
            Model model) {

        List<Train> trains = trainService.findByFilters(fromCity, toCity, departureDatetime, sortBy);

        model.addAttribute("trains", trains);
        model.addAttribute("fromCity", fromCity);
        model.addAttribute("toCity", toCity);
        model.addAttribute("departureDatetime", departureDatetime);
        model.addAttribute("sortBy", sortBy);

        return "index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add-form")
    public String showAddForm(Model model) {
        model.addAttribute("train", new Train());
        return "add-train";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-train")
    public String addTrain(@ModelAttribute Train train) {
        trainService.createTrain(train);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete-train/{id}")
    public String deleteTrain(@PathVariable("id") Long id) {
        trainService.deleteTrain(id);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update-form/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Train train = trainService.getTrainById(id);
        model.addAttribute("train", train);
        return "update-train";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-train/{id}")
    public String updateTrain(@ModelAttribute Train train, @PathVariable("id") Long id) {
        train.setId(id);
        trainService.createTrain(train);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminForm(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin-panel";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/update-role")
    public String updateUserRole(@RequestParam Long userId,
                                 @RequestParam String newRole) {
        userService.updateUserRole(userId, newRole);
        return "redirect:/admin";
    }

    @GetMapping("/stats")
    public String statsPage(Model model) {
        model.addAttribute("stats", statsService.getSystemStats());
        return "stats";
    }

    @GetMapping("/about")
    public String showAboutAuthor() {
        return "/about";
    }


}
