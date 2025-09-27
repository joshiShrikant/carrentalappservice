package com.example.carrental.config;

import com.example.carrental.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v2/auth/**",
                                "/v3/api-docs/**",        // ✅ allow OpenAPI JSON + config
                                "/api-docs/**",          // ✅ matches your springdoc.api-docs.path
                                "/swagger-ui/**",         // ✅ allow UI assets
                                "/swagger-ui.html",       // ✅ redirect
                                "/actuator/**"            // ✅ health/metrics
                        ).permitAll() // Allow /auth/** without authentication
                        .requestMatchers("/api/v2/cars/**", "/api/v2/users/**").authenticated()
                        .anyRequest().authenticated() // Secure other endpoints
                )
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable); // Disable default login page

        return http.build();
    }
}
