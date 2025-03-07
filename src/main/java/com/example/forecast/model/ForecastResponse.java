package com.example.forecast.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "Weather forecast response")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponse {

    @Schema(description = "Units of temperature", example = "fahrenheit")
    private String units;

    @Schema(description = "List of daily forecasts")
    private List<DayForecast> forecast;

}
