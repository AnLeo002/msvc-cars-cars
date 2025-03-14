package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record FuelTypeDTOResponse(@NotBlank Long id,
        @NotBlank String fuel) {
}
