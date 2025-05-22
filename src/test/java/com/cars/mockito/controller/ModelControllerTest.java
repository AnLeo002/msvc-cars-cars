package com.cars.mockito.controller;
import com.cars.controller.ModelController;
import com.cars.controller.dto.ModelDTO;
import com.cars.controller.dto.ModelDTOResponse;
import com.cars.service.IModelService;
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
class ModelControllerTest {
    @Mock
    private IModelService modelService;
    @InjectMocks
    private ModelController controller;
    private ModelDTOResponse modelDTOResponse;
    @BeforeEach
    void setup(){
        modelDTOResponse = new ModelDTOResponse(1L,"sentra","nissan",List.of("b13"));
    }
    @Test
    void finAllTest(){
        ModelDTOResponse modelDTOResponse2 = new ModelDTOResponse(2L,"911","porsche",List.of("gt3"));
        List<ModelDTOResponse> list = List.of(modelDTOResponse,modelDTOResponse2);
        when(modelService.findAll()).thenReturn(list);
        ResponseEntity<List<ModelDTOResponse>> result = controller.findAll();
        assertNotNull(result);
        assertEquals("sentra",result.getBody().get(0).model());
        assertEquals("911",result.getBody().get(1).model());
        verify(modelService, times(1)).findAll();
    }
    @Test
    void findByIdTest(){
        Long id = 1L;
        when(modelService.findById(id)).thenReturn(modelDTOResponse);
        ResponseEntity<ModelDTOResponse> result = controller.findById(id);
        assertNotNull(result);
        assertEquals("sentra",result.getBody().model());
        assertEquals("nissan",result.getBody().brand());
        verify(modelService, times(1)).findById(id);
    }
    @Test
    void findByModelTest(){
        String model = "sentra";
        when(modelService.findByModel(model)).thenReturn(modelDTOResponse);
        ResponseEntity<ModelDTOResponse> result = controller.findByModel(model);
        assertNotNull(result);
        assertEquals("sentra",result.getBody().model());
        assertEquals("nissan",result.getBody().brand());
        verify(modelService, times(1)).findByModel(model);
    }
    @Test
    void createModelTest(){
        ModelDTO modelDTO = new ModelDTO("sentra","nissan",List.of("b13"));
        when(modelService.createModel(modelDTO)).thenReturn(modelDTOResponse);
        ResponseEntity<ModelDTOResponse> result = controller.createModel(modelDTO);
        assertNotNull(result);
        assertEquals("sentra",result.getBody().model());
        assertEquals("nissan",result.getBody().brand());
        verify(modelService, times(1)).createModel(modelDTO);
    }
    @Test
    void updateModelTest(){
        Long id = 1L;
        ModelDTO modelDTO = new ModelDTO("911","nissan",List.of("b13"));
        ModelDTOResponse modelUpdate = new ModelDTOResponse(id,modelDTO.model(),modelDTO.brand(),modelDTO.versions());
        when(modelService.updateModel(modelDTO,id)).thenReturn(modelUpdate);
        ResponseEntity<ModelDTOResponse> result = controller.updateModel(modelDTO,id);
        assertNotNull(result);
        assertEquals("911",result.getBody().model());
        assertEquals("nissan",result.getBody().brand());
        verify(modelService, times(1)).updateModel(modelDTO,id);
    }
    @Test
    void deleteModelTest(){
        Long id = 1l;
        ResponseEntity<String> result = controller.deleteModel(id);
        assertNotNull(result);
        assertEquals(204,result.getStatusCode().value());
        verify(modelService,times(1)).deleteModel(id);
    }
}
