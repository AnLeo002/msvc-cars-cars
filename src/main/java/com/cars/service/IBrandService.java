package com.cars.service;

import com.cars.controller.dto.BrandDTO;
import com.cars.controller.dto.BrandDTOResponse;

import java.util.List;

public interface IBrandService {
    BrandDTOResponse findById(Long id);
    List<BrandDTOResponse> findAll();
    BrandDTOResponse findByBrand(String brand);
    BrandDTOResponse createBrand(BrandDTO brandDTO);
    BrandDTOResponse updateBrand(BrandDTO brandDTO, Long id);
    void deleteBrand(Long id);
}
