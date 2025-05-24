package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/trains", produces = "application/json")  // ← Важно!
public class ApiController {
    private final TrainService trainService;

    public ApiController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public ResponseEntity<List<Train>> findAll() {
        return new ResponseEntity<>(trainService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createTrain(@RequestBody Train train) {
        try {
            Train savedTrain = trainService.createTrain(train);
            return new ResponseEntity<>(savedTrain, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrain(@PathVariable Long id, @RequestBody Train train) {
        try {
            train.setId(id);
            Train updatedTrain = trainService.createTrain(train);
            return new ResponseEntity<>(updatedTrain, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
