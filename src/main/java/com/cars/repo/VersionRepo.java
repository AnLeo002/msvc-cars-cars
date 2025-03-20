package com.cars.repo;

import com.cars.persistence.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionRepo extends JpaRepository<VersionEntity,Long> {
    @Query("SELECT v FROM VersionEntity v WHERE LOWER(REPLACE(v.version, ' ', '')) = LOWER(REPLACE(:version, ' ', ''))")
    Optional<VersionEntity> findByVersionIgnoreCase(String version);

    List<VersionEntity> findByVersionIn(List<String> version);
}
