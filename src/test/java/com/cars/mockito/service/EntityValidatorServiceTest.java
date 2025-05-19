package com.cars.mockito.service;

import com.cars.controller.dto.CarDTO;
import com.cars.mockito.DataProvider;
import com.cars.repo.*;
import com.cars.service.impl.EntityValidatorService;
import com.cars.service.record.CarValidateComponents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EntityValidatorServiceTest {
    @Mock
    private TransmissionRepo transmissionRepo;
    @Mock
    private FuelTypeRepo fuelTypeRepo;
    @Mock
    CarTypeRepo carTypeRepo;
    @Mock
    private ModelRepo modelRepo;
    @InjectMocks
    private EntityValidatorService carValidationService;
    private CarDTO carDTO;
    @BeforeEach
    void setUp(){
        carDTO = DataProvider.createTestCarDTO("BDP018","nissan");
    }
    @Test
    void testVerifiedCarDTO(){
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.of(DataProvider.createTestTrans()));
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type())).thenReturn(
                Optional.of(DataProvider.createTestCarType()));
        when(modelRepo.findByModelIgnoreCase(carDTO.model())).thenReturn(
                Optional.of(DataProvider.createTestModel()));
        //Then
        CarValidateComponents result = carValidationService.verifiedCarDTO(carDTO);
        assertNotNull(result);
        assertEquals("corriente",result.fuelType().getFuel());
        assertEquals("nissan",result.model().getBrand().getBrand());
        // Verificaciones de interacción correctas
        verify(modelRepo, times(1)).findByModelIgnoreCase(carDTO.model());
    }

    @Test
    void testCreateCarFuelNoExist(){
        //When
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.verifiedCarDTO(carDTO);
        });
    }
    @Test
    void testCreateCarTransNoExist(){
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateTransmission(carDTO.transmission().transmission(),carDTO.transmission().speeds());
        });
    }
    @Test
    void testCreateCarTypeNoExist() {
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.of(DataProvider.createTestTrans()));
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.empty());
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type())).thenReturn(
                Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.verifiedCarDTO(carDTO);
        });
    }
    @Test
    void testModelNoExist(){
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.of(DataProvider.createTestTrans()));
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type())).thenReturn(
                Optional.of(DataProvider.createTestCarType()));
        when(modelRepo.findByModelIgnoreCase(carDTO.model())).thenReturn(
                Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.verifiedCarDTO(carDTO);
        });
    }
    @Test
    void testVersionNoExist(){
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.of(DataProvider.createTestTrans()));
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.empty());
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type())).thenReturn(
                Optional.of(DataProvider.createTestCarType()));
        when(modelRepo.findByModelIgnoreCase(carDTO.model())).thenReturn(
                Optional.of(DataProvider.createTestModelVersionEmpty()));
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.verifiedCarDTO(carDTO);
        });
    }
    @Test
    void testBrandDifferentToDto(){
        carDTO = DataProvider.createTestCarDTO("BDP018","ferrari");
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.of(DataProvider.createTestTrans()));
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.empty());
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type())).thenReturn(
                Optional.of(DataProvider.createTestCarType()));
        when(modelRepo.findByModelIgnoreCase(carDTO.model())).thenReturn(
                Optional.of(DataProvider.createTestModel()));
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.verifiedCarDTO(carDTO);
        });
    }
}
