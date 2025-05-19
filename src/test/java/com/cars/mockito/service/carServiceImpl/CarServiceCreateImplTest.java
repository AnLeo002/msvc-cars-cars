package com.cars.mockito.service.carServiceImpl;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.CarEntity;
import com.cars.repo.*;
import com.cars.service.impl.CarServiceImpl;
import com.cars.service.impl.EntityValidatorService;
import com.cars.service.record.CarValidateComponents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CarServiceCreateImplTest {
    @Mock
    private CarRepo carRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EntityValidatorService carValidationService;
    @InjectMocks
    private CarServiceImpl carService;
    CarEntity testCar;
    CarDTO carDTO;
    CarValidateComponents components;
    @BeforeEach
    void setup(){
        testCar = DataProvider.createTestCar("BDP018");
        carDTO = DataProvider.createTestCarDTO("BDP018","nissan");
        components = new CarValidateComponents(
                DataProvider.createTestFuel(),
                DataProvider.createTestTrans(),
                DataProvider.createTestCarType(),
                DataProvider.createTestModel(),
                DataProvider.createTestVersion());
    }
    @Test
    void testCreateCar(){
        //When
        when(carRepo.existsById(carDTO.plate())).thenReturn(false);
        when(carRepo.save(any(CarEntity.class))).thenReturn(testCar);
        when(carValidationService.verifiedCarDTO(carDTO)).thenReturn(components);
        when(modelMapper.map(testCar, CarDTOResponse.class)).
                thenReturn(DataProvider.createTestCarMapResponse(testCar));
        //Then
        CarDTOResponse result = carService.createCar(carDTO);
        assertNotNull(result);
        assertEquals("BDP018",result.plate());
        assertEquals("nissan",result.brand());
        // Verificaciones de interacción correctas
        verify(carRepo, times(1)).existsById(carDTO.plate());
        verify(carRepo, times(1)).save(any(CarEntity.class));
    }
    @Test
    void testCreateCarPlateExist(){
        //When
        when(carRepo.existsById(carDTO.plate())).thenReturn(true);
        assertThrows(ResponseStatusException.class,()->{
            carService.createCar(carDTO);
        });
    }
    @Test
    void testCreateCarCatch() {
        //When
        when(carRepo.existsById(carDTO.plate())).thenReturn(false);
        when(carRepo.save(any())).thenReturn(any());
        when(carValidationService.verifiedCarDTO(carDTO)).thenReturn(components);
        when(modelMapper.map(testCar, CarDTOResponse.class)).
                thenReturn(DataProvider.createTestCarMapResponse(testCar));
        //Then
        assertThrows(ResponseStatusException.class,()->{
            carService.createCar(carDTO);
        });
    }
}
