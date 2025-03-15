package com.cars.config;

import com.cars.controller.dto.CarDTOResponse;
import com.cars.persistence.CarEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapeo de CarEntity -> CarDTOResponse
        Converter<CarEntity, CarDTOResponse> carToDtoConverter = context -> {
            CarEntity source = context.getSource();
            return new CarDTOResponse(
                    source.getId(),
                    source.getModel(),
                    source.getAge(),
                    source.getKm(),
                    source.getColor(),
                    source.getDescription(),
                    source.getMotor(),
                    source.getPrice() != null ? source.getPrice() : BigDecimal.ZERO,// Si `price` es null, asignar 0.00
                    source.getFuel() != null ? source.getFuel().getFuel() : "N/A",// Si `fuel` es null, asignar "N/A"
                    source.getBrand() != null ? source.getBrand().getBrand() : "N/A",
                    source.getTransmission() != null ? source.getTransmission().getTransmission() : "N/A",
                    source.getType() != null ? source.getType().getType() : "N/A"
            );
        };

        TypeMap<CarEntity, CarDTOResponse> typeMap = modelMapper.createTypeMap(CarEntity.class, CarDTOResponse.class);
        typeMap.setConverter(carToDtoConverter);

        return modelMapper;
    }
}
