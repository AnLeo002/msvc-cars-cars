package com.cars.mockito.controller;

import com.cars.controller.TransmissionController;
import com.cars.controller.dto.TransmissionDTO;
import com.cars.controller.dto.TransmissionDTOResponse;
import com.cars.service.ITransmissionService;
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
@ExtendWith(MockitoExtension.class)
class TransmissionControllerTest {
    @Mock
    private ITransmissionService transmissionService;
    @InjectMocks
    private TransmissionController controller;
    private TransmissionDTOResponse transmissionDTOResponse;
    @BeforeEach
    void setup(){
        transmissionDTOResponse = new TransmissionDTOResponse(1L,"automatica",4);
    }
    @Test
    void findAllTest(){
        TransmissionDTOResponse transmissionDTOResponse2 = new TransmissionDTOResponse(2L,"manual",4);
        List<TransmissionDTOResponse> list = List.of(transmissionDTOResponse,transmissionDTOResponse2);
        when(transmissionService.findAll()).thenReturn(list);
        ResponseEntity<List<TransmissionDTOResponse>> result = controller.findAll();
        assertNotNull(result);
        assertEquals("automatica",result.getBody().get(0).transmission());
        assertEquals("manual",result.getBody().get(1).transmission());
        verify(transmissionService, times(1)).findAll();
    }
    @Test
    void findByNameTest(){
        TransmissionDTO transmissionDTO = new TransmissionDTO("automatica",4);
        when(transmissionService.findByTransmissionAndSpeeds(transmissionDTO)).thenReturn(transmissionDTOResponse);
        ResponseEntity<TransmissionDTOResponse> result = controller.findByName(transmissionDTO);
        assertNotNull(result);
        assertEquals("automatica",result.getBody().transmission());
        assertEquals(4,result.getBody().speeds());
        verify(transmissionService, times(1)).findByTransmissionAndSpeeds(transmissionDTO);
    }
    @Test
    void createTransmissionTest(){
        TransmissionDTO transmissionDTO = new TransmissionDTO("automatica",4);
        when(transmissionService.createTransmission(transmissionDTO)).thenReturn(transmissionDTOResponse);
        ResponseEntity<TransmissionDTOResponse> result = controller.createTransmission(transmissionDTO);
        assertNotNull(result);
        assertEquals("automatica",result.getBody().transmission());
        assertEquals(4,result.getBody().speeds());
        verify(transmissionService, times(1)).createTransmission(transmissionDTO);
    }
    @Test
    void updateTransmissionTest(){
        Long id = 1L;
        TransmissionDTO transmissionDTO = new TransmissionDTO("manual",4);
        TransmissionDTOResponse transmissionUpdate = new TransmissionDTOResponse(id,transmissionDTO.transmission(),transmissionDTO.speeds());
        when(transmissionService.updateTransmission(transmissionDTO,id)).thenReturn(transmissionUpdate);
        ResponseEntity<TransmissionDTOResponse> result = controller.updateTransmission(transmissionDTO,id);
        assertNotNull(result);
        assertEquals("manual",result.getBody().transmission());
        assertEquals(4,result.getBody().speeds());
        verify(transmissionService, times(1)).updateTransmission(transmissionDTO,id);
    }

    @Test
    void deleteTransmissionTest(){
        Long id = 1l;
        ResponseEntity<String> result = controller.deleteTransmission(id);
        assertNotNull(result);
        assertEquals(204,result.getStatusCode().value());
        verify(transmissionService,times(1)).deleteTransmission(id);
    }
}
