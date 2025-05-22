package com.cars.mockito.service;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.ModelDTO;
import com.cars.mockito.DataProvider;
import com.cars.persistence.*;
import com.cars.repo.*;
import com.cars.service.impl.EntityValidatorService;
import com.cars.service.record.CarValidateComponents;
import com.cars.service.record.ModelValidateComponents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EntityValidatorServiceTest {
    @Mock
    private TransmissionRepo transmissionRepo;
    @Mock
    private FuelTypeRepo fuelTypeRepo;
    @Mock
    private CarTypeRepo carTypeRepo;
    @Mock
    private ModelRepo modelRepo;
    @Mock
    private VersionRepo versionRepo;
    @Mock
    private BrandRepo brandRepo;
    @InjectMocks
    private EntityValidatorService carValidationService;
    private CarDTO carDTO;
    @BeforeEach
    void setUp(){
        carDTO = DataProvider.createTestCarDTO("BDP018","gris");
    }
    @Test
    void testVerifiedCarDTO(){
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.of(DataProvider.createTestTrans()));
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type())).thenReturn(
                Optional.of(DataProvider.createTestCarType()));
        when(modelRepo.findByModelIgnoreCase(carDTO.model())).thenReturn(
                Optional.of(DataProvider.createTestModel()));
        //Then
        CarValidateComponents result = carValidationService.verifiedCarDTO(carDTO);
        assertNotNull(result);
        assertEquals("corriente", result.fuelType().getFuel());
        assertEquals("sentra", result.model().getModel());
        assertEquals("b13", result.version().getVersion());
        assertEquals("automatica", result.transmission().getTransmission());
    }
    @Test
    void validateFuelTest(){
        //When
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.of(DataProvider.createTestFuel()));
        FuelTypeEntity result = carValidationService.validateFuel(carDTO.fuel());
        assertNotNull(result);
        assertEquals("corriente",result.getFuel());
    }

    @Test
    void testCreateCarFuelNoExist(){
        //When
        when(fuelTypeRepo.findByFuelIgnoreCase(carDTO.fuel()))
                .thenReturn(Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateFuel(carDTO.fuel());
        });
    }
    @Test
    void validateTransmissionTest(){
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(carDTO.transmission().transmission(),carDTO.transmission().speeds()))
                .thenReturn(Optional.of(DataProvider.createTestTrans()));
        TransmissionEntity result = carValidationService.validateTransmission(carDTO.transmission().transmission(),carDTO.transmission().speeds());
        assertNotNull(result);
        assertEquals("automatica",result.getTransmission());
        verify(transmissionRepo, times(1)).findByTransmissionIgnoreCaseAndSpeeds(carDTO.transmission().transmission(),carDTO.transmission().speeds());

    }
    @Test
    void testCreateCarTransNoExist(){
        //When
        when(transmissionRepo.findByTransmissionIgnoreCaseAndSpeeds(
                carDTO.transmission().transmission(),
                carDTO.transmission().speeds()))
                .thenReturn(Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateTransmission(carDTO.transmission().transmission(),carDTO.transmission().speeds());
        });
    }
    @Test
    void validateCarTypeTest() {
        //When
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type()))
                .thenReturn(Optional.of(DataProvider.createTestCarType()));
        CarTypeEntity result = carValidationService.validateCarType(carDTO.type());
        assertNotNull(result);
        assertEquals("sedan", result.getType());
    }
    @Test
    void validateCarTypeNoExistTest() {
        //When
        when(carTypeRepo.findByTypeIgnoreCase(carDTO.type())).thenReturn(
                Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateCarType(carDTO.type());
        });
    }
    @Test
    void validateModelTest(){
        //When
        when(modelRepo.findByModelIgnoreCase(carDTO.model()))
                .thenReturn(Optional.of(DataProvider.createTestModel()));
        ModelEntity result = carValidationService.validateModel(carDTO.model());
        assertNotNull(result);
        assertEquals("sentra",result.getModel());
    }
    @Test
    void testModelNoExist(){
        //When
        when(modelRepo.findByModelIgnoreCase(carDTO.model())).thenReturn(
                Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateModel(carDTO.model());
        });
    }
    @Test
    void validateVersionTest(){
        when(modelRepo.findByModelIgnoreCase(carDTO.model()))
                .thenReturn(Optional.of(DataProvider.createTestModel()));
        VersionEntity result = carValidationService.validateVersion(carDTO.version(),carDTO.model());
        assertNotNull(result);
        assertEquals("b13",result.getVersion());
    }
    @Test
    void testVersionNoExist(){
        String versionDifferent = "gts";
        when(modelRepo.findByModelIgnoreCase(carDTO.model()))
                .thenReturn(Optional.of(DataProvider.createTestModel()));
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateVersion(versionDifferent,carDTO.model());
        });
    }
    @Test
    void validateAllVersionsTest(){
        VersionEntity version1 = DataProvider.createTestVersion();
        VersionEntity version2 = VersionEntity.builder().id(2L).version("gts").build();
        List<String> list = List.of("b13","gts");
        List<VersionEntity> versionEntityList = List.of(version1,version2);
        //When
        when(versionRepo.findByVersionIn(list))
                .thenReturn(versionEntityList);
        Set<VersionEntity> result = carValidationService.validateAllVersions(list);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(v -> v.getVersion().equalsIgnoreCase("b13")));
        assertTrue(result.stream().anyMatch(v -> v.getVersion().equalsIgnoreCase("gts")));
    }
    @Test
    void BrandDifferentToDtoTest(){
        String brand = "ferrari";
        ModelEntity model = DataProvider.createTestModel();

        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateBrandMatch(brand,model);
        });
    }
    @Test
    void validateBrandMatchTest(){
        String brand = "nissan";
        ModelEntity model = DataProvider.createTestModel();

        carValidationService.validateBrandMatch(brand,model);

        assertDoesNotThrow(() -> carValidationService.validateBrandMatch(brand, model));
    }
    @Test
    void validateBrandTest() {
        //When
        when(brandRepo.findByBrandIgnoreCase(carDTO.brand()))
                .thenReturn(Optional.of(DataProvider.createTestBrand()));
        BrandEntity result = carValidationService.validateBrand(carDTO.brand());
        assertNotNull(result);
        assertEquals("nissan", result.getBrand());
    }
    @Test
    void validateBrandNoExistTest() {
        //When
        when(brandRepo.findByBrandIgnoreCase(carDTO.brand())).thenReturn(
                Optional.empty());
        //Given
        assertThrows(ResponseStatusException.class,()->{
            carValidationService.validateBrand(carDTO.brand());
        });
    }
    @Test
    void verifiedModelDTOTest(){
        VersionEntity version1 = DataProvider.createTestVersion();
        VersionEntity version2 = VersionEntity.builder().id(2L).version("gts").build();
        List<String> list = List.of("b13","gts");
        List<VersionEntity> versionEntityList = List.of(version1,version2);
        ModelDTO modelDTO = new ModelDTO("sentra","nissan",list);
        //When
        when(versionRepo.findByVersionIn(list))
                .thenReturn(versionEntityList);
        when(brandRepo.findByBrandIgnoreCase(modelDTO.brand()))
                .thenReturn(Optional.of(DataProvider.createTestBrand()));
        ModelValidateComponents result = carValidationService.verifiedModelDTO(modelDTO);
        assertNotNull(result);
        assertEquals("nissan", result.brand().getBrand());
        assertTrue(result.versions().stream().anyMatch(v -> v.getVersion().equalsIgnoreCase("b13")));
        assertTrue(result.versions().stream().anyMatch(v -> v.getVersion().equalsIgnoreCase("gts")));
    }
    @Test
    void verifiedModelDTOConflictTest(){
        // Given
        VersionEntity version1 = VersionEntity.builder().id(1L).version("b13").build();
        List<String> inputVersions = List.of("b13", "gts"); // "gts" no existe
        List<VersionEntity> foundVersions = List.of(version1); // Solo "b13" fue encontrado

        ModelDTO modelDTO = new ModelDTO("sentra", "nissan", inputVersions);

        when(brandRepo.findByBrandIgnoreCase("nissan"))
                .thenReturn(Optional.of(DataProvider.createTestBrand()));

        when(versionRepo.findByVersionIn(inputVersions))
                .thenReturn(foundVersions); // "gts" faltante

        assertThrows(ResponseStatusException.class,()->{
            carValidationService.verifiedModelDTO(modelDTO);
        });
    }
}
