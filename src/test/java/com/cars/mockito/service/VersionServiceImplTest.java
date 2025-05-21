package com.cars.mockito.service;

import com.cars.controller.dto.VersionDTO;
import com.cars.controller.dto.VersionDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.VersionEntity;
import com.cars.repo.VersionRepo;
import com.cars.service.impl.VersionServiceImpl;
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
 class VersionServiceImplTest {
    @Mock
    private VersionRepo versionRepo;
    @InjectMocks
    private VersionServiceImpl versionService;
    private VersionEntity versionTest;
    @BeforeEach
    void setup(){
        versionTest = DataProvider.createTestVersion();
    }
    @Test
    void findByIdTest(){
        Long id = 1L;
        when(versionRepo.findById(id)).thenReturn(Optional.of(versionTest));
        VersionDTOResponse result = versionService.findById(id);
        assertNotNull(result);
        assertEquals("b13",result.version());
        verify(versionRepo).findById(id);
    }
    @Test
    void findByIdNotFoundTest(){
        Long id = 1L;
        when(versionRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            versionService.findById(id);
        });
    }
    @Test
    void findAllTest(){
        VersionEntity version2 = VersionEntity.builder().id(2L).version("gts").build();
        List<VersionEntity> list = List.of(versionTest,version2);
        when(versionRepo.findAll()).thenReturn(list);
        List<VersionDTOResponse> result = versionService.findAll();

        assertNotNull(result);
        assertEquals("gts",result.get(1).version());
        verify(versionRepo).findAll();
    }
    @Test
    void findByVersionTest(){
        String version = "b13";
        when(versionRepo.findByVersionIgnoreCase(version)).thenReturn(Optional.of(versionTest));
        VersionDTOResponse result = versionService.findByVersion(version);
        assertNotNull(result);
        assertEquals("b13",result.version());
        verify(versionRepo).findByVersionIgnoreCase(version);
    }
    @Test
    void findByVersionNotFoundTest(){
        String version = "b13";
        when(versionRepo.findByVersionIgnoreCase(version)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,()->{
            versionService.findByVersion(version);
        });
    }
    @Test
    void createVersionTest(){
        VersionDTO versionDTO = new VersionDTO("b13");
        when(versionRepo.findByVersionIgnoreCase(versionDTO.version())).thenReturn(Optional.empty());
        when(versionRepo.save(any(VersionEntity.class))).thenReturn(versionTest);
        VersionDTOResponse result = versionService.createVersion(versionDTO);
        assertNotNull(result);
        assertEquals("b13",result.version());
        verify(versionRepo).save(any(VersionEntity.class));
    }
    @Test
    void createVersionConflictTest(){
        VersionDTO versionDTO = new VersionDTO("b13");
        when(versionRepo.findByVersionIgnoreCase(versionDTO.version())).thenReturn(Optional.of(versionTest));
        assertThrows(ResponseStatusException.class,()->{
            versionService.createVersion(versionDTO);
        });
    }
    @Test
    void updateVersionTest(){
        Long id = 1L;
        VersionDTO versionDTO = new VersionDTO("b13");
        when(versionRepo.findById(id)).thenReturn(Optional.of(versionTest));
        when(versionRepo.save(any(VersionEntity.class))).thenReturn(versionTest);
        VersionDTOResponse result = versionService.updateVersion(versionDTO,id);
        assertNotNull(result);
        assertEquals("b13",result.version());
        verify(versionRepo).save(any(VersionEntity.class));
    }
    @Test
    void updateVersionConflictTest(){
        Long id = 1L;
        VersionDTO versionDTO = new VersionDTO("b13");
        when(versionRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            versionService.updateVersion(versionDTO,id);
        });
    }
    @Test
    void deleteVersionTest(){
        Long id = 1L;

        when(versionRepo.existsById(id)).thenReturn(true);

        versionService.deleteVersion(id);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(versionRepo).deleteById(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(id, argumentCaptor.getValue());
        verify(versionRepo,times(1)).deleteById(id);
    }
    @Test
    void deleteFuelTypeNotContentTest(){
        Long id = 1L;

        when(versionRepo.existsById(id)).thenReturn(false);

        assertThrows(ResponseStatusException.class,()->{
            versionService.deleteVersion(id);
        });
    }
    @Test
    void deleteFuelTypeErrorTest(){
        Long id = 1L;

        when(versionRepo.existsById(id)).thenReturn(true);

        doThrow(new RuntimeException("Error")).when(versionRepo).deleteById(id);

        assertThrows(ResponseStatusException.class,()->{
            versionService.deleteVersion(id);
        });
    }

}
