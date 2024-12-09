//package com.ni3.thirdpartycall.service;
//
//import com.ni3.thirdpartycall.entity.CurrentWeather;
//import com.ni3.thirdpartycall.entity.WeatherResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//
//@Component
//public class WeatherServiceImpl {
//    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceImpl.class);
//    private static final String apiKey= "84b4276d677ed29bbdbe44f5a5767970";
//
//    private static final String API ="http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    public CurrentWeather getWeather(String city) {
//        String finalAPI = API.replace("API_KEY", apiKey).replace("CITY", city);
//
////        HttpHeaders headers = new HttpHeaders();
////        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
////
////        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
//
//        WeatherResponse weatherResponse = response.getBody();
//        if (weatherResponse == null || weatherResponse.getCurrent() == null) {
//            LOGGER.error("No weather data found");
//            return null;
//        }
//
//        CurrentWeather currentWeather = weatherResponse.getCurrent();
//        LOGGER.info(String.format("Weather data: %s", currentWeather));
//
//
//
//        return currentWeather;
//
//
//    }
//
//}
package com.ni3.thirdpartycall.service;

import com.ni3.thirdpartycall.entity.CurrentWeather;
import com.ni3.thirdpartycall.entity.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class WeatherServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceImpl.class);
    private static final String apiKey = "84b4276d677ed29bbdbe44f5a5767970";
    private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    private final WebClient webClient;

    public WeatherServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<CurrentWeather> getWeather(String city) {
        String finalAPI = API.replace("API_KEY", apiKey).replace("CITY", city);
        // Fetch the response from the weather API
        Mono<WeatherResponse> weatherResponseMono = webClient
                .get()
                .uri(finalAPI)
                .retrieve()
                .bodyToMono(WeatherResponse.class);

        // Transform the response to extract the current weather
        Mono<CurrentWeather> currentWeatherMono = weatherResponseMono
                .flatMap(weatherResponse -> {
                    if (weatherResponse == null || weatherResponse.getCurrent() == null) {
                        LOGGER.error("No weather data found");
                        return Mono.empty();
                    }
                    CurrentWeather currentWeather = weatherResponse.getCurrent();
                    LOGGER.info(String.format("Weather data: %s", currentWeather));
                    return Mono.just(currentWeather);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    LOGGER.error("Error fetching weather data: " + e.getMessage(), e);
                    return Mono.empty();
                })
                .onErrorResume(Exception.class, e -> {
                    LOGGER.error("Unexpected error: " + e.getMessage(), e);
                    return Mono.empty();
                });

        // Return the result
        return currentWeatherMono;
    }

//        try {
//            WeatherResponse weatherResponse = webClient
//                    .get()
//                    .uri(finalAPI)
//                    .retrieve()
//                    .bodyToMono(WeatherResponse.class)
//                    .block(); // Blocking here to simplify; prefer reactive chaining in production.
//
//            if (weatherResponse == null || weatherResponse.getCurrent() == null) {
//                LOGGER.error("No weather data found");
//                return null;
//            }
//
//            CurrentWeather currentWeather = weatherResponse.getCurrent();
//            LOGGER.info(String.format("Weather data: %s", currentWeather));
//
//            return currentWeather;
//
//        } catch (WebClientResponseException e) {
//            LOGGER.error("Error fetching weather data: " + e.getMessage(), e);
//            return null;
//        } catch (Exception e) {
//            LOGGER.error("Unexpected error: " + e.getMessage(), e);
//            return null;
//        }
    }


