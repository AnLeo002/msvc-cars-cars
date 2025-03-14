package com.cars.service;

import com.cars.controller.dto.CarTypeDTO;
import com.cars.controller.dto.CarTypeDTOResponse;

import java.util.List;

public interface ICarTypeService {
    CarTypeDTOResponse findById(Long id);
    List<CarTypeDTOResponse> findAll();
    CarTypeDTOResponse findByType(String type);
    CarTypeDTOResponse createType(CarTypeDTO carTypeDTO);
    CarTypeDTOResponse updateTypeCar(CarTypeDTO carTypeDTO,Long id);
    void deleteTypeCar(Long id);
}
