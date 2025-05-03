package com.cars.config;

import com.cars.controller.dto.CarDTOResponse;
import com.cars.controller.dto.TransmissionDTOResponse;
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
            TransmissionDTOResponse transmissionDTOResponse = null;
            if (source.getTransmission()!=null){
                 transmissionDTOResponse = new TransmissionDTOResponse(
                         source.getTransmission().getId(),
                         source.getTransmission().getTransmission(),
                         source.getTransmission().getSpeeds());
            }
            return new CarDTOResponse(
                    source.getPlate().toUpperCase(),
                    source.getAge(),
                    source.getKm(),
                    source.getColor(),
                    source.getDescription(),
                    source.getMotor(),
                    source.getPrice() != null ? source.getPrice() : BigDecimal.ZERO,// Si `price` es null, asignar 0.00
                    source.getFuel() != null ? source.getFuel().getFuel() : "N/A",// Si `fuel` es null, asignar "N/A"
                    source.getBrand() != null ? source.getBrand().getBrand() : "N/A",
                    transmissionDTOResponse,
                    source.getType() != null ? source.getType().getType() : "N/A",
                    source.getModel() != null ? source.getModel().getModel() : "N/A",
                    source.getVersion() != null ? source.getVersion().getVersion() : "N/A"
            );
        };

        TypeMap<CarEntity, CarDTOResponse> typeMap = modelMapper.createTypeMap(CarEntity.class, CarDTOResponse.class);
        typeMap.setConverter(carToDtoConverter);

        return modelMapper;
    }
}
