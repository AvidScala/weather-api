package com.example.forecast.service;

import com.example.forecast.config.WeatherConfig;
import com.example.forecast.exception.BadDataException;
import com.example.forecast.model.Daily;
import com.example.forecast.model.WeatherResponse;
import com.example.forecast.service.impl.ForecastServiceImpl;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ForeCastServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WeatherConfig weatherConfig;

    @SpyBean

    private ForecastServiceImpl forecastService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Ignore
    void testGetWeatherForecast_ValidRequest() {
        String place = "New York";
        String state = "NY";
        String countryCode = "US";
        String units = "fahrenheit";

        double[] mockCoordinates = {40.7128, -74.0060};
        WeatherResponse mockWeatherResponse = new WeatherResponse();
        Daily mockDaily = new Daily();
        mockDaily.setTime(List.of("2025-03-06", "2025-03-07"));
        mockDaily.setTemperature_2m_max(List.of(31.7, 31.5));
        mockDaily.setTemperature_2m_min(List.of(3.2, 2.2));
        mockWeatherResponse.setDaily(mockDaily);


        ForecastServiceImpl spyService = spy(forecastService);
        doReturn(Mono.just(mockCoordinates)).when(spyService).getCoordinates(place, state, countryCode);
        doReturn(Mono.just(mockWeatherResponse)).when(spyService).fetchWeatherData(40.7128, -74.0060, units);

        Mono<Map<String, Object>> result = spyService.getWeatherForecast(place, state, countryCode, units);

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    List<Map<String, Object>> forecast = (List<Map<String, Object>>) response.get("forecast");
                    return forecast.size() == 2 && response.get("units").equals("fahrenheit");
                })
                .verifyComplete();
    }

    @Ignore
    @Test
    void testGetWeatherForecast_InvalidUnits() {
        String place = "Los Angeles";
        String state = "CA";
        String countryCode = "US";
        String units = "kelvin"; // Invalid units

        Mono<Map<String, Object>> result = forecastService.getWeatherForecast(place, state, countryCode, units);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BadDataException &&
                        throwable.getMessage().equals("Invalid units. Allowed values: 'celsius' or 'fahrenheit'"))
                .verify();
    }

    @Ignore
    @Test
    void testGetCoordinates_LocationNotFound() {
        String place = "UnknownCity";
        String state = "UnknownState";
        String countryCode = "XX";

        ForecastServiceImpl spyService = spy(forecastService);
        doReturn(Mono.error(new RuntimeException("Location not found"))).when(spyService)
                .getCoordinates(place, state, countryCode);

        Mono<double[]> result = spyService.getCoordinates(place, state, countryCode);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Location not found"))
                .verify();
    }
}
