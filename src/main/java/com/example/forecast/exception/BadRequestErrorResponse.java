package com.example.forecast.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response containing the error message")
public class BadRequestErrorResponse {
    @Schema(description = "Error message detailing the issue", example = "Invalid units. Allowed values: 'celsius' or 'fahrenheit'")
    private String error;

}
