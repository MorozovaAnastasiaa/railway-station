package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.model.Train;
import com.railway.RailwayStation3.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST контроллер для работы с поездами через JSON.
 * Предоставляет полный набор HTTP-методов: GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS.
 */
@RestController
@RequestMapping(path = "api", produces = "application/json")
public class ApiController {
    private final TrainService trainService;

    public ApiController(TrainService trainService) {
        this.trainService = trainService;
    }

    /**
     * Возвращает список всех поездов в формате JSON.
     *
     * @return ResponseEntity со списком поездов
     */
    @GetMapping
    public ResponseEntity<List<Train>> findAll() {
        return new ResponseEntity<>(trainService.findAll(), HttpStatus.OK);
    }

    /**
     * Добавляет новый поезд из JSON-тела запроса.
     *
     * @param train данные нового поезда
     * @return ResponseEntity с созданным поездом или сообщением об ошибке
     */
    @PostMapping("/add")
    public ResponseEntity<?> createTrain(@RequestBody Train train) {
        try {
            Train savedTrain = trainService.createTrain(train);
            return new ResponseEntity<>(savedTrain, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Удаляет поезд по ID.
     * Возвращает статус 204 No Content при успешном удалении.
     *
     * @param id ID поезда
     * @return ResponseEntity без тела
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Полностью обновляет поезд по ID.
     * Если данные некорректны, возвращает сообщение об ошибке.
     *
     * @param id ID поезда
     * @param train обновлённые данные
     * @return ResponseEntity с обновлённым поездом или сообщением об ошибке
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTrain(@PathVariable Long id, @RequestBody Train train) {
        try {
            train.setId(id);
            Train updatedTrain = trainService.updateTrain(train);
            return new ResponseEntity<>(updatedTrain, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Частично обновляет поезд по ID.
     * Принимает Map с полями, которые нужно изменить.
     *
     * @param id ID поезда
     * @param updates поля для обновления
     * @return ResponseEntity с обновлённым поездом или сообщением об ошибке
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateTrain(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            Train updatedTrain = trainService.partialUpdate(id, updates);
            return new ResponseEntity<>(updatedTrain, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Возвращает только заголовки ответа без тела.
     *
     * @return пустой ответ с метаданными
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<?> headTrains() {
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает информацию о поддерживаемых методах для всего списка поездов.
     *
     * @return заголовок Allow с доступными методами
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok()
                .header("Allow", "GET, POST, HEAD, OPTIONS")
                .build();
    }

    /**
     * Возвращает информацию о поддерживаемых методах для одного поезда.
     *
     * @return заголовок Allow с доступными методами
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> optionsTrain() {
        return ResponseEntity.ok()
                .header("Allow", "GET, PUT, PATCH, DELETE, HEAD, OPTIONS")
                .build();
    }
}