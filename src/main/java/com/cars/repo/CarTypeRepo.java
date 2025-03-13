package com.cars.repo;

import com.cars.persistence.CarTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarTypeRepo extends JpaRepository<CarTypeEntity,Long> {
}
