package com.cars.mockito.controller;

import com.cars.controller.BrandController;
import com.cars.controller.dto.BrandDTO;
import com.cars.controller.dto.BrandDTOResponse;
import com.cars.mockito.DataProvider;
import com.cars.persistence.BrandEntity;
import com.cars.service.IBrandService;
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
class BrandControllerTest {
    @Mock
    private IBrandService brandService;
    @InjectMocks
    private BrandController brandController;
    BrandEntity brandTest;
    BrandDTOResponse brandDTOResponse;
    @BeforeEach
    void setup(){
        brandTest = DataProvider.createTestBrand();
        brandDTOResponse = new BrandDTOResponse(1L,"nissan");
    }

    @Test
    void findAllTest(){
        BrandDTOResponse brandDTOResponse2 = new BrandDTOResponse(2L,"ferrari");
        List<BrandDTOResponse> list = List.of(brandDTOResponse,brandDTOResponse2);
        when(brandService.findAll()).thenReturn(list);

        ResponseEntity<List<BrandDTOResponse>> result = brandController.findAll();
        assertNotNull(result);
        assertEquals("nissan",result.getBody().get(0).brand());
        assertEquals("ferrari",result.getBody().get(1).brand());
        verify(brandService, times(1)).findAll();
    }
    @Test
    void findByIdTest(){
        Long id= 1L;
        when(brandService.findById(id)).thenReturn(brandDTOResponse);
        ResponseEntity<BrandDTOResponse> result = brandController.findById(id);
        assertNotNull(result);
        assertEquals("nissan",result.getBody().brand());
        verify(brandService, times(1)).findById(id);
    }
    @Test
    void createBrandTest(){
        BrandDTO brandDTO = new BrandDTO("nissan");
        when(brandService.createBrand(brandDTO))
                .thenReturn(brandDTOResponse);

        ResponseEntity<BrandDTOResponse> result = brandController.createBrand(brandDTO);
        assertNotNull(result);
        assertEquals("nissan",result.getBody().brand());
        verify(brandService,times(1)).createBrand(brandDTO);
    }
    @Test
    void updateBrandTest(){
        BrandDTO brandDTO = new BrandDTO("honda");
        Long id = 1L;
        BrandDTOResponse updatedBrand = new BrandDTOResponse(id,brandDTO.brand());
        when(brandService.updateBrand(brandDTO,id))
                .thenReturn(updatedBrand);

        ResponseEntity<BrandDTOResponse> result = brandController.updateBrand(brandDTO,id);
        assertNotNull(result);
        assertEquals("honda",result.getBody().brand());
        verify(brandService,times(1)).updateBrand(brandDTO,id);
    }
    @Test
    void deleteBrandTest(){
        Long id = 1l;
        ResponseEntity<String> result = brandController.deleteBrand(id);
        assertNotNull(result);
        assertEquals(204,result.getStatusCode().value());
        verify(brandService,times(1)).deleteBrand(id);
    }
}
