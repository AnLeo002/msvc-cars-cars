package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record FuelTypeDTO(@NotBlank String fuel) {
}
