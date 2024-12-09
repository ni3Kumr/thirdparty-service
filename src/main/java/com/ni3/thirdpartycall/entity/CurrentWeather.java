package com.ni3.thirdpartycall.entity;

public class CurrentWeather {
    private int temperature;
    private int feelslike;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getFeelslike() {
        return feelslike;
    }

    public void setFeelslike(int feelslike) {
        this.feelslike = feelslike;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "temperature=" + temperature +
                ", feelslike=" + feelslike +
                '}';
    }
}
