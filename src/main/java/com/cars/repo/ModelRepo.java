package com.cars.repo;

import com.cars.persistence.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ModelRepo extends JpaRepository<ModelEntity, Long> {
    Optional<ModelEntity> findByModelIgnoreCase(String model);
}
