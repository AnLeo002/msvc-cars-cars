package com.cars.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CarDTO(@NotBlank
                     @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$|^[A-Z]{3}[0-9]{2}[A-Z]$",
                             message = "La placa debe tener el formato BDP018 o DGF87G")
                     String plate,
                     @NotBlank int age,
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
