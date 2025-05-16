package com.cars.mockito.service.carTypeServiceImpl;

import com.cars.controller.dto.CarTypeDTO;
import com.cars.controller.dto.CarTypeDTOResponse;
import com.cars.persistence.CarTypeEntity;
import com.cars.repo.CarTypeRepo;
import com.cars.service.impl.CarTypeServiceImpl;
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
class CarTypeServiceImplTest {
    @Mock
    private CarTypeRepo carTypeRepo;
    @InjectMocks
    private CarTypeServiceImpl carTypeService;
    private CarTypeEntity carType;
    @BeforeEach
    void setUp(){
        carType = new CarTypeEntity();
        carType.setId(1L);
        carType.setType("sedan");

    }
    @Test
    void findByIdTest(){
        Long id = 1L;
        when(carTypeRepo.findById(id)).thenReturn(Optional.of(carType));
        CarTypeDTOResponse result = carTypeService.findById(id);
        assertNotNull(result);
        assertEquals("sedan",result.type());
        verify(carTypeRepo,times(1)).findById(id);
    }
    @Test
    void findByIdNotFoundTest(){
        Long id = 1L;
        when(carTypeRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            carTypeService.findById(id);
        });
    }
    @Test
    void findAllTest(){
        CarTypeEntity carType2 = new CarTypeEntity();
        carType2.setId(2L);
        carType2.setType("coupe");
        List<CarTypeEntity> listTest = List.of(carType,carType2);
        when(carTypeRepo.findAll()).thenReturn(listTest);
        List<CarTypeDTOResponse> result = carTypeService.findAll();
        assertNotNull(result);
        assertEquals("sedan",result.get(0).type());
        verify(carTypeRepo,times(1)).findAll();
    }
    @Test
    void findByTypeTest(){
        String type = "sedan";
        when(carTypeRepo.findByTypeIgnoreCase(type)).thenReturn(Optional.of(carType));
        CarTypeDTOResponse result = carTypeService.findByType(type);
        assertNotNull(result);
        assertEquals("sedan",result.type());
        verify(carTypeRepo,times(1)).findByTypeIgnoreCase(type);
    }
    @Test
    void findByTypeNotFoundTest(){
        String type = "sedan";
        when(carTypeRepo.findByTypeIgnoreCase(type)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            carTypeService.findByType(type);
        });
    }
    @Test
    void createTypeTest(){
        CarTypeDTO carTypeDTO = new CarTypeDTO("sedan");
        when(carTypeRepo.findByTypeIgnoreCase(carTypeDTO.type()))
                .thenReturn(Optional.empty());
        when(carTypeRepo.save(any(CarTypeEntity.class)))
                .thenReturn(carType);
        CarTypeDTOResponse result = carTypeService.createType(carTypeDTO);
        assertNotNull(result);
        assertEquals("sedan",result.type());
        verify(carTypeRepo,times(1)).save(any(CarTypeEntity.class));
    }
    @Test
    void createTypeConflictTest(){
        CarTypeDTO carTypeDTO = new CarTypeDTO("sedan");
        when(carTypeRepo.findByTypeIgnoreCase(carTypeDTO.type()))
                .thenReturn(Optional.of(carType));
        assertThrows(ResponseStatusException.class,()->{
            carTypeService.createType(carTypeDTO);
        });
    }
    @Test
    void updateTypeTest(){
        CarTypeDTO carTypeDTO = new CarTypeDTO("coupe");
        Long id = 1L;
        CarTypeEntity updateType = CarTypeEntity.builder().id(id).type(carTypeDTO.type()).build();


        when(carTypeRepo.findById(id))
                .thenReturn(Optional.of(carType));
        when(carTypeRepo.save(any(CarTypeEntity.class)))
                .thenReturn(updateType);
        CarTypeDTOResponse result = carTypeService.updateTypeCar(carTypeDTO,id);
        assertNotNull(result);
        assertEquals("coupe",result.type());
        verify(carTypeRepo,times(1)).save(any(CarTypeEntity.class));
    }
    @Test
    void updateTypeConflictTest(){
        Long id = 1L;
        CarTypeDTO carTypeDTO = new CarTypeDTO("coupe");

        when(carTypeRepo.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            carTypeService.updateTypeCar(carTypeDTO,id);
        });
    }
    @Test
    void deleteTypeCarTest(){
        Long id = 1L;
        when(carTypeRepo.existsById(id)).thenReturn(true);

        carTypeService.deleteTypeCar(id);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(carTypeRepo).deleteById(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(id, argumentCaptor.getValue());
        verify(carTypeRepo,times(1)).deleteById(id);
    }
    @Test
    void deleteTypeCarNotFoundTest(){
        Long id = 1L;
        when(carTypeRepo.existsById(id)).thenReturn(false);
        assertThrows(ResponseStatusException.class,()->{
            carTypeService.deleteTypeCar(id);
        });
    }
    @Test
    void deleteTypeCarErrorTest(){
        Long id = 1L;
        when(carTypeRepo.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException("Error")).when(carTypeRepo).deleteById(id);
        assertThrows(ResponseStatusException.class,()->{
            carTypeService.deleteTypeCar(id);
        });
    }
}
