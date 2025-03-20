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

@Service
public class CarServiceImpl implements ICarService {
    private final CarRepo repo;
    private final CarTypeRepo carTypeRepo;
    private final FuelTypeRepo fuelTypeRepo;
    private final TransmissionRepo transmissionRepo;
    private final ModelRepo modelRepo;
    private final ModelMapper modelMapper;

    public CarServiceImpl(CarRepo repo, CarTypeRepo carTypeRepo, FuelTypeRepo fuelTypeRepo, TransmissionRepo transmissionRepo, ModelRepo modelRepo, ModelMapper modelMapper) {
        this.repo = repo;
        this.carTypeRepo = carTypeRepo;
        this.fuelTypeRepo = fuelTypeRepo;
        this.transmissionRepo = transmissionRepo;
        this.modelRepo = modelRepo;
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
                .toList();
    }

    @Override
    public CarDTOResponse createCar(CarDTO carDTO) {

        FuelTypeEntity fuelType = fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El combustible :"+carDTO.fuel() +" no existe"));
        TransmissionEntity transmission = transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(carDTO.transmission().transmission(),carDTO.transmission().speeds())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La transmission: "+carDTO.transmission() +" no existe"));
        CarTypeEntity carType = carTypeRepo.findByTypeIgnoreCase(carDTO.type())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El tipo de vehiculo: "+carDTO.type() +" no existe"));
        ModelEntity model = modelRepo.findByModelIgnoreCase(carDTO.model())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo: "+carDTO.model() +" no existe"));
        VersionEntity version = model.getVersionEntities().stream().filter(versionEntity -> versionEntity.getVersion().equalsIgnoreCase(carDTO.version()))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT,"La version que se esta buscando no se encuentra o no le pertenece a este modelo"));
        if (!model.getBrand().getBrand().equalsIgnoreCase(carDTO.brand())) {//Si el modelo no tiene la misma marca que el dto lanza error
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El modelo '" + carDTO.model() + "' no pertenece a la marca '" + carDTO.brand() + "'");
        }


        CarEntity car = CarEntity.builder()
                .age(carDTO.age())
                .km(carDTO.km())
                .color(carDTO.color())
                .description(carDTO.description())
                .price(carDTO.price())
                .motor(carDTO.motor())
                .brand(model.getBrand())
                .fuel(fuelType)
                .transmission(transmission)
                .type(carType)
                .model(model)
                .version(version)
                .build();

        try {
            CarEntity carSave = repo.save(car);
            return modelMapper.map(carSave, CarDTOResponse.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el vehiculo", e);
        }
    }

    @Override
    public CarDTOResponse updateCar(CarDTO carDTO, Long id) {
        CarEntity car = repo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El vehiculo con el id :"+ id +" no existe"));

        FuelTypeEntity fuelType = fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El combustible:"+carDTO.fuel() +" no existe"));
        TransmissionEntity transmission = transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(carDTO.transmission().transmission(),carDTO.transmission().speeds())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La transmission:"+carDTO.transmission() +" no existe"));
        CarTypeEntity carType = carTypeRepo.findByTypeIgnoreCase(carDTO.type())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El tipo de vehiculo:"+carDTO.type() +" no existe"));
        ModelEntity model = modelRepo.findByModelIgnoreCase(carDTO.model())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo: "+carDTO.model() +" no existe"));
        VersionEntity version = model.getVersionEntities().stream()
                .filter(versionEntity -> versionEntity.getVersion().equalsIgnoreCase(carDTO.version()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "La versión '" + carDTO.version() + "' no pertenece al modelo '" + carDTO.model() + "'"));

        if (!model.getBrand().getBrand().equalsIgnoreCase(carDTO.brand())) {//Si el modelo no tiene la misma marca que el dto lanza error
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El modelo '" + carDTO.model() + "' no pertenece a la marca '" + carDTO.brand() + "'");
        }

        car.setAge(carDTO.age());
        car.setKm(carDTO.km());
        car.setColor(carDTO.color());
        car.setDescription(carDTO.description());
        car.setMotor(carDTO.motor());
        car.setPrice(carDTO.price());
        car.setBrand(model.getBrand());
        car.setFuel(fuelType);
        car.setTransmission(transmission);
        car.setType(carType);
        car.setModel(model);
        car.setVersion(version);

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
