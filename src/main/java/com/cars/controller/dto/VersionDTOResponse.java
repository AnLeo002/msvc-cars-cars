package com.cars.controller.dto;

import lombok.NonNull;


public record VersionDTOResponse(@NonNull Long id,
                                 @NonNull String version) {
}
