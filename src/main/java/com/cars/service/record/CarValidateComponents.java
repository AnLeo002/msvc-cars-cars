package com.cars.service.record;

import com.cars.persistence.*;

public record CarValidateComponents (FuelTypeEntity fuelType,
                                     TransmissionEntity transmission,
                                     CarTypeEntity carType,
                                     ModelEntity model,
                                     VersionEntity version) {
}
