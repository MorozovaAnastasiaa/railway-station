package com.railway.RailwayStation3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByNumber(String number);

    List<Train> findByFromCityAndToCityAndDepartureDatetime(
            String fromCity,
            String toCity,
            LocalDateTime departureDate,
            Sort sort);

}
