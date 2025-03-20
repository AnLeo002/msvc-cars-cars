package com.cars.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CarDTO(@NotBlank int age,
                     @NotBlank double km,
                     @NotBlank String color,
                     @NotBlank String description,
                     @NotBlank String motor,
                     @NotBlank BigDecimal price,
                     @NotBlank String fuel,
                     @NotBlank String brand,
                     @NotBlank TransmissionDTO transmission,
                     @NotBlank String type,
                     @NotBlank String model,
                     @NotNull String version
){}
