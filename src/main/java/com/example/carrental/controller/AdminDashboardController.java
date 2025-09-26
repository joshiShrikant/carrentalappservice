package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.mapper.CarMapper;
import com.example.carrental.repository.CarRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/admin/dashboard")
@Tag(name = "Admin Dashboard", description = "Admin Dashboard")
public class AdminDashboardController {

    private final CarRepository carRepository;

    public AdminDashboardController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping
    @Operation(summary = "get Dashboard Data", description = "get Dashboard Data")
    public Map<String, Object> getDashboardData() {
        List<CarDto> cars = carRepository.findAll().stream().map(CarMapper::toDto).toList();

        // Dummy bookings and users data
        List<Map<String, Object>> bookings = List.of(Map.of("id", 1, "carId", 1, "userId", 101, "status", "completed"), Map.of("id", 2, "carId", 2, "userId", 102, "status", "active"));
        List<Map<String, Object>> users = List.of(Map.of("id", 101, "name", "Admin User", "role", "ADMIN"), Map.of("id", 102, "name", "Customer User", "role", "CUSTOMER"));

        // Dummy bookingHistory and users data userId and carId will come from car and user table
        List<Map<String, Object>> bookingHistory = List.of(Map.of("id", 1, "carId", 10, "status", "completed"), Map.of("id", 2, "carId", 10, "status", "completed"), Map.of("id", 3, "carId", 30, "status", "completed"));
        List<Map<String, Object>> activeBookings = List.of(Map.of("id", 1, "carId", 10, "status", "active"), Map.of("id", 2, "carId", 20, "status", "active"), Map.of("id", 3, "carId", 30, "status", "active"), Map.of("id", 4, "carId", 30, "status", "active"));


        return Map.of("cars", cars, "bookings", bookings, "users", users, "bookingHistory", bookingHistory, "activeBookings", activeBookings);
    }
}

