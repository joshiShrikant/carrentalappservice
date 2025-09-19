package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.mapper.CarMapper;
import com.example.carrental.repository.CarRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/dashboard")
public class CustomerDashboardController {

    private final CarRepository carRepository;

    public CustomerDashboardController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping
    public Map<String, Object> getDashboardData() {
        List<CarDto> cars = carRepository.findAll().stream().map(CarMapper::toDto).toList();

        // Dummy bookingHistory and users data userId and carId will come from car and user table
        List<Map<String, Object>> bookingHistory = List.of(
                Map.of("id", 1, "carId", 10,  "status", "completed"),
                Map.of("id", 2, "carId", 10,  "status", "completed"),
                Map.of("id", 3, "carId", 30, "status", "completed")
        );
        List<Map<String, Object>> activeBookings = List.of(
                Map.of("id", 1, "carId", 10,  "status", "active"),
                Map.of("id", 2, "carId", 20,  "status", "active"),
                Map.of("id", 3, "carId", 30,  "status", "active"),
                Map.of("id", 4, "carId", 30,  "status", "active")
        );

        return Map.of(
                "bookingHistory", bookingHistory,
                "activeBookings", activeBookings
        );
    }
}

