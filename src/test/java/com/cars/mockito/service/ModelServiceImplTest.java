package com.cars.mockito.service;

import com.cars.controller.dto.ModelDTO;
import com.cars.controller.dto.ModelDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.BrandEntity;
import com.cars.persistence.ModelEntity;
import com.cars.persistence.VersionEntity;
import com.cars.repo.ModelRepo;
import com.cars.service.impl.EntityValidatorService;
import com.cars.service.impl.ModelServiceImpl;
import com.cars.service.record.ModelValidateComponents;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ModelServiceImplTest {
    @Mock
    private ModelRepo modelRepo;
    @Mock
    private EntityValidatorService validatorService;
    @InjectMocks
    private ModelServiceImpl modelService;
    private ModelEntity modelTest;
    private BrandEntity brandTest;
    private VersionEntity versionTest;
    @BeforeEach
    void setup(){
        modelTest = DataProvider.createTestModel();
        brandTest = DataProvider.createTestBrand();
        versionTest = DataProvider.createTestVersion();
    }
    @Test
    void findByIdTest(){
        Long id = 1L;
        when(modelRepo.findById(id)).thenReturn(Optional.of(modelTest));

        ModelDTOResponse result = modelService.findById(id);
        assertNotNull(result);
        assertEquals("sentra",result.model());
        verify(modelRepo).findById(id);
    }
    @Test
    void findByIdNotFoundTest(){
        Long id = 1L;
        when(modelRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,()->{
            modelService.findById(id);
        });
    }
    @Test
    void findAll(){
        ModelEntity model2 = ModelEntity.builder().id(2L).model("versa").brand(brandTest).versionEntities(Set.of(versionTest)).build();
        List<ModelEntity> list = List.of(modelTest,model2);
        when(modelRepo.findAll()).thenReturn(list);

        List<ModelDTOResponse> result = modelService.findAll();
        assertNotNull(result);
        assertEquals("sentra",result.get(0).model());
        verify(modelRepo,times(1)).findAll();
    }
    @Test
    void findByModelTest(){
        String model = "sentra";
        when(modelRepo.findByModelIgnoreCase(model)).thenReturn(Optional.of(modelTest));

        ModelDTOResponse result = modelService.findByModel(model);
        assertNotNull(result);
        assertEquals("sentra",result.model());
        verify(modelRepo).findByModelIgnoreCase(model);
    }
    @Test
    void findByModelNotFoundTest(){
        String model = "sentra";
        when(modelRepo.findByModelIgnoreCase(model)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,()->{
            modelService.findByModel(model);
        });
    }
    @Test
    void createModelTest(){
        ModelDTO modelDTO = new ModelDTO("sentra","nissan",List.of("B13"));
        ModelValidateComponents modelValidate = new ModelValidateComponents(
                DataProvider.createTestBrand(),
                Set.of(DataProvider.createTestVersion()));
        when(validatorService.verifiedModelDTO(modelDTO))
                .thenReturn(modelValidate);
        when(modelRepo.findByModelIgnoreCase(modelDTO.model())).thenReturn(Optional.empty());
        when(modelRepo.save(any(ModelEntity.class))).thenReturn(modelTest);

        ModelDTOResponse result = modelService.createModel(modelDTO);
        assertNotNull(result);
        assertEquals("sentra",result.model());
        verify(modelRepo,times(1)).save(any(ModelEntity.class));
    }
    @Test
    void createModelConflictTest(){
        ModelDTO modelDTO = new ModelDTO("sentra","nissan",List.of("B13"));
        ModelValidateComponents modelValidate = new ModelValidateComponents(
                DataProvider.createTestBrand(),
                Set.of(DataProvider.createTestVersion()));
        when(validatorService.verifiedModelDTO(modelDTO))
                .thenReturn(modelValidate);
        when(modelRepo.findByModelIgnoreCase(modelDTO.model())).thenReturn(Optional.of(modelTest));
        assertThrows(ResponseStatusException.class,()->{
            modelService.createModel(modelDTO);
        });
    }
    @Test
    void updateModelTest(){
        Long id = 1L;
        ModelDTO modelDTO = new ModelDTO("sentra","nissan",List.of("gts"));
        VersionEntity versionUpdate = DataProvider.createTestVersion();
        versionUpdate.setVersion("gts");
        ModelEntity modelUpdate = ModelEntity.builder().id(id).brand(brandTest).model("sentra").versionEntities(Set.of(versionUpdate)).build();
        ModelValidateComponents modelValidate = new ModelValidateComponents(
                DataProvider.createTestBrand(),
                Set.of(DataProvider.createTestVersion()));

        when(validatorService.verifiedModelDTO(modelDTO))
                .thenReturn(modelValidate);
        when(modelRepo.findById(id)).thenReturn(Optional.of(modelTest));
        when(modelRepo.save(any(ModelEntity.class))).thenReturn(modelUpdate);

        ModelDTOResponse result = modelService.updateModel(modelDTO,id);
        assertNotNull(result);
        assertEquals("gts",result.versions().get(0));
        verify(modelRepo,times(1)).save(any(ModelEntity.class));
    }
    @Test
    void updateModelConflictTest(){
        Long id = 1L;
        ModelDTO modelDTO = new ModelDTO("sentra","nissan",List.of("B13"));
        ModelValidateComponents modelValidate = new ModelValidateComponents(
                DataProvider.createTestBrand(),
                Set.of(DataProvider.createTestVersion()));
        when(validatorService.verifiedModelDTO(modelDTO))
                .thenReturn(modelValidate);
        when(modelRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            modelService.updateModel(modelDTO,id);
        });
    }
    @Test
    void deleteModelTest(){
        Long id = 1L;
        when(modelRepo.existsById(id)).thenReturn(true);
        modelService.deleteModel(id);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(modelRepo).deleteById(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(id,argumentCaptor.getValue());
    }
    @Test
    void deleteModelNotFoundTest(){
        Long id = 1L;
        when(modelRepo.existsById(id)).thenReturn(false);
        assertThrows(ResponseStatusException.class,()->{
            modelService.deleteModel(id);
        });
    }
    @Test
    void deleteTypeCarErrorTest(){
        Long id = 1L;
        when(modelRepo.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException("Error")).when(modelRepo).deleteById(id);
        assertThrows(ResponseStatusException.class,()->{
            modelService.deleteModel(id);
        });
    }
}
