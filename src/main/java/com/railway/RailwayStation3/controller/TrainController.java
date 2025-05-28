package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.service.StatsService;
import com.railway.RailwayStation3.service.TrainService;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для отображения расписания поездов.
 * Поддерживает фильтрацию по городам и дате, а также сортировку.
 */
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

    /**
     * Отображает таблицу с расписанием поездов.
     * Может применять фильтры (город отправления, город прибытия, дата) и сортировку по любому из столбцов.
     *
     * @param fromCity город отправления
     * @param toCity город прибытия
     * @param departureDate дата отправления
     * @param sortBy поле для сортировки (по умолчанию — id)
     * @param model модель данных для представления
     * @return имя шаблона "index"
     */
    @GetMapping
    public String findAll(
            @RequestParam(required = false) String fromCity,
            @RequestParam(required = false) String toCity,
            @RequestParam(required = false) LocalDate departureDate,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            List<Train> trains = trainService.findByFilters(fromCity, toCity, departureDate, sortBy);
            List<String> allFromCities = trainService.getAllUniqueFromCities();
            List<String> allToCities = trainService.getAllUniqueToCities();

            model.addAttribute("trains", trains);
            model.addAttribute("allFromCities", allFromCities);
            model.addAttribute("allToCities", allToCities);
            model.addAttribute("fromCity", fromCity);
            model.addAttribute("toCity", toCity);
            model.addAttribute("departureDate", departureDate);
            model.addAttribute("sortBy", sortBy);

            return "index";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }

    }
}