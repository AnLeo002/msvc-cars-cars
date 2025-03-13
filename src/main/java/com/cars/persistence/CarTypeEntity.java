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
@Table(name = "types")
public class CarTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "car-type")
    private String typeCar;
    @OneToMany(targetEntity = CarEntity.class,fetch = FetchType.LAZY,mappedBy = "type")
    private List<CarEntity> carList;
}
