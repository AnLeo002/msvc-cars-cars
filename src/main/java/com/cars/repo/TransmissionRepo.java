package com.cars.repo;

import com.cars.persistence.TransmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmissionRepo extends JpaRepository<TransmissionEntity,Long> {
}
