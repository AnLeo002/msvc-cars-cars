package com.cars.mockito.service;

import com.cars.controller.dto.BrandDTO;
import com.cars.controller.dto.BrandDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.BrandEntity;
import com.cars.repo.BrandRepo;
import com.cars.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {
    @Mock
    private BrandRepo brandRepo;
    @InjectMocks
    private BrandServiceImpl brandService;
    BrandEntity brandTest;
    @BeforeEach
    void setup(){
        brandTest = DataProvider.createTestBrand();
    }
    @Test
    void findByIdTest(){
        Long id= 1L;
        when(brandRepo.findById(id)).thenReturn(Optional.of(brandTest));
        BrandDTOResponse result = brandService.findById(id);

        assertNotNull(result);
        assertEquals("nissan",result.brand());
        verify(brandRepo, times(1)).findById(id);
    }
    @Test
    void findByIdNotFoundTest(){
        Long id= 1L;
        when(brandRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,()->{
            brandService.findById(id);
        });
    }
    @Test
    void findAllTest(){
        BrandEntity brand2 = BrandEntity.builder().id(2L).brand("ferrari").build();
        List<BrandEntity> list = List.of(brandTest,brand2);
        when(brandRepo.findAll()).thenReturn(list);

        List<BrandDTOResponse> result = brandService.findAll();
        assertNotNull(result);
        assertEquals("nissan",result.get(0).brand());
        verify(brandRepo, times(1)).findAll();
    }
    @Test
    void findByBrandTest(){
        String brand= "nissan";
        when(brandRepo.findByBrand(brand)).thenReturn(Optional.of(brandTest));
        BrandDTOResponse result = brandService.findByBrand(brand);

        assertNotNull(result);
        assertEquals("nissan",result.brand());
        verify(brandRepo, times(1)).findByBrand(brand);
    }
    @Test
    void findByBrandNotFoundTest(){
        String brand= "nissan";
        when(brandRepo.findByBrand(brand)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,()->{
            brandService.findByBrand(brand);
        });
    }
    @Test
    void createBrandTest(){
        BrandDTO brandDTO = new BrandDTO("nissan");
        when(brandRepo.findByBrandIgnoreCase(brandDTO.brand()))
                .thenReturn(Optional.empty());
        when(brandRepo.save(any(BrandEntity.class)))
                .thenReturn(brandTest);

        BrandDTOResponse result = brandService.createBrand(brandDTO);
        assertNotNull(result);
        assertEquals("nissan",result.brand());
        verify(brandRepo,times(1)).findByBrandIgnoreCase(brandDTO.brand());
    }
    @Test
    void createBrandConflictTest(){
        BrandDTO brandDTO = new BrandDTO("nissan");
        when(brandRepo.findByBrandIgnoreCase(brandDTO.brand()))
                .thenReturn(Optional.of(brandTest));
        assertThrows(ResponseStatusException.class,()->{
            brandService.createBrand(brandDTO);
        });
    }
    @Test
    void updateBrandTest(){
        BrandDTO brandDTO = new BrandDTO("honda");
        Long id = 1L;
        BrandEntity updatedBrand = BrandEntity.builder().id(id).brand(brandDTO.brand()).build();
        when(brandRepo.findById(id))
                .thenReturn(Optional.of(brandTest));
        when(brandRepo.save(any(BrandEntity.class)))
                .thenReturn(updatedBrand);

        BrandDTOResponse result = brandService.updateBrand(brandDTO,id);
        assertNotNull(result);
        assertEquals("honda",result.brand());
        verify(brandRepo,times(1)).findById(id);
    }
    @Test
    void updateBrandConflictTest(){
        BrandDTO brandDTO = new BrandDTO("honda");
        Long id = 1L;
        when(brandRepo.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,()->{
            brandService.updateBrand(brandDTO,id);
        });
    }
    @Test
    void deleteBrandTest(){
        Long id = 1L;
        when(brandRepo.existsById(id)).thenReturn(true);

        brandService.deleteBrand(id);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(brandRepo).deleteById(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(id,argumentCaptor.getValue());
        verify(brandRepo,times(1)).deleteById(id);
    }
    @Test
    void deleteBrandNotFoundTest(){
        Long id = 1L;
        when(brandRepo.existsById(id)).thenReturn(false);
        assertThrows(ResponseStatusException.class,()->{
            brandService.deleteBrand(id);
        });
    }
    @Test
    void deleteBrandErrorTest(){
        Long id = 1L;
        when(brandRepo.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException("Error")).when(brandRepo).deleteById(id);
        assertThrows(ResponseStatusException.class,()->{
            brandService.deleteBrand(id);
        });
    }

}
