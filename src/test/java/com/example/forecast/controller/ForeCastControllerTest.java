package com.example.forecast.controller;

import com.example.forecast.service.ForecastService;
import com.example.forecast.service.impl.ForecastServiceImpl;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToController;

@WebFluxTest(ForecastController.class)
class ForeCastControllerTest {

    @MockBean
    private ForecastService forecastService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        // Initialize the WebTestClient for testing the controller
        webTestClient = bindToController(new ForecastController((ForecastServiceImpl) forecastService)).build();
    }


    @Ignore
    void testGetWeatherForecast_Success() {
        // Prepare mock data
        Map<String, Object> mockResponse = Map.of(
                "units", "fahrenheit",
                "forecast", List.of(
                        Map.of("date", "2025-03-06", "min_temp", 31.7, "max_temp", 53.2),
                        Map.of("date", "2025-03-07", "min_temp", 31.5, "max_temp", 52.2)
                )
        );

        // Mock service call
        when(forecastService.getWeatherForecast("TestPlace", "TestState", "US", "fahrenheit"))
                .thenReturn(Mono.just(mockResponse));

        // Call the API and assert the response
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("place", "TestPlace")
                        .queryParam("state", "TestState")
                        .queryParam("countryCode", "US")
                        .queryParam("units", "fahrenheit")
                        .build())
                .exchange()
                .expectStatus().isOk()  // Check for status 200
                .expectBody()
                .jsonPath("$.units").isEqualTo("fahrenheit")  // Assert units
                .jsonPath("$.forecast[0].date").isEqualTo("2025-03-06")  // Assert date
                .jsonPath("$.forecast[0].min_temp").isEqualTo(31.7)  // Assert min_temp
                .jsonPath("$.forecast[0].max_temp").isEqualTo(53.2);  // Assert max_temp
    }


    @Ignore
    void testGetWeatherForecast_InvalidUnits() {
        // Prepare mock data for invalid units
        when(forecastService.getWeatherForecast("TestPlace", "TestState", "US", "invalid"))
                .thenReturn(Mono.error(new RuntimeException("Invalid units")));

        // Call the API and assert the response
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("place", "TestPlace")
                        .queryParam("state", "TestState")
                        .queryParam("countryCode", "US")
                        .queryParam("units", "invalid")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError()  // Check for status 500
                .expectBody()
                .jsonPath("$.error").isEqualTo("Invalid units");  // Check error message
    }
}
