package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TransmissionDTOResponse(@NotBlank Long id,
                                      @NotBlank String transmission,
                                      @NotBlank int speeds) {
}
