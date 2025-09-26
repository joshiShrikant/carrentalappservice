package com.example.carrental.service.impl;

import com.example.carrental.repository.BookingRepository;
import com.example.carrental.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;

    @Override
    public Map<String, Object> getCustomerDashboardStats() {
        try {
            // Example implementation - replace with actual repository calls
            long totalBookings = bookingRepository.count();
            long activeBookings = bookingRepository.countByStatus("ACTIVE");
            double totalSpend = bookingRepository.getTotalSpend();
            
            return Map.of(
                "totalBookings", totalBookings,
                "activeBookings", activeBookings,
                "totalSpend", totalSpend
            );
        } catch (Exception e) {
            log.error("Error fetching dashboard statistics: {}", e.getMessage(), e);
            // Return default values in case of error
            return Map.of(
                "totalBookings", 0,
                "activeBookings", 0,
                "totalSpend", 0.0
            );
        }
    }
}
