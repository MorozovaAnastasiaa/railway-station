package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.repository.User;
import com.railway.RailwayStation3.service.TrainService;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {
    private final TrainService trainService;
    private final UserService userService;

    public AdminController(TrainService trainService, UserService userService) {
        this.trainService = trainService;
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add-form")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("train")) {
            model.addAttribute("train", new Train());
        }
        return "add-train";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-train")
    public String addTrain(@ModelAttribute Train train,
                           RedirectAttributes redirectAttributes) {
        try {
            Train savedTrain = trainService.createTrain(train);
            redirectAttributes.addFlashAttribute("success", "Поезд успешно добавлен");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("train", train); // Сохраняем введенные данные
            return "redirect:/add-form";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete-train/{id}")
    public String deleteTrain(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String fromCity,
            @RequestParam(required = false) String toCity,
            @RequestParam(required = false) LocalDate departureDate,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            RedirectAttributes redirectAttributes) {

        trainService.deleteTrain(id);

        // Сохраняем параметры для редиректа
        redirectAttributes.addAttribute("fromCity", fromCity);
        redirectAttributes.addAttribute("toCity", toCity);
        redirectAttributes.addAttribute("departureDate", departureDate);
        redirectAttributes.addAttribute("sortBy", sortBy);

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
    public String updateTrain(@ModelAttribute Train train,
                              @PathVariable("id") Long id,
                              RedirectAttributes redirectAttributes) {
        try {
            train.setId(id);
            trainService.updateTrain(train); // Метод может выбросить IllegalArgumentException
            redirectAttributes.addFlashAttribute("success", "Поезд успешно обновлён");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("train", train); // Сохраняем введенные данные
            return "redirect:/edit-form/" + id; // Страница редактирования
        }
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
}
