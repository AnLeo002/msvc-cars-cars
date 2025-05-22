package com.cars.mockito.controller;
import com.cars.controller.FuelTypeController;
import com.cars.controller.dto.FuelTypeDTO;
import com.cars.controller.dto.FuelTypeDTOResponse;
import com.cars.service.IFuelTypeService;
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
class FuelTypeControllerTest {
    @Mock
    private IFuelTypeService fuelTypeService;
    @InjectMocks
    private FuelTypeController controller;
    private FuelTypeDTOResponse fuelTypeDTOResponse;
    @BeforeEach
    void setup(){
        fuelTypeDTOResponse = new FuelTypeDTOResponse(1L,"corriente");
    }
    @Test
    void findAllTest(){
        FuelTypeDTOResponse fuelTypeDTOResponse2 = new FuelTypeDTOResponse(2L,"extra");
        List<FuelTypeDTOResponse> list = List.of(fuelTypeDTOResponse,fuelTypeDTOResponse2);
        when(fuelTypeService.findAll()).thenReturn(list);
        ResponseEntity<List<FuelTypeDTOResponse>> result = controller.findAll();
        assertNotNull(result);
        assertEquals("corriente",result.getBody().get(0).fuel());
        assertEquals("extra",result.getBody().get(1).fuel());
        verify(fuelTypeService, times(1)).findAll();
    }
    @Test
    void findByFuelTest(){
        String fuel = "corriente";
        when(fuelTypeService.findByFuel(fuel)).thenReturn(fuelTypeDTOResponse);
        ResponseEntity<FuelTypeDTOResponse> result = controller.findByFuel(fuel);
        assertNotNull(result);
        assertEquals("corriente",result.getBody().fuel());
        verify(fuelTypeService, times(1)).findByFuel(fuel);
    }
    @Test
    void createFuelTest(){
        FuelTypeDTO fuelTypeDTO = new FuelTypeDTO("corriente");
        when(fuelTypeService.createFuelType(fuelTypeDTO)).thenReturn(fuelTypeDTOResponse);
        ResponseEntity<FuelTypeDTOResponse> result = controller.createFuel(fuelTypeDTO);
        assertNotNull(result);
        assertEquals("corriente",result.getBody().fuel());
        verify(fuelTypeService, times(1)).createFuelType(fuelTypeDTO);
    }
    @Test
    void updateFuelTest(){
        Long id = 1L;
        FuelTypeDTO fuelTypeDTO = new FuelTypeDTO("extra");
        FuelTypeDTOResponse fuelUpdate = new FuelTypeDTOResponse(1L,"extra");
        when(fuelTypeService.updateFuelType(fuelTypeDTO,id)).thenReturn(fuelUpdate);
        ResponseEntity<FuelTypeDTOResponse> result = controller.updateFuel(fuelTypeDTO,id);
        assertNotNull(result);
        assertEquals("extra",result.getBody().fuel());
        verify(fuelTypeService, times(1)).updateFuelType(fuelTypeDTO,id);
    }
    @Test
    void deleteFuelTest(){
        Long id = 1l;
        ResponseEntity<String> result = controller.deleteFuel(id);
        assertNotNull(result);
        assertEquals(204,result.getStatusCode().value());
        verify(fuelTypeService,times(1)).deleteFuelType(id);
    }
}

