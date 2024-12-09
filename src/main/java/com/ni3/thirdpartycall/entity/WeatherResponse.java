package com.ni3.thirdpartycall.entity;

public class WeatherResponse {
    private CurrentWeather current;

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "current=" + current +
                '}';
    }
}
