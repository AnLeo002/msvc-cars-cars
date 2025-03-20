package com.cars.service;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;

import java.util.List;

public interface ICarService {
    CarDTOResponse findById(Long id);
    List<CarDTOResponse> findAll();
    CarDTOResponse createCar(CarDTO carDTO);
    CarDTOResponse updateCar(CarDTO carDTO,Long id);
    void deleteCar(Long id);
}
