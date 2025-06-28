package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;


public record VersionDTOResponse(@NotBlank Long id,
                                 @NotBlank String version) {
}
