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

        return Map.of(
                "totalBookings", 103,
                "totalSpends", 12020,
                "activeBookings", 2
        );
    }
}

