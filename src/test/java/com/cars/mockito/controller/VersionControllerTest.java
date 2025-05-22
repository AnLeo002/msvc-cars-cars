package com.cars.mockito.controller;

import com.cars.controller.VersionController;
import com.cars.controller.dto.VersionDTO;
import com.cars.controller.dto.VersionDTOResponse;
import com.cars.service.IVersionService;
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
class VersionControllerTest {
    @Mock
    private IVersionService versionService;
    @InjectMocks
    private VersionController controller;
    private VersionDTOResponse versionDTOResponse;
    @BeforeEach
    void setup(){
        versionDTOResponse = new VersionDTOResponse(1L,"b13");
    }
    @Test
    void findAll(){
        VersionDTOResponse versionDTOResponse2 = new VersionDTOResponse(2L,"gt3");
        List<VersionDTOResponse> list = List.of(versionDTOResponse,versionDTOResponse2);
        when(versionService.findAll()).thenReturn(list);

        ResponseEntity<List<VersionDTOResponse>> result = controller.findAll();
        assertNotNull(result);
        assertEquals("b13",result.getBody().get(0).version());
        assertEquals("gt3",result.getBody().get(1).version());
        verify(versionService, times(1)).findAll();
    }
    @Test
    void findByIdTest(){
        Long id= 1L;
        when(versionService.findById(id)).thenReturn(versionDTOResponse);
        ResponseEntity<VersionDTOResponse> result = controller.findById(id);
        assertNotNull(result);
        assertEquals("b13",result.getBody().version());
        verify(versionService, times(1)).findById(id);
    }
    @Test
    void findByVersionTest(){
        String version = "b13";
        when(versionService.findByVersion(version)).thenReturn(versionDTOResponse);
        ResponseEntity<VersionDTOResponse> result = controller.findByVersion(version);
        assertNotNull(result);
        assertEquals("b13",result.getBody().version());
        verify(versionService, times(1)).findByVersion(version);
    }
    @Test
    void createBrandTest(){
        VersionDTO versionDTO = new VersionDTO("b13");
        when(versionService.createVersion(versionDTO))
                .thenReturn(versionDTOResponse);

        ResponseEntity<VersionDTOResponse> result = controller.createVersion(versionDTO);
        assertNotNull(result);
        assertEquals("b13",result.getBody().version());
        verify(versionService,times(1)).createVersion(versionDTO);
    }
    @Test
    void updateBrandTest(){
        VersionDTO versionDTO = new VersionDTO("gts");
        Long id = 1L;
        VersionDTOResponse updatedVersion = new VersionDTOResponse(id,versionDTO.version());
        when(versionService.updateVersion(versionDTO,id))
                .thenReturn(updatedVersion);

        ResponseEntity<VersionDTOResponse> result = controller.updateVersion(versionDTO,id);
        assertNotNull(result);
        assertEquals("gts",result.getBody().version());
        verify(versionService,times(1)).updateVersion(versionDTO,id);
    }

    @Test
    void deleteBrandTest(){
        Long id = 1l;
        ResponseEntity<String> result = controller.deleteVersion(id);
        assertNotNull(result);
        assertEquals(204,result.getStatusCode().value());
        verify(versionService,times(1)).deleteVersion(id);
    }
}
