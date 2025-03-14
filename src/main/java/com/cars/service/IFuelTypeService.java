package com.cars.service;

import com.cars.controller.dto.FuelTypeDTO;
import com.cars.controller.dto.FuelTypeDTOResponse;

import java.util.List;

public interface IFuelTypeService {
    FuelTypeDTOResponse findByFuel(String fuel);
    List<FuelTypeDTOResponse> findAll();
    FuelTypeDTOResponse createFuelType(FuelTypeDTO fuelTypeDTO);
    FuelTypeDTOResponse updateFuelType(FuelTypeDTO fuelTypeDTO,Long id);
    void deleteFuelType(Long id);
}
