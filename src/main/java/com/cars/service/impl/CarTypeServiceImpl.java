package com.cars.service.impl;

import com.cars.controller.dto.CarTypeDTO;
import com.cars.controller.dto.CarTypeDTOResponse;
import com.cars.persistence.CarTypeEntity;
import com.cars.repo.CarTypeRepo;
import com.cars.service.ICarTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class CarTypeServiceImpl implements ICarTypeService {
    private final CarTypeRepo repo;

    public CarTypeServiceImpl(CarTypeRepo repo) {
        this.repo = repo;
    }

    @Override
    public CarTypeDTOResponse findById(Long id) {
        return repo.findById(id)
                .map(type -> new CarTypeDTOResponse(type.getId(), type.getType()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encuentra el tipo de vehiculo en la base de datos"));
    }

    @Override
    public List<CarTypeDTOResponse> findAll() {
        return repo.findAll()
                .stream().map(type -> new CarTypeDTOResponse(type.getId(), type.getType()))
                .toList();
    }

    @Override
    public CarTypeDTOResponse findByType(String type) {
        return repo.findByTypeIgnoreCase(type)
                .map(typeEntity -> new CarTypeDTOResponse(typeEntity.getId(), typeEntity.getType()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encuentra el tipo de vehiculo en la base de datos"));
    }

    @Override
    public CarTypeDTOResponse createType(CarTypeDTO carTypeDTO) {
        if(repo.findByTypeIgnoreCase(carTypeDTO.type()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No es posible crear este tipo de vehiculo");
        }
        CarTypeEntity type = CarTypeEntity.builder()
                .type(carTypeDTO.type())
                .build();
        CarTypeEntity typeSave = repo.save(type);
        return new CarTypeDTOResponse(typeSave.getId(), typeSave.getType());
    }

    @Override
    public CarTypeDTOResponse updateTypeCar(CarTypeDTO carTypeDTO, Long id) {
        CarTypeEntity carType = repo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encuentra el tipo de vehiculo en la base de datos"));
        carType.setType(carTypeDTO.type());
        CarTypeEntity typeSave = repo.save(carType);
        return new CarTypeDTOResponse(typeSave.getId(), typeSave.getType());
    }

    @Override
    public void deleteTypeCar(Long id) {
        try {
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el tipo de vehiculo porque está en uso.");
        }

    }
}
