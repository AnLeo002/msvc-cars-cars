package com.cars.service.impl;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.ModelDTO;
import com.cars.persistence.*;
import com.cars.repo.*;
import com.cars.service.record.CarValidateComponents;
import com.cars.service.record.ModelValidateComponents;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//Esta clase se encarga de validar información en la base de datos
@Service
public class EntityValidatorService {
    private final CarTypeRepo carTypeRepo;
    private final FuelTypeRepo fuelTypeRepo;
    private final TransmissionRepo transmissionRepo;
    private final ModelRepo modelRepo;
    private final BrandRepo brandRepo;
    private final VersionRepo versionRepo;

    public EntityValidatorService(CarTypeRepo carTypeRepo, FuelTypeRepo fuelTypeRepo, TransmissionRepo transmissionRepo, ModelRepo modelRepo, BrandRepo brandRepo, VersionRepo versionRepo) {

        this.carTypeRepo = carTypeRepo;
        this.fuelTypeRepo = fuelTypeRepo;
        this.transmissionRepo = transmissionRepo;
        this.modelRepo = modelRepo;
        this.brandRepo = brandRepo;
        this.versionRepo = versionRepo;
    }

    public FuelTypeEntity validateFuel(String fuel) {
        return fuelTypeRepo.findByFuelIgnoreCase(fuel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El combustible: " + fuel + " no existe"));
    }

    public TransmissionEntity validateTransmission(String transmission, int speeds) {
        return transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(transmission, speeds)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La transmisión no existe"));
    }

    public CarTypeEntity validateCarType(String type) {
        return carTypeRepo.findByTypeIgnoreCase(type)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El tipo de vehículo no existe"));
    }

    public ModelEntity validateModel(String model) {
        return modelRepo.findByModelIgnoreCase(model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El modelo no existe"));
    }

    public VersionEntity validateVersion(String version, String model) {
        ModelEntity modelFind = validateModel(model);
        return modelFind.getVersionEntities().stream()
                .filter(v -> v.getVersion().equalsIgnoreCase(version))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "La versión '" + version + "' no pertenece al modelo '" + model + "'"));
    }
    public Set<VersionEntity> validateAllVersions(List<String> versionsString){
        return versionRepo.findByVersionIn(versionsString)
                .stream()
                .collect(Collectors.toSet());
    }
    public void validateBrandMatch(String brand, ModelEntity model) {
        if (!model.getBrand().getBrand().equalsIgnoreCase(brand)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El modelo '" + model.getModel() + "' no pertenece a la marca '" + brand + "'");}
    }
    public BrandEntity validateBrand(String brand){
        return brandRepo.findByBrandIgnoreCase(brand)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La marca no se encuentra en base de datos"));
    }
    public CarValidateComponents verifiedCarDTO(CarDTO carDTO){
        FuelTypeEntity fuel = validateFuel(carDTO.fuel());
        TransmissionEntity transmission = validateTransmission(carDTO.transmission().transmission(), carDTO.transmission().speeds());
        CarTypeEntity carType = validateCarType(carDTO.type());
        ModelEntity model = validateModel(carDTO.model());
        VersionEntity version = validateVersion(carDTO.version(), carDTO.model());
        validateBrandMatch(carDTO.brand(), model);
        return new CarValidateComponents(fuel,transmission,carType,model,version);
    }
    public ModelValidateComponents verifiedModelDTO(ModelDTO modelDTO){
        BrandEntity brand = validateBrand(modelDTO.brand());
        Set<VersionEntity> versionEntities = validateAllVersions(modelDTO.versions());
        if(modelDTO.versions().size()!= versionEntities.size()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Algunas versiones no se encuentran en la base de datos");
        }
        return new ModelValidateComponents(brand,versionEntities);
    }
}
