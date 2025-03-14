package com.cars.repo;

import com.cars.persistence.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepo extends JpaRepository<CarEntity,Long> {
    Optional<CarEntity> findByModel(String model);
}
