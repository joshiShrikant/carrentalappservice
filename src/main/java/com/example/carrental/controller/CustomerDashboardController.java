package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.mapper.CarMapper;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/dashboard")
@Tag(name = "Customer Dashboard", description = "APIs for customer dashboard operations")
public class CustomerDashboardController {

    private final CarRepository carRepository;
    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get dashboard data", 
              description = "Retrieves all relevant data for the customer dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard data"),
        @ApiResponse(responseCode = "500", description = "Internal server error while fetching dashboard data")
    })
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        try {
            // Get available cars
            List<CarDto> availableCars = carRepository.findByAvailableTrue()
                .stream()
                .map(CarMapper::toDto)
                .toList();

            // Get dashboard statistics
            Map<String, Object> dashboardStats = dashboardService.getCustomerDashboardStats();
            
            // Combine all data
            Map<String, Object> response = Map.of(
                "availableCars", availableCars,
                "statistics", dashboardStats
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching dashboard data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load dashboard data. Please try again later.");
        }
    }
}
