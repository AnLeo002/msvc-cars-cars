package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record  CarDTOResponse(@NotBlank String plate,
                             @NotBlank int age,
                             @NotBlank double km,
                             @NotBlank String color,
                             @NotBlank String description,
                             @NotBlank String motor,
                             @NotBlank BigDecimal price,
                             @NotBlank String fuel,
                             @NotBlank String brand,
                             @NotBlank TransmissionDTOResponse transmission,
                             @NotBlank String type,

                             @NotBlank String model,
                             @NotEmpty String version
                             ) {
}
