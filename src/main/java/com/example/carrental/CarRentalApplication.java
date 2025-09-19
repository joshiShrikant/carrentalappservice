package com.example.carrental;

import com.example.carrental.entity.Car;
import com.example.carrental.repository.CarRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CarRentalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }

    @Bean
    public org.springframework.boot.CommandLineRunner dataLoader(CarRepository carRepository) {
        return args -> {
            carRepository.save(new Car(null, "Model S", "Tesla"));
            carRepository.save(new Car(null, "Civic", "Honda"));
            carRepository.save(new Car(null, "Corolla", "Toyota"));
            carRepository.save(new Car(null, "Mustang", "Ford"));
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
