package com.example.forecast.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public  class Daily {

    @JsonProperty("time")
    private List<String> time;
    @JsonProperty("temperature_2m_max")
    private List<Double> temperature_2m_max;
    @JsonProperty("temperature_2m_min")
    private List<Double> temperature_2m_min;

}