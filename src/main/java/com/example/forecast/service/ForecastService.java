package com.example.forecast.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.Map;


public interface ForecastService {

    Mono<Map<String, Object>> getWeatherForecast(String place, String state, String countryCode, String units);
}
