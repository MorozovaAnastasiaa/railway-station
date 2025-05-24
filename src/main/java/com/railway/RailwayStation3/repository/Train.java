package com.railway.RailwayStation3.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @Column(name="from_city")
    private String fromCity;

    @Column(name="to_city")
    private String toCity;

    @Column(name="departure_station")
    private String departureStation;

    @Column(name="arrival_station")
    private String arrivalStation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    public Train(Long id, String number, String fromCity, String toCity, String departureStation, String arrivalStation, LocalDate departureDate, LocalTime departureTime, LocalDate arrivalDate, LocalTime arrivalTime) {
        this.id = id;
        this.number = number;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
    }

    public Train() {
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", fromcity='" + fromCity + '\'' +
                ", tocity='" + toCity + '\'' +
                '}';
    }
}
