package com.cars.service;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;

import java.util.List;

public interface ICarService {
    CarDTOResponse findById(String plate);
    List<CarDTOResponse> findAll();
    CarDTOResponse createCar(CarDTO carDTO);
    CarDTOResponse updateCar(CarDTO carDTO,String plate);
    void deleteCar(String plate);
}
