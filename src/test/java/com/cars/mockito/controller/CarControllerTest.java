package com.cars.mockito.controller;

import com.cars.controller.CarController;
import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.CarEntity;
import com.cars.service.ICarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
    @Mock
    private ICarService carService;
    @InjectMocks
    private CarController carController;
    private CarEntity carTest;
    private CarDTO carDTO;
    private CarDTOResponse carDTOResponse;
    @BeforeEach
    void setup(){
        carTest = DataProvider.createTestCar("bdp018");
        carDTO = DataProvider.createTestCarDTO("BDP018","amarillo");
        carDTOResponse = DataProvider.createTestCarMapResponse(carTest);
    }
    @Test
    void findAll(){
        CarEntity carTest2 = DataProvider.createTestCar("bbb102");
        CarDTOResponse carDTOResponse2 = DataProvider.createTestCarMapResponse(carTest2);
        List<CarDTOResponse> list = List.of(carDTOResponse,carDTOResponse2);
        when(carService.findAll()).thenReturn(list);
        ResponseEntity<List<CarDTOResponse>> result = carController.findAll();
        assertNotNull(result);
        assertEquals("bdp018",result.getBody().get(0).plate());
        assertEquals("bbb102",result.getBody().get(1).plate());
        verify(carService, times(1)).findAll();
    }
    @Test
    void findByPlateTest(){
        String plate = "bdp018";
        when(carService.findById(plate)).thenReturn(carDTOResponse);
        ResponseEntity<CarDTOResponse> result = carController.findByPlate(plate);
        assertNotNull(result);
        assertEquals("bdp018",result.getBody().plate());
        verify(carService, times(1)).findById(plate);
    }
    @Test
    void createCar(){
        carDTO = DataProvider.createTestCarDTO("bdp018","rojo corsa");
        when(carService.createCar(carDTO)).thenReturn(carDTOResponse);
        ResponseEntity<CarDTOResponse> result = carController.createCar(carDTO);
        assertNotNull(result);
        assertEquals("bdp018",result.getBody().plate());
        verify(carService, times(1)).createCar(carDTO);
    }
    @Test
    void updateCar(){
        carDTO = DataProvider.createTestCarDTO("bdp018","gris");
        carTest.setColor(carDTO.color());
        carDTOResponse = DataProvider.createTestCarMapResponse(carTest);
        when(carService.updateCar(carDTO)).thenReturn(carDTOResponse);
        ResponseEntity<CarDTOResponse> result = carController.updateCar(carDTO);
        assertNotNull(result);
        assertEquals("gris",result.getBody().color());
        verify(carService, times(1)).updateCar(carDTO);
    }
    @Test
    void deleteBrandTest(){
        String plate = "bdp018";
        ResponseEntity<String> result = carController.deleteCar(plate);
        assertNotNull(result);
        assertEquals(204,result.getStatusCode().value());
        verify(carService,times(1)).deleteCar(plate);
    }
}
