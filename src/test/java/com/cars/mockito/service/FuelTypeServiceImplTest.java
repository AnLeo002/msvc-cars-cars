package com.cars.mockito.service;

import com.cars.controller.dto.FuelTypeDTO;
import com.cars.controller.dto.FuelTypeDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.FuelTypeEntity;
import com.cars.repo.FuelTypeRepo;
import com.cars.service.impl.FuelTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuelTypeServiceImplTest {
    @Mock
    private FuelTypeRepo fuelTypeRepo;
    @InjectMocks
    private FuelTypeServiceImpl fuelTypeService;
    private FuelTypeEntity fuelTest;
    @BeforeEach
    void setup(){
        fuelTest = DataProvider.createTestFuel();
    }
    @Test
    void findByFuelTest(){
        when(fuelTypeRepo.findByFuelIgnoreCase(fuelTest.getFuel()))
                .thenReturn(Optional.of(fuelTest));
        FuelTypeDTOResponse result = fuelTypeService.findByFuel(fuelTest.getFuel());

        assertNotNull(result);
        assertEquals("corriente",result.fuel());
        // Verificaciones de interacción correctas
        verify(fuelTypeRepo, times(1)).findByFuelIgnoreCase(fuelTest.getFuel() );
    }
    @Test
    void findByFuelErrorTest(){
        when(fuelTypeRepo.findByFuelIgnoreCase(fuelTest.getFuel()))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            fuelTypeService.findByFuel(fuelTest.getFuel());
        });
    }
    @Test
    void findAllTest(){
        FuelTypeEntity fuelTest2 = new FuelTypeEntity();
        fuelTest2.setId(1L);
        fuelTest2.setFuel("extra");
        List<FuelTypeEntity> listTest = List.of(fuelTest,fuelTest2);

        when(fuelTypeRepo.findAll()).thenReturn(listTest);

        List<FuelTypeDTOResponse> result = fuelTypeService.findAll();

        assertNotNull(result);
        assertEquals("corriente",result.get(0).fuel());
        verify(fuelTypeRepo,times(1)).findAll();
    }
    @Test
    void createFuelTest(){
        FuelTypeDTO fuelDTO = new FuelTypeDTO("corriente");

        when(fuelTypeRepo.findByFuelIgnoreCase(fuelDTO.fuel()))
                .thenReturn(Optional.empty());
        when(fuelTypeRepo.save(any(FuelTypeEntity.class))).thenReturn(fuelTest);

        FuelTypeDTOResponse result = fuelTypeService.createFuelType(fuelDTO);

        assertNotNull(result);
        assertEquals("corriente",result.fuel());
        // Verificaciones de interacción correctas
        verify(fuelTypeRepo, times(1)).save(any(FuelTypeEntity.class));
    }
    @Test
    void createFuelErrorTest() {
        FuelTypeDTO fuelDTO = new FuelTypeDTO("corriente");

        when(fuelTypeRepo.findByFuelIgnoreCase(fuelDTO.fuel()))
                .thenReturn(Optional.of(fuelTest));
        assertThrows(ResponseStatusException.class,()->{
            fuelTypeService.createFuelType(fuelDTO);
        });
    }
    @Test
    void updateFuelTest(){
        FuelTypeDTO fuelDTO = new FuelTypeDTO("extra");
        Long id = 1L;
        FuelTypeEntity updatedFuel = FuelTypeEntity.builder().id(id).fuel(fuelDTO.fuel()).build();


        when(fuelTypeRepo.findById(id))
                .thenReturn(Optional.of(fuelTest));
        when(fuelTypeRepo.save(fuelTest)).thenReturn(updatedFuel);

        FuelTypeDTOResponse result = fuelTypeService.updateFuelType(fuelDTO,id);

        assertNotNull(result);
        assertEquals("extra",result.fuel());
        // Verificaciones de interacción correctas
        verify(fuelTypeRepo, times(1)).save(any(FuelTypeEntity.class));
    }
    @Test
    void updateFuelErrorTest() {
        FuelTypeDTO fuelDTO = new FuelTypeDTO("extra");
        Long id = 1L;

        when(fuelTypeRepo.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            fuelTypeService.updateFuelType(fuelDTO,id);
        });
    }
    @Test
    void deleteFuelTypeTest(){
        Long id = 1L;

        when(fuelTypeRepo.existsById(id)).thenReturn(true);

        fuelTypeService.deleteFuelType(id);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(fuelTypeRepo).deleteById(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(id, argumentCaptor.getValue());
        verify(fuelTypeRepo,times(1)).deleteById(id);
    }
    @Test
    void deleteFuelTypeNotContentTest(){
        Long id = 1L;

        when(fuelTypeRepo.existsById(id)).thenReturn(false);

        assertThrows(ResponseStatusException.class,()->{
            fuelTypeService.deleteFuelType(id);
        });
    }
    @Test
    void deleteFuelTypeErrorTest(){
        Long id = 1L;

        when(fuelTypeRepo.existsById(id)).thenReturn(true);

        doThrow(new RuntimeException("Error")).when(fuelTypeRepo).deleteById(id);

        assertThrows(ResponseStatusException.class,()->{
            fuelTypeService.deleteFuelType(id);
        });
    }
}
