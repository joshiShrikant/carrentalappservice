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
@RequestMapping("/api/v2/admin/dashboard")
public class AdminDashboardController {

    private final CarRepository carRepository;

    public AdminDashboardController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping
    public Map<String, Object> getDashboardData() {
        List<CarDto> cars = carRepository.findAll().stream().map(CarMapper::toDto).toList();

        // Dummy bookings and users data
        List<Map<String, Object>> bookings = List.of(
                Map.of("id", 1, "carId", 1, "userId", 101, "status", "completed"),
                Map.of("id", 2, "carId", 2, "userId", 102, "status", "active")
        );
        List<Map<String, Object>> users = List.of(
                Map.of("id", 101, "name", "Admin User", "role", "ADMIN"),
                Map.of("id", 102, "name", "Customer User", "role", "CUSTOMER")
        );

        return Map.of(
                "cars", cars,
                "bookings", bookings,
                "users", users
        );
    }
}

