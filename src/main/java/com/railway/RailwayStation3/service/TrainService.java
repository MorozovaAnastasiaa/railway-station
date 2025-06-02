package com.railway.RailwayStation3.service;

import com.railway.RailwayStation3.model.Train;
import com.railway.RailwayStation3.repository.TrainRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с поездами.
 */
@Service
public class TrainService {
    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    /**
     * Возвращает список всех поездов без фильтров.
     *
     * @return список поездов
     */
    public List<Train> findAll() {
        return trainRepository.findAll();
    }

    /**
     * Возвращает отсортированный список поездов.
     *
     * @param sortBy поле для сортировки
     * @return отсортированный список поездов
     */
    public List<Train> findAllSorted(String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        return trainRepository.findAll(sort);
    }

    /**
     * Добавляет новый поезд после проверки корректности данных:
     *
     * @param train данные нового поезда
     * @return сохранённый поезд
     */
    public Train createTrain(Train train) {
        if (trainRepository.existsByNumber(train.getNumber())) {
            throw new IllegalArgumentException("Поезд с таким номером уже существует");
        }

        if (!train.getNumber().matches("^[a-zA-Z0-9]{2,10}$")) {
            throw new IllegalArgumentException("Номер поезда должен содержать 2-10 английских букв или цифр");
        }

        // Проверка: город отправления не пустой и содержит от 2 до 30 символов
//        if (train.getFromCity() == null || train.getFromCity().trim().isEmpty()) {
//            throw new IllegalArgumentException("Город отправления не может быть пустым");
//        }
//        if (train.getFromCity().length() < 2 || train.getFromCity().length() > 30) {
//            throw new IllegalArgumentException("Город отправления должен содержать от 2 до 30 символов");
//        }
//
//        // Проверка: город отправления не должен содержать цифры
//        if (!train.getFromCity().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {
//            throw new IllegalArgumentException("Город отправления не должен содержать цифры");
//        }

        if (train.getFromCity() == null
                || train.getFromCity().trim().isEmpty()
                || train.getFromCity().length() < 2
                || train.getFromCity().length() > 30
                || !train.getFromCity().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {

            throw new IllegalArgumentException("Город отправления должен содержать от 2 до 30 символов и не должен содержать цифр");
        }

        if (train.getToCity() == null
                || train.getToCity().trim().isEmpty()
                || train.getToCity().length() < 2
                || train.getToCity().length() > 30
                || !train.getToCity().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {

            throw new IllegalArgumentException("Город прибытия должен содержать от 2 до 30 символов и не должен содержать цифр");
        }

        if (train.getDepartureStation() == null
                || train.getDepartureStation().trim().isEmpty()
                || train.getDepartureStation().length() < 2
                || train.getDepartureStation().length() > 30
                || !train.getDepartureStation().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {

            throw new IllegalArgumentException("Вокзал отправления должен содержать от 2 до 30 символов и не должен содержать цифр");
        }

        if (train.getArrivalStation() == null
                || train.getArrivalStation().trim().isEmpty()
                || train.getArrivalStation().length() < 2
                || train.getArrivalStation().length() > 30
                || !train.getArrivalStation().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {

            throw new IllegalArgumentException("Вокзал прибытия должен содержать от 2 до 30 символов и не должен содержать цифр");
        }

//        // Проверка: город прибытия не пустой и содержит от 2 до 30 символов
//        if (train.getToCity() == null || train.getToCity().trim().isEmpty()) {
//            throw new IllegalArgumentException("Город прибытия не может быть пустым");
//        }
//        if (train.getToCity().length() < 2 || train.getToCity().length() > 30) {
//            throw new IllegalArgumentException("Город прибытия должен содержать от 2 до 30 символов");
//        }
//
//        // Проверка: город прибытия не должен содержать цифры
//        if (!train.getToCity().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {
//            throw new IllegalArgumentException("Город прибытия не должен содержать цифры");
//        }


        if (train.getFromCity().equalsIgnoreCase(train.getToCity())) {
            throw new IllegalArgumentException("Города отправления и прибытия не могут совпадать");
        }

        if (train.getDepartureDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата отправления не может быть в прошлом");
        }

        if (train.getArrivalDate().isBefore(train.getDepartureDate())) {
            throw new IllegalArgumentException("Дата прибытия не может быть раньше даты отправления");
        }

        if (train.getDepartureDate().equals(train.getArrivalDate()) &&
                train.getArrivalTime().isBefore(train.getDepartureTime())) {
            throw new IllegalArgumentException("Время прибытия не может быть раньше времени отправления");
        }

        return trainRepository.save(train);
    }

    /**
     * Удаляет поезд по ID.
     *
     * @param id ID поезда
     */
    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    /**
     * Обновляет существующий поезд с проверкой данных.
     *
     * @param train обновлённые данные поезда
     * @return обновлённый поезд
     */
    public Train updateTrain(Train train) {
        Train existingTrain = trainRepository.findById(train.getId())
                .orElseThrow(() -> new IllegalArgumentException("Поезд не найден"));

        if (!existingTrain.getNumber().equals(train.getNumber()) && trainRepository.existsByNumber(train.getNumber())) {
            throw new IllegalArgumentException("Поезд с таким номером уже существует");
        }

        if (!train.getNumber().matches("^[a-zA-Z0-9]{2,10}$")) {
            throw new IllegalArgumentException("Номер поезда должен содержать 2-10 букв или цифр");
        }

        if (train.getFromCity().length() < 2 || train.getFromCity().length() > 30) {
            throw new IllegalArgumentException("Город отправления должен содержать от 2 до 30 символов");
        }

        if (!train.getFromCity().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {
            throw new IllegalArgumentException("Город отправления не должен содержать цифры");
        }

        if (train.getToCity().length() < 2 || train.getToCity().length() > 30) {
            throw new IllegalArgumentException("Город прибытия должен содержать от 2 до 30 символов");
        }

        if (!train.getToCity().matches("[а-яА-ЯёЁa-zA-Z\\s\\-]+")) {
            throw new IllegalArgumentException("Город прибытия не должен содержать цифры");
        }

        if (train.getFromCity().equalsIgnoreCase(train.getToCity())) {
            throw new IllegalArgumentException("Города отправления и прибытия не могут совпадать");
        }

        if (train.getDepartureDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата отправления не может быть в прошлом");
        }

        if (train.getArrivalDate().isBefore(train.getDepartureDate())) {
            throw new IllegalArgumentException("Дата прибытия не может быть раньше даты отправления");
        }

        if (train.getDepartureDate().equals(train.getArrivalDate()) &&
                train.getArrivalTime().isBefore(train.getDepartureTime())) {
            throw new IllegalArgumentException("Время прибытия не может быть раньше времени отправления");
        }

        return trainRepository.save(train);
    }

    /**
     * Находит поезд по ID.
     *
     * @param id ID поезда
     * @return объект Train или null, если не найден
     */
    public Train getTrainById(Long id) {
        return trainRepository.findById(id).orElse(null);
    }

    /**
     * Фильтрует поезда. Может применять сортировку.
     *
     * @param fromCity город отправления
     * @param toCity город прибытия
     * @param departureDate дата отправления
     * @param sortBy поле для сортировки
     * @return список подходящих поездов
     */
    public List<Train> findByFilters(String fromCity, String toCity, LocalDate departureDate, String sortBy) {
        Sort sort = Sort.by(sortBy);

        boolean allEmpty = (isBlank(fromCity) && isBlank(toCity) && departureDate == null);
        if (allEmpty) {
            return trainRepository.findAll(sort);
        }

        boolean anyNotEmpty = (!isBlank(fromCity) || !isBlank(toCity) || departureDate != null);
        boolean allPresent  = (!isBlank(fromCity) && !isBlank(toCity) && departureDate != null);
        if (anyNotEmpty && !allPresent) {
            throw new IllegalArgumentException("Для фильтрации необходимо заполнить все поля");
        }

        return trainRepository.findByFromCityAndToCityAndDepartureDate(fromCity.trim(), toCity.trim(), departureDate, sort);
    }

    private boolean isBlank(String s) {
        return (s == null || s.trim().isEmpty());
    }

//    public List<Train> findByFilters(String fromCity, String toCity, LocalDate departureDate, Sort sort) {
//        if (fromCity != null && toCity != null && departureDate != null) {
//            return trainRepository.findByFromCityAndToCityAndDepartureDate(fromCity, toCity, departureDate, sort);
//        }
//
//        return trainRepository.findAll(sort);
//    }

    /**
     * Возвращает список уникальных городов отправления.
     *
     * @return список городов
     */
    public List<String> getAllUniqueFromCities() {
        return trainRepository.findDistinctFromCities();
    }

    /**
     * Возвращает список уникальных городов прибытия.
     *
     * @return список городов
     */
    public List<String> getAllUniqueToCities() {
        return trainRepository.findDistinctToCities();
    }

    /**
     * Частично обновляет поезд по указанному ID.
     * Поддерживает обновление конкретных полей через Map.
     *
     * @param id ID поезда
     * @param updates карта полей для обновления
     * @return обновлённый поезд
     */
    public Train partialUpdate(Long id, Map<String, Object> updates) {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Поезд не найден"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "number":
                    train.setNumber((String) value);
                    break;
                case "fromCity":
                    train.setFromCity((String) value);
                    break;
                case "toCity":
                    train.setToCity((String) value);
                    break;
                case "departureStation":
                    train.setDepartureStation((String) value);
                    break;
                case "arrivalStation":
                    train.setArrivalStation((String) value);
                    break;
                case "departureDate":
                    train.setDepartureDate(LocalDate.parse((String) value));
                    break;
                case "arrivalDate":
                    train.setArrivalDate(LocalDate.parse((String) value));
                    break;
                case "departureTime":
                    train.setDepartureTime(LocalTime.parse((String) value));
                    break;
                case "arrivalTime":
                    train.setArrivalTime(LocalTime.parse((String) value));
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестное поле: " + key);
            }
        });

        return trainRepository.save(train);
    }
}