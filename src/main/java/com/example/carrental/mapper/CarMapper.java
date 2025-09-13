package com.example.carrental.mapper;

import com.example.carrental.entity.Car;
import com.example.carrental.dto.CarDto;

public class CarMapper {
    public static CarDto toDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setModel(car.getModel());
        dto.setBrand(car.getBrand());
        return dto;
    }

    public static Car toEntity(CarDto dto) {
        Car car = new Car();
        car.setId(dto.getId());
        car.setModel(dto.getModel());
        car.setBrand(dto.getBrand());
        return car;
    }
}

