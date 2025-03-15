package com.cars.repo;

import com.cars.persistence.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepo extends JpaRepository<BrandEntity,Long> {
    Optional<BrandEntity> findByBrand(String brand);
    @Query("SELECT b FROM BrandEntity b WHERE LOWER(b.brand) = LOWER(:brand)")
    Optional<BrandEntity> findByBrandIgnoreCase(@Param("brand") String brand);
}
