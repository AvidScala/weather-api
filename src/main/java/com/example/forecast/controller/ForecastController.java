package com.example.forecast.controller;

import com.example.forecast.exception.BadRequestErrorResponse;
import com.example.forecast.exception.InternalServerErrorResponse;
import com.example.forecast.model.ForecastResponse;
import com.example.forecast.service.impl.ForecastServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/weatherService")
@Tag(name = "Weather API", description = "Endpoints for fetching weather forecast")
public class ForecastController {


    private final ForecastServiceImpl forecastService;
    public ForecastController(ForecastServiceImpl forecastService) {
        this.forecastService = forecastService;
    }


    @GetMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Weather Forecast", description = "Retrieves the weather forecast for a given location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ForecastResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BadRequestErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerErrorResponse.class)))
    })
    public ResponseEntity<Mono<Map<String, Object>>> getForecast(
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String countryCode,
            @RequestParam String units) {

        return ResponseEntity.ok(forecastService.getWeatherForecast(city, state, countryCode, units));
    }

}
