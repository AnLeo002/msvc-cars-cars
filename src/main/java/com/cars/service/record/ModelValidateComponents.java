package com.cars.service.record;

import com.cars.persistence.BrandEntity;
import com.cars.persistence.VersionEntity;

import java.util.Set;

public record ModelValidateComponents(BrandEntity brand, Set<VersionEntity> versions) {}
