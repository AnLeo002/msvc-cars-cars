package com.cars.persistence;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cars")
public class CarEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String model;
        private int age;
        private double km;
        private String color;
        private String description;
        private String motor;
        private String amount;
        }
