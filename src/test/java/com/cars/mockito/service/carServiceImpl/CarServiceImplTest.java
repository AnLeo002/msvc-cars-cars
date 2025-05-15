package com.cars.mockito.service.carServiceImpl;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.CarEntity;
import com.cars.repo.*;
import com.cars.service.impl.CarServiceImpl;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {
    @Mock
    private  CarRepo carRepo;
    @Mock
    private  ModelMapper modelMapper;
    @InjectMocks
    private CarServiceImpl carService;
    CarEntity testCar;
    CarDTO carDTO;
    @BeforeEach
    void setup(){
        testCar = DataProvider.createTestCar("BDP018");
        carDTO = DataProvider.createTestCarDTO("BDP018","nissan");
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
    void testFindAllError(){
        when(carRepo.findAll()).thenReturn(List.of());
        //then
        assertThrows(ResponseStatusException.class,()->{
            carService.findAll();
        });
    }
    @Test
    void testDeleteCar(){
        //Given
        String plate = "BDP018";
        //When
        when(carRepo.existsById(plate)).thenReturn(true);

        carService.deleteCar(plate);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(carRepo).deleteById(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(plate, argumentCaptor.getValue());
    }
    @Test
    void testDeleteCarErrorByNotExistPlate(){
        String plate = "BDP018";

        when(carRepo.existsById(plate)).thenReturn(false);
        assertThrows(ResponseStatusException.class,()->{
            carService.deleteCar(plate);
        });
    }
    @Test
    void testDeleteCarWithCatch(){
        //Given
        String plate = "BDP018";

        when(carRepo.existsById(plate)).thenReturn(true);
        doThrow(new RuntimeException("Constraint violation")).when(carRepo).deleteById(plate);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            carService.deleteCar(plate);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("No se puede eliminar el vehiculo porque está en uso.", exception.getReason());
    }

}
