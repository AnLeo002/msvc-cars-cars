package com.cars.mockito.controller;

import com.cars.controller.CarTypeController;
import com.cars.controller.dto.CarTypeDTO;
import com.cars.controller.dto.CarTypeDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.CarTypeEntity;
import com.cars.service.ICarTypeService;
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
class CarTypeControllerTest {
    @Mock
    private ICarTypeService typeService;
    @InjectMocks
    private CarTypeController controller;
    CarTypeEntity carType;
    CarTypeDTOResponse carTypeDTOResponse;
    @BeforeEach
    void setup(){
        carType = DataProvider.createTestCarType();
        carTypeDTOResponse = new CarTypeDTOResponse(1L,"sedan");
    }
    @Test
    void findAllTest(){
        CarTypeDTOResponse carTypeDTOResponse2 = new CarTypeDTOResponse(2L,"coupe");
        List<CarTypeDTOResponse> list = List.of(carTypeDTOResponse,carTypeDTOResponse2);
        when(typeService.findAll()).thenReturn(list);
        ResponseEntity<List<CarTypeDTOResponse>> result = controller.findAll();
        assertNotNull(result);
        assertEquals("sedan",result.getBody().get(0).type());
        assertEquals("coupe",result.getBody().get(1).type());
        verify(typeService, times(1)).findAll();
    }
    @Test
    void findByIdTest(){
        Long id = 1L;
        when(typeService.findById(id)).thenReturn(carTypeDTOResponse);
        ResponseEntity<CarTypeDTOResponse> result = controller.findById(id);
        assertNotNull(result);
        assertEquals("sedan",result.getBody().type());
        verify(typeService, times(1)).findById(id);
    }
    @Test
    void createCarTypeTest(){
        CarTypeDTO carTypeDTO = new CarTypeDTO("sedan");
        when(typeService.createType(carTypeDTO)).thenReturn(carTypeDTOResponse);
        ResponseEntity<CarTypeDTOResponse> result = controller.createCarType(carTypeDTO);
        assertNotNull(result);
        assertEquals("sedan",result.getBody().type());
        verify(typeService, times(1)).createType(carTypeDTO);
    }
    @Test
    void updateCarTypeTest(){
        Long id = 1L;
        CarTypeDTO carTypeDTO = new CarTypeDTO("coupe");
        CarTypeDTOResponse carUpdateDTO = new CarTypeDTOResponse(1L,"coupe");
        when(typeService.updateTypeCar(carTypeDTO,id)).thenReturn(carUpdateDTO);
        ResponseEntity<CarTypeDTOResponse> result = controller.updateCarType(carTypeDTO,id);
        assertNotNull(result);
        assertEquals("coupe",result.getBody().type());
        verify(typeService, times(1)).updateTypeCar(carTypeDTO,id);
    }
    @Test
    void deleteBrandTest(){
        Long id = 1l;
        ResponseEntity<String> result = controller.deleteCarType(id);
        assertNotNull(result);
        assertEquals(204,result.getStatusCode().value());
        verify(typeService,times(1)).deleteTypeCar(id);
    }


}
