package com.cars.repo;

import com.cars.persistence.CarTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarTypeRepo extends JpaRepository<CarTypeEntity,Long> {
    @Query("SELECT c FROM CarTypeEntity c WHERE LOWER(c.type) = LOWER(:type) ")
    Optional<CarTypeEntity> findByTypeIgnoreCase(@Param("type")String type);
}
