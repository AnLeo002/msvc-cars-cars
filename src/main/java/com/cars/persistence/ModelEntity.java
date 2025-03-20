package com.cars.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "models")
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    @ManyToOne(targetEntity = BrandEntity.class, fetch = FetchType.LAZY)
    private BrandEntity brand;
    @OneToMany(targetEntity = CarEntity.class,fetch = FetchType.LAZY,mappedBy = "model")
    private List<CarEntity> carList;
    @ManyToMany(targetEntity = VersionEntity.class, fetch = FetchType.LAZY)
    @JoinTable(name = "model_version",joinColumns = @JoinColumn(name = "model"),inverseJoinColumns = @JoinColumn(name = "version"))
    private Set<VersionEntity> versionEntities;


}
