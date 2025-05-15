package com.cars.service.impl;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.persistence.*;
import com.cars.repo.*;
import com.cars.service.ICarService;
import com.cars.service.record.CarValidateComponents;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CarServiceImpl implements ICarService {
    private final CarRepo repo;
    private final ModelMapper modelMapper;
    private final CarValidationService carValidationService;

    public CarServiceImpl(CarRepo repo, ModelMapper modelMapper, CarValidationService carValidationService) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.carValidationService = carValidationService;
    }

    @Override
    public CarDTOResponse findById(String plate) {
        CarEntity car = repo.findById(plate.toUpperCase())
                .orElseThrow(()-> new RuntimeException("El vehiculo no se encuentra registrado en base de datos"));

        return modelMapper.map(car, CarDTOResponse.class);
    }

    @Override
    public List<CarDTOResponse> findAll() {
        List<CarEntity> carEntities = repo.findAll();
        if(carEntities.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"La base de datos se encuentra vacía");
        }
        return carEntities.stream()
                .map(car -> modelMapper.map(car, CarDTOResponse.class))
                .toList();
    }

    @Override
    public CarDTOResponse createCar(CarDTO carDTO) {
        if (repo.existsById(carDTO.plate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La placa ya está registrada");
        }
        CarValidateComponents components = carValidationService.verifiedCarDTO(carDTO);

        CarEntity car = CarEntity.builder()
                .plate(carDTO.plate().toUpperCase())
                .age(carDTO.age())
                .km(carDTO.km())
                .color(carDTO.color())
                .description(carDTO.description())
                .price(carDTO.price())
                .motor(carDTO.motor())
                .brand(components.model().getBrand())
                .fuel(components.fuelType())
                .transmission(components.transmission())
                .type(components.carType())
                .model(components.model())
                .version(components.version())
                .build();
        try {
            CarEntity savedCar  = repo.save(car);
            return modelMapper.map(savedCar , CarDTOResponse.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el vehiculo", e);
        }
    }

    @Override
    public CarDTOResponse updateCar(CarDTO carDTO) {
        CarEntity car = repo.findById(carDTO.plate()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El vehiculo con placa :"+ carDTO.plate() +" no existe"));

        CarValidateComponents components = carValidationService.verifiedCarDTO(carDTO);

        car.setAge(carDTO.age());
        car.setKm(carDTO.km());
        car.setColor(carDTO.color());
        car.setDescription(carDTO.description());
        car.setMotor(carDTO.motor());
        car.setPrice(carDTO.price());
        car.setBrand(components.model().getBrand());
        car.setFuel(components.fuelType());
        car.setTransmission(components.transmission());
        car.setType(components.carType());
        car.setModel(components.model());
        car.setVersion(components.version());
        try {
            CarEntity savedCar  = repo.save(car);
            return modelMapper.map(savedCar , CarDTOResponse.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el vehiculo", e);
        }
    }

    @Override
    public void deleteCar(String plate) {
        if (!repo.existsById(plate)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vehículo no existe.");
        }
        try {
            repo.deleteById(plate);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el vehiculo porque está en uso.");
        }
    }
}
