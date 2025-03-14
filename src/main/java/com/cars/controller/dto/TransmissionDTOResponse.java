package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TransmissionDTOResponse(@NotBlank String transmission,
                                      @NotBlank int speeds) {
}
