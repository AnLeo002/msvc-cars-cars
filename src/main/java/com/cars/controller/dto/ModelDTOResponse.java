package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ModelDTOResponse(@NotBlank Long id,
                               @NotBlank String model,
                               @NotBlank String brand,
                               @NotBlank List<String> versions) {
}
