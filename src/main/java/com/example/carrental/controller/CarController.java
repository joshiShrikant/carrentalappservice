package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.entity.Car;
import com.example.carrental.mapper.CarMapper;
import com.example.carrental.repository.CarRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v2/cars")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> ResponseEntity.ok(CarMapper.toDto(car)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<CarDto> getCars() {
        return carRepository.findAll().stream()
                .map(CarMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@Valid @RequestBody CarDto carDto) {
        Car car = CarMapper.toEntity(carDto);
        Car savedCar = carRepository.save(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(CarMapper.toDto(savedCar));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @RequestBody CarDto carDto) {
        return carRepository.findById(id)
                .map(existingCar -> {
                    existingCar.setModel(carDto.getModel());
                    existingCar.setBrand(carDto.getBrand());
                    Car updatedCar = carRepository.save(existingCar);
                    return ResponseEntity.ok(CarMapper.toDto(updatedCar));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @PatchMapping("/{id}")
    public ResponseEntity<CarDto> patchCar(@PathVariable Long id, @RequestBody CarDto carDto) {
        return carRepository.findById(id)
                .map(existingCar -> {
                    if (carDto.getModel() != null) {
                        existingCar.setModel(carDto.getModel());
                    }
                    if (carDto.getBrand() != null) {
                        existingCar.setBrand(carDto.getBrand());
                    }
                    Car updatedCar = carRepository.save(existingCar);
                    return ResponseEntity.ok(CarMapper.toDto(updatedCar));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarDto> deleteCar(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> {
                    carRepository.deleteById(id);
                    return ResponseEntity.ok(CarMapper.toDto(car));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping
    public void deleteAllCars() {
        carRepository.deleteAll();
    }
}
