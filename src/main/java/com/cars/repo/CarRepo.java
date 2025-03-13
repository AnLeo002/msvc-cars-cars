package com.cars.repo;

import com.cars.persistence.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepo extends JpaRepository<CarEntity,Long> {
}
