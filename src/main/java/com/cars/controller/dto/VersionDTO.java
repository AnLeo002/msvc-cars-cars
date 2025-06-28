package com.cars.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record VersionDTO(@NotBlank String version) {
}
