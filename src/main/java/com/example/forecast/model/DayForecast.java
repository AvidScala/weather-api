package com.example.forecast.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayForecast {
        @Schema(description = "Date of forecast", example = "2025-03-06")
        private String date;

        @Schema(description = "Minimum temperature", example = "31.7")
        private double min_temp;

        @Schema(description = "Maximum temperature", example = "53.2")
        private double max_temp;

    }