package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandDTOResponse(@NotBlank Long id,
        @NotBlank String brand) {
}
