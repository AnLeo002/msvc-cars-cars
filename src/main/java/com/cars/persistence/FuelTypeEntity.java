package com.cars.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuel-types")
public class FuelTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fuel;
    @OneToMany(targetEntity = CarEntity.class,fetch = FetchType.LAZY,mappedBy = "fuel")
    private List<CarEntity> carList;
}
