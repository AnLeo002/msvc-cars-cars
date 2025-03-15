package com.cars.repo;

import com.cars.persistence.FuelTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuelTypeRepo extends JpaRepository<FuelTypeEntity,Long> {
    @Query("SELECT f FROM FuelTypeEntity f WHERE LOWER(f.fuel) = LOWER(:fuel)")
    Optional<FuelTypeEntity> findByFuelIgnoreCase(@Param("fuel") String fuel);
}
