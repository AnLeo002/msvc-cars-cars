package com.cars.service.impl;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.persistence.*;
import com.cars.repo.*;
import com.cars.service.ICarService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements ICarService {
    private final CarRepo repo;
    private final BrandRepo brandRepo;
    private final CarTypeRepo carTypeRepo;
    private final FuelTypeRepo fuelTypeRepo;
    private final TransmissionRepo transmissionRepo;
    private final ModelMapper modelMapper;

    public CarServiceImpl(CarRepo repo, BrandRepo brandRepo, CarTypeRepo carTypeRepo, FuelTypeRepo fuelTypeRepo, TransmissionRepo transmissionRepo, ModelMapper modelMapper) {
        this.repo = repo;
        this.brandRepo = brandRepo;
        this.carTypeRepo = carTypeRepo;
        this.fuelTypeRepo = fuelTypeRepo;
        this.transmissionRepo = transmissionRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CarDTOResponse findById(Long id) {
        CarEntity car = repo.findById(id)
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
                .collect(Collectors.toList());
    }

    @Override
    public CarDTOResponse createCar(CarDTO carDTO) {
        BrandEntity brand = brandRepo.findByBrandIgnoreCase(carDTO.brand()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La marca:"+carDTO.brand() +" no existe"));
        FuelTypeEntity fuelType = fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El combustible:"+carDTO.fuel() +" no existe"));
        TransmissionEntity transmission = transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(carDTO.transmission().transmission(),carDTO.transmission().speeds()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La transmission:"+carDTO.transmission() +" no existe"));
        CarTypeEntity carType = carTypeRepo.findByTypeIgnoreCase(carDTO.type()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El tipo de vehiculo:"+carDTO.type() +" no existe"));
        CarEntity car = CarEntity.builder()
                .model(carDTO.model())
                .age(carDTO.age())
                .km(carDTO.km())
                .color(carDTO.color())
                .description(carDTO.description())
                .price(carDTO.price())
                .motor(carDTO.motor())
                .brand(brand)
                .fuel(fuelType)
                .transmission(transmission)
                .type(carType)
                .build();
        CarEntity carSave = repo.save(car);
        return modelMapper.map(carSave,CarDTOResponse.class);
    }

    @Override
    public CarDTOResponse findByModel(String model) {
        CarEntity car = repo.findByModel(model)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo no existe"));
        return modelMapper.map(car,CarDTOResponse.class);
    }

    @Override
    public CarDTOResponse updateCar(CarDTO carDTO, Long id) {
        BrandEntity brand = brandRepo.findByBrandIgnoreCase(carDTO.brand()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La marca:"+carDTO.brand() +" no existe"));
        FuelTypeEntity fuelType = fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El combustible:"+carDTO.fuel() +" no existe"));
        TransmissionEntity transmission = transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(carDTO.transmission().transmission(),carDTO.transmission().speeds()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La transmission:"+carDTO.transmission() +" no existe"));
        CarTypeEntity carType = carTypeRepo.findByTypeIgnoreCase(carDTO.type()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El tipo de vehiculo:"+carDTO.type() +" no existe"));

        CarEntity car = repo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El vehiculo con el id :"+ id +" no existe"));

        car.setModel(carDTO.model());
        car.setAge(carDTO.age());
        car.setKm(carDTO.km());
        car.setColor(carDTO.color());
        car.setDescription(carDTO.description());
        car.setMotor(carDTO.motor());
        car.setPrice(carDTO.price());
        car.setBrand(brand);
        car.setFuel(fuelType);
        car.setTransmission(transmission);
        car.setType(carType);

        CarEntity carSave = repo.save(car);

        return modelMapper.map(carSave,CarDTOResponse.class);
    }

    @Override
    public void deleteCar(Long id) {
        try {
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el vehiculo porque está en uso.");
        }
    }
}
