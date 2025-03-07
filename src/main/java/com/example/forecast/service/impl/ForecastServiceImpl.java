package com.example.forecast.service.impl;

import com.example.forecast.config.WeatherConfig;
import com.example.forecast.exception.BadDataException;
import com.example.forecast.model.GeocodingResponse;
import com.example.forecast.model.WeatherResponse;
import com.example.forecast.service.ForecastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ForecastServiceImpl implements ForecastService {

    private final WeatherConfig weatherConfig;
    private final WebClient webClient;

    public ForecastServiceImpl(WebClient webClient, WeatherConfig weatherConfig) {
        this.webClient = webClient;
        this.weatherConfig = weatherConfig;
    }

    @Cacheable(value = "forecasts", key = "#place + #state + #countryCode + #units", unless = "#result == null")
    @Override
    public Mono<Map<String, Object>> getWeatherForecast(String place, String state, String countryCode, String units) {
        //log.info("Fetching forecast for: {}, {}, {} with units: {}", place, state, countryCode, units);

        // Validate units
        if (!"celsius".equalsIgnoreCase(units) && !"fahrenheit".equalsIgnoreCase(units)) {
            return Mono.error(new BadDataException("Invalid units. Allowed values: 'celsius' or 'fahrenheit'"));
        }

        return getCoordinates(place, state, countryCode)
                .flatMap(coordinates -> {
                    if (coordinates == null) {
                        return Mono.just(Map.of("error", "Location not found"));
                    }
                    return fetchWeatherData(coordinates[0], coordinates[1], units)
                            .map(weatherResponse -> parseWeatherResponse(weatherResponse, units))
                            .onErrorResume(e -> Mono.error(new RuntimeException("Failed to fetch weather data: " + e.getMessage())));
                });
    }

    // Fetch coordinates based on location
    public Mono<double[]> getCoordinates(String place, String state, String countryCode) {
        log.info("Fetching coordinates for: {}, {}, {}", place, state, countryCode);
        String url = String.format("%s?name=%s&state=%s&country=%s&count=1",
                weatherConfig.getGeoApiUrl(), place.replace(" ", "%20"),
                state.replace(" ", "%20"), countryCode);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GeocodingResponse.class)
                .flatMap(response -> {
                    if (response.getResults().isEmpty()) {
                        return Mono.error(new RuntimeException("Location not found: " + place + ", " + state + ", " + countryCode));
                    }
                    double latitude = response.getResults().get(0).getLatitude();
                    double longitude = response.getResults().get(0).getLongitude();
                    return Mono.just(new double[]{latitude, longitude});
                });
    }

    // Fetch weather data from external API
    public Mono<WeatherResponse> fetchWeatherData(double latitude, double longitude, String units) {
        log.info("Fetching weather data for coordinates: {}, {}", latitude, longitude);
        String url = String.format("%s?latitude=%f&longitude=%f&daily=temperature_2m_max,temperature_2m_min&temperature_unit=%s&timezone=auto",
                weatherConfig.getWeatherApiUrl(), latitude, longitude, units);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Weather data unavailable for location.")));
    }

    // Convert WeatherResponse to Map<String, Object> (including units)
    private Map<String, Object> parseWeatherResponse(WeatherResponse weatherResponse, String units) {
        log.info("Parsing weather response");

        List<Map<String, Object>> forecast = IntStream.range(0, weatherResponse.getDaily().getTime().size())
                .mapToObj(i -> {
                    Map<String, Object> dayForecast = new HashMap<>();
                    dayForecast.put("date", weatherResponse.getDaily().getTime().get(i));
                    dayForecast.put("min_temp", weatherResponse.getDaily().getTemperature_2m_min().get(i));
                    dayForecast.put("max_temp", weatherResponse.getDaily().getTemperature_2m_max().get(i));
                    return dayForecast;
                })
                .toList();

        return Map.of(
                "units", units,
                "forecast", forecast
        );
    }
}
