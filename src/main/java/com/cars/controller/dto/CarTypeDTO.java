package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CarTypeDTO(@NotBlank String type) {
}
