package com.cars.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cars")
public class CarEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String model;
        private String version;
        private int age;
        private double km;
        private String color;
        private String description;
        private String motor;
        private BigDecimal price;
        @ManyToOne(targetEntity = BrandEntity.class,fetch = FetchType.LAZY)
        private BrandEntity brand;
        @ManyToOne(targetEntity = FuelTypeEntity.class,fetch = FetchType.LAZY)
        private FuelTypeEntity fuel;
        @ManyToOne(targetEntity = CarTypeEntity.class,fetch = FetchType.LAZY)
        private CarTypeEntity type;
        @ManyToOne(targetEntity = TransmissionEntity.class,fetch = FetchType.LAZY)
        private TransmissionEntity transmission;

        }
