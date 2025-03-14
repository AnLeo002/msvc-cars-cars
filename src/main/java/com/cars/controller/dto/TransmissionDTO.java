package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TransmissionDTO(@NotBlank String transmission,
                              @NotBlank int speeds) {
}
