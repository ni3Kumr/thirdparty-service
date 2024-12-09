package com.ni3.thirdpartycall.controller;

import com.ni3.thirdpartycall.entity.CurrentWeather;
import com.ni3.thirdpartycall.service.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {



    @Autowired
    private WeatherServiceImpl weatherService;

    @GetMapping("/{city}")
    public Mono<ResponseEntity<String>> getWeather(@PathVariable("city") String city) {
        return weatherService.getWeather(city)
                .map(currentWeather ->
                        ResponseEntity.ok("Hi, Today " + city + " weather feels like: " + currentWeather.getFeelslike()))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
