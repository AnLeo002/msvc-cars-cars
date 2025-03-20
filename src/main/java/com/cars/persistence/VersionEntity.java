package com.cars.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "versions")
public class VersionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String version;
    @OneToMany(targetEntity = CarEntity.class,fetch = FetchType.LAZY,mappedBy = "version")
    private List<CarEntity> carList;

}
