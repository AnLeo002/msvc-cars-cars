package com.cars.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
        @Column(nullable = false, unique = true, length = 10)
        @Pattern(regexp = "^[A-Z]{3}[0-9]{3}$|^[A-Z]{3}[0-9]{2}[A-Z]$",
                message = "La placa debe tener el formato BDP018 o DGF87G")
        private String plate;
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
        @ManyToOne(targetEntity = ModelEntity.class, fetch = FetchType.LAZY)
        private ModelEntity model;
        @ManyToOne(targetEntity = VersionEntity.class, fetch = FetchType.LAZY)
        private VersionEntity version;


        }
