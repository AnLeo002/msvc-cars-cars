package com.cars.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands")
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    @OneToMany(targetEntity = CarEntity.class,fetch = FetchType.LAZY,mappedBy = "brand")
    private List<CarEntity> carList;
}
