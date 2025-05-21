package com.cars.mockito.service;

import com.cars.controller.dto.TransmissionDTO;
import com.cars.controller.dto.TransmissionDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.TransmissionEntity;
import com.cars.repo.TransmissionRepo;
import com.cars.service.impl.TransmissionServiceImpl;
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
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TransmissionServiceImplTest {
    @Mock
    private TransmissionRepo transmissionRepo;
    @InjectMocks
    private TransmissionServiceImpl transmissionService;
    private TransmissionEntity transTest;
    private TransmissionDTO transDTO;
    @BeforeEach
    void setup(){
        transTest = DataProvider.createTestTrans();
        transDTO = new TransmissionDTO("automatica",4);
    }
    @Test
    void findByTransmissionTest(){
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(transDTO.transmission(),transDTO.speeds()))
                .thenReturn(Optional.of(transTest));
        TransmissionDTOResponse result = transmissionService.findByTransmissionAndSpeeds(transDTO);
        assertNotNull(result);
        assertEquals("automatica",result.transmission());
        // Verificaciones de interacción correctas
                verify(transmissionRepo, times(1)).findByTransmissionIgnoreCaseAndSpeeds(transDTO.transmission(),transDTO.speeds());
    }
    @Test
    void findByTransmissionErrorTest(){
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(transDTO.transmission(),transDTO.speeds()))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            transmissionService.findByTransmissionAndSpeeds(transDTO);
        });
    }
    @Test
    void findAllTest(){
        TransmissionEntity trans2 = TransmissionEntity.builder().id(2L).transmission("manual").speeds(5).build();
        List<TransmissionEntity> list = List.of(transTest,trans2);
        when(transmissionRepo.findAll()).thenReturn(list);
        List<TransmissionDTOResponse> result = transmissionService.findAll();
        assertNotNull(result);
        assertNotNull("manual",result.get(1).transmission());
        verify(transmissionRepo,times(1)).findAll();
    }
    @Test
    void createTransmissionTest(){
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(transDTO.transmission(),transDTO.speeds()))
                .thenReturn(Optional.empty());
        when(transmissionRepo.save(any(TransmissionEntity.class))).thenReturn(transTest);
        TransmissionDTOResponse result = transmissionService.createTransmission(transDTO);
        assertNotNull(result);
        assertNotNull("automatico",result.transmission());
        verify(transmissionRepo,times(1)).save(any(TransmissionEntity.class));
    }
    @Test
    void createTransmissionNotFoundTest(){
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(transDTO.transmission(),transDTO.speeds()))
                .thenReturn(Optional.of(transTest));
        assertThrows(ResponseStatusException.class,()->{
            transmissionService.createTransmission(transDTO);
        });
    }
    @Test
    void updateTransmissionTest(){
        Long id = 1L;
        when(transmissionRepo.findById(id))
                .thenReturn(Optional.of(transTest));
        when(transmissionRepo.save(any(TransmissionEntity.class))).thenReturn(transTest);
        TransmissionDTOResponse result = transmissionService.updateTransmission(transDTO,id);
        assertNotNull(result);
        assertNotNull("automatico",result.transmission());
        verify(transmissionRepo,times(1)).save(any(TransmissionEntity.class));
    }
    @Test
    void updateTransmissionNotFoundTest(){
        Long id = 1L;
        when(transmissionRepo.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            transmissionService.updateTransmission(transDTO,id);
        });
    }
    @Test
    void deleteTransmissionTest(){
        Long id = 1l;
        when(transmissionRepo.existsById(id)).thenReturn(true);
        transmissionService.deleteTransmission(id);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(transmissionRepo).deleteById(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(id,argumentCaptor.getValue());
    }
    @Test
    void deleteTransmissionNotFoundTest(){
        Long id = 1l;
        when(transmissionRepo.existsById(id)).thenReturn(false);
        assertThrows(ResponseStatusException.class,()->{
            transmissionService.deleteTransmission(id);
        });
    }
    @Test
    void deleteTransmissionErrorTest(){
        Long id = 1l;
        when(transmissionRepo.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException("ERROR")).when(transmissionRepo).deleteById(id);
        assertThrows(ResponseStatusException.class,()->{
            transmissionService.deleteTransmission(id);
        });
    }

}
