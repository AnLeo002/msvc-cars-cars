package com.cars.repo;

import com.cars.persistence.FuelTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelTypeRepo extends JpaRepository<FuelTypeEntity,Long> {
}
