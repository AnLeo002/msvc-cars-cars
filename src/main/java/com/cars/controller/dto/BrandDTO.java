package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandDTO(@NotBlank String brand) {
}
