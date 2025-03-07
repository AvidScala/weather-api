package com.example.forecast.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("weather-config")
@Getter
@Setter
public class WeatherConfig {
    private String geoApiUrl;
    private String weatherApiUrl;
}
