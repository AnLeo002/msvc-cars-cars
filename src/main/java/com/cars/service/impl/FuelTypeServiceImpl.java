package com.cars.service.impl;

import com.cars.controller.dto.FuelTypeDTO;
import com.cars.controller.dto.FuelTypeDTOResponse;
import com.cars.persistence.FuelTypeEntity;
import com.cars.repo.FuelTypeRepo;
import com.cars.service.IFuelTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class FuelTypeServiceImpl implements IFuelTypeService {
    public FuelTypeServiceImpl(FuelTypeRepo repo) {
        this.repo = repo;
    }

    private final FuelTypeRepo repo;

    @Override
    public FuelTypeDTOResponse findByFuel(String fuel) {
        return repo.findByFuelIgnoreCase(fuel)
                .map(fuelTypeEntity -> new FuelTypeDTOResponse(fuelTypeEntity.getId(), fuelTypeEntity.getFuel()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,fuel +" no se encuentra en base de datos"));
    }

    @Override
    public List<FuelTypeDTOResponse> findAll() {
        return repo.findAll()
                .stream().map(fuel -> new FuelTypeDTOResponse(fuel.getId(), fuel.getFuel()))
                .toList();
    }

    @Override
    public FuelTypeDTOResponse createFuelType(FuelTypeDTO fuelTypeDTO) {
        if(repo.findByFuelIgnoreCase(fuelTypeDTO.fuel()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No es posible crear este combustible");
        }
        FuelTypeEntity fuel = FuelTypeEntity.builder()
                .fuel(fuelTypeDTO.fuel())
                .build();
        FuelTypeEntity fuelSave = repo.save(fuel);
        return new FuelTypeDTOResponse(fuelSave.getId(), fuelSave.getFuel());
    }

    @Override
    public FuelTypeDTOResponse updateFuelType(FuelTypeDTO fuelTypeDTO, Long id) {
        FuelTypeEntity fuel = repo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "El combustible no se encuentra en la base de datos"));
        fuel.setFuel(fuelTypeDTO.fuel());
        FuelTypeEntity fuelSave = repo.save(fuel);
        return new FuelTypeDTOResponse(fuelSave.getId(), fuelSave.getFuel());
    }

    @Override
    public void deleteFuelType(Long id) {
        try {
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el combustible porque está en uso.");
        }
    }
}
