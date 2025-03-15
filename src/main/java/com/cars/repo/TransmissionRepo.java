package com.cars.repo;

import com.cars.persistence.TransmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransmissionRepo extends JpaRepository<TransmissionEntity,Long> {
    //@Query("SELECT t FROM TransmissionEntity t WHERE LOWER(t.transmission) = LOWER(:trans)")
    Optional<TransmissionEntity> findByTransmissionIgnoreCase(String transmission);
    Optional<TransmissionEntity> findByTransmissionIgnoreCaseAndSpeeds(String transmission, int speeds);

}
