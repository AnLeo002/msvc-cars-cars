package com.cars.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModelDTO(@NotNull String model,
                       @NotNull String brand,
                       @NotNull List<String> versions) {
}
