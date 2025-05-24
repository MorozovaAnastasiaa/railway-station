package com.railway.RailwayStation3.service;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.repository.TrainRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainService {
    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public List<Train> findAll() {
        return trainRepository.findAll();
    }

    public List<Train> findAllSorted(String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        return trainRepository.findAll(sort);
    }

    public Train createTrain(Train train) {
        // Проверка номера поезда
        if (trainRepository.existsByNumber(train.getNumber())) {
            throw new IllegalArgumentException("Поезд с таким номером уже существует");
        }

        // Проверка даты отправления
        if (train.getDepartureDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата отправления не может быть в прошлом");
        }

        // Проверка даты прибытия
        if (train.getArrivalDate().isBefore(train.getDepartureDate())) {
            throw new IllegalArgumentException("Дата прибытия не может быть раньше даты отправления");
        }

        return trainRepository.save(train);
    }

    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    public Train updateTrain(Train train) {
        // Проверка даты отправления
        if (train.getDepartureDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата отправления не может быть в прошлом");
        }

        // Проверка даты прибытия
        if (train.getArrivalDate().isBefore(train.getDepartureDate())) {
            throw new IllegalArgumentException("Дата прибытия не может быть раньше даты отправления");
        }

        return trainRepository.save(train);
    }

    public Train getTrainById(Long id) {
        return trainRepository.findById(id).orElse(null);

    }


    public List<Train> findByFilters(
            String fromCity,
            String toCity,
            LocalDate departureDate,
            String sortBy) {

        Sort sort = Sort.by(sortBy);

        if (fromCity != null && toCity != null && departureDate != null) {
            return trainRepository.findByFromCityAndToCityAndDepartureDate(
                    fromCity, toCity, departureDate, sort);
        }

        return trainRepository.findAll(sort);

    }

    public List<String> getAllUniqueFromCities() {
        return trainRepository.findDistinctFromCities();
    }


    public List<String> getAllUniqueToCities() {
        return trainRepository.findDistinctToCities();
    }
}
