package com.cars.mockito.service;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.CarEntity;
import com.cars.persistence.TransmissionEntity;
import com.cars.repo.*;
import com.cars.service.impl.CarServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {
    @Mock
    private  CarRepo carRepo;
    @Mock
    private  CarTypeRepo carTypeRepo;
    @Mock
    private  FuelTypeRepo fuelTypeRepo;
    @Mock
    private  TransmissionRepo transmissionRepo;
    @Mock
    private  ModelRepo modelRepo;
    @Mock
    private  ModelMapper modelMapper;
    @InjectMocks
    private CarServiceImpl carService;
    CarEntity testCar;
    @BeforeEach
    void setup(){
        testCar = DataProvider.createTestCar("BDP018");
    }
    @Test
    void testFindById(){
        when(carRepo.findById(testCar.getPlate())).thenReturn(
                Optional.of(testCar)
        );
        when(modelMapper.map(testCar, CarDTOResponse.class)).thenReturn(
                DataProvider.createTestCarMapResponse(testCar)
        );
        CarDTOResponse car = carService.findById(testCar.getPlate());
        assertEquals(testCar.getBrand().getBrand(),car.brand());
        assertNotNull(car);
    }
    @Test
    void testFindByIdIsEmpty(){
        when(carRepo.findById(testCar.getPlate())).thenReturn(
                Optional.empty()
        );
        assertThrows(RuntimeException.class,()->{
            carService.findById(testCar.getPlate());
        });
    }
    @Test
    void testFindAll(){
        List<CarEntity> mockCars = DataProvider.createTestCarList();
        List<CarDTOResponse> expectedResponses = mockCars.stream()
                .map(car -> DataProvider.createTestCarMapResponse(car))
                .toList();
        //When
        when(carRepo.findAll()).thenReturn(
                mockCars
        );
        when(modelMapper.map(mockCars.get(0), CarDTOResponse.class)).thenReturn(
                expectedResponses.get(0)
        );
        List<CarDTOResponse> result = carService.findAll();
        //Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("BDP018",result.get(0).plate());
        assertEquals("nissan",result.get(0).brand());
        // Verifica que se llamó al repositorio
        verify(carRepo, times(1)).findAll();
        // Verifica que se usó el ModelMapper para cada elemento
        verify(modelMapper, times(mockCars.size())).map(any(CarEntity.class), eq(CarDTOResponse.class));
    }
    @Test
    void TestFindAllError(){
        when(carRepo.findAll()).thenReturn(List.of());
        //then
        assertThrows(ResponseStatusException.class,()->{
            carService.findAll();
        });
    }
    @Test
    void testCreateCar(){
        //Given
        CarDTO carDTO = DataProvider.createTestCarDTO("BDP018");
        //When
        when(carRepo.existsById(eq(carDTO.plate()))).thenReturn(false);
        when(carRepo.save(any(CarEntity.class))).thenReturn(testCar);
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
        when(modelMapper.map(testCar, CarDTOResponse.class)).
                thenReturn(DataProvider.createTestCarMapResponse(testCar));
        //Then
        CarDTOResponse result = carService.createCar(carDTO);
        assertNotNull(result);
        assertEquals("BDP018",result.plate());
        assertEquals("nissan",result.brand());
        // Verificaciones de interacción correctas
        verify(carRepo, times(1)).existsById(eq(carDTO.plate()));
        verify(carRepo, times(1)).save(any(CarEntity.class));
    }
    @Test
    void testCreateCarPlateExist(){
        //Given
        CarDTO carDTO = DataProvider.createTestCarDTO("BDP018");
        //When
        when(carRepo.existsById(eq(carDTO.plate()))).thenReturn(true);
        assertThrows(ResponseStatusException.class,()->{
            carService.createCar(carDTO);
        });
    }
    @Test
    void testCreateCarFuelNoExist(){
        //Given
        CarDTO carDTO = DataProvider.createTestCarDTO("BDP018");
        //When
        when(carRepo.existsById(eq(carDTO.plate()))).thenReturn(false);
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carService.createCar(carDTO);
        });
    }
    @Test
    void testCreateCarTransNoExist(){
        //Given
        CarDTO carDTO = DataProvider.createTestCarDTO("BDP018");
        //When
        when(carRepo.existsById(eq(carDTO.plate()))).thenReturn(false);
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carService.createCar(carDTO);
        });
    }
    @Test
    void testCreateCarTypeNoExist() {
        //Given
        CarDTO carDTO = DataProvider.createTestCarDTO("BDP018");
        //When
        when(carRepo.existsById(eq(carDTO.plate()))).thenReturn(false);
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
            carService.createCar(carDTO);
        });
    }
}
