package com.railway.RailwayStation3.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByNumber(String number);

    List<Train> findByFromCityAndToCityAndDepartureDate(
            String fromCity,
            String toCity,
            LocalDate departureDate,
            Sort sort);

    @Query("SELECT CONCAT(t.fromCity, ' → ', t.toCity) as direction, COUNT(t) as count " +
            "FROM Train t GROUP BY direction ORDER BY count DESC")
    List<Object[]> findPopularDirections();
}
