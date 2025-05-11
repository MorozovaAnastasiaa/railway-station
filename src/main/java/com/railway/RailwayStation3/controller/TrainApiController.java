//package com.railway.RailwayStation3.controller;
//
//import com.railway.RailwayStation3.repository.Train;
//import com.railway.RailwayStation3.service.TrainService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(path="api/trains")
//public class TrainApiController {
//    private final TrainService trainService;
//
//    public TrainApiController(TrainService trainService) {
//        this.trainService = trainService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Train>> findAll() {
//        return new ResponseEntity<>(trainService.findAll(), HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<Train> createTrain(@RequestBody Train train) {
//        return new ResponseEntity<>(trainService.createTrain(train), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
//        trainService.deleteTrain(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
