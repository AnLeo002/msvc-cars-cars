package com.cars.service.impl;

import com.cars.controller.dto.CarDTO;
import com.cars.persistence.*;
import com.cars.repo.*;
import com.cars.service.record.CarValidateComponents;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarValidationService {
    private final CarTypeRepo carTypeRepo;
    private final FuelTypeRepo fuelTypeRepo;
    private final TransmissionRepo transmissionRepo;
    private final ModelRepo modelRepo;

    public CarValidationService(CarTypeRepo carTypeRepo, FuelTypeRepo fuelTypeRepo, TransmissionRepo transmissionRepo, ModelRepo modelRepo) {

        this.carTypeRepo = carTypeRepo;
        this.fuelTypeRepo = fuelTypeRepo;
        this.transmissionRepo = transmissionRepo;
        this.modelRepo = modelRepo;
    }

    public CarValidateComponents verifiedCarDTO(CarDTO carDTO){
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
        return new CarValidateComponents(fuelType,transmission,carType,model,version);
    }
}
