package com.cars.repo;

import com.cars.persistence.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends JpaRepository<BrandEntity,Long> {
}
