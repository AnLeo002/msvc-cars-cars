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
@Table(name = "transmissions")
public class TransmissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transmission;
    private int speeds;
    @OneToMany(targetEntity = CarEntity.class,fetch = FetchType.LAZY, mappedBy = "transmission")
    private List<CarEntity> carList;
}
