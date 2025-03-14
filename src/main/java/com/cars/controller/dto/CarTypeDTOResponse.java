package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CarTypeDTOResponse(@NotBlank Long id,
        @NotBlank String type) {
}
