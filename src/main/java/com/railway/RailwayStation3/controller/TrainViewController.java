package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TrainViewController {
    private final TrainService trainService;

    public TrainViewController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public String findAll(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            Model model) {

        List<Train> trains = trainService.findAllSorted(sortBy);

        model.addAttribute("trains", trains);
        model.addAttribute("sortBy", sortBy);

        return "index";
    }

    @GetMapping("/add-form")
    public String showAddForm(Model model) {
        model.addAttribute("train", new Train());
        return "add-train";
    }

    @PostMapping("/add-train")
    public String addTrain(@ModelAttribute Train train) {
        trainService.createTrain(train);
        return "redirect:/";
    }

    @PostMapping("/delete-train/{id}")
    public String deleteTrain(@PathVariable("id") Long id) {
        trainService.deleteTrain(id);
        return "redirect:/";
    }

    @GetMapping("/update-form/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Train train = trainService.getTrainById(id);
        model.addAttribute("train", train);
        return "update-train";
    }

    @PostMapping("/update-train/{id}")
    public String updateTrain(@ModelAttribute Train train, @PathVariable("id") Long id) {
        train.setId(id);
        trainService.createTrain(train);
        return "redirect:/";
    }

}
