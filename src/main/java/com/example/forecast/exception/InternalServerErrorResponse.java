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
@Schema(description = "Internal Server Error Response")
public class InternalServerErrorResponse {
    @Schema(description = "Error message", example = "An unexpected error occurred")
    private String error;

}
