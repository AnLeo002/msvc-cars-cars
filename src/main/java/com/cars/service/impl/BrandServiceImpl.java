package com.cars.service.impl;

import com.cars.controller.dto.BrandDTO;
import com.cars.controller.dto.BrandDTOResponse;
import com.cars.persistence.BrandEntity;
import com.cars.repo.BrandRepo;
import com.cars.service.IBrandService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BrandServiceImpl implements IBrandService {
    private final BrandRepo repo;

    public BrandServiceImpl(BrandRepo repo) {
        this.repo = repo;
    }

    @Override
    public BrandDTOResponse findById(Long id) {
        return repo.findById(id)
                .map(brand -> new BrandDTOResponse(brand.getId(), brand.getBrand()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La marca con id :"+id +" no se encuentra en la base de datos"));

    }

    @Override
    public List<BrandDTOResponse> findAll() {
        return repo.findAll()
                .stream().map(brand -> new BrandDTOResponse(brand.getId(), brand.getBrand()))
                .toList();
    }

    @Override
    public BrandDTOResponse findByBrand(String brand) {
        return repo.findByBrand(brand)
                .map(brandEntity -> new BrandDTOResponse(brandEntity.getId(), brandEntity.getBrand()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Marca :"+ brand +" no se encuentra en la base de datos"));
    }

    @Override
    public BrandDTOResponse createBrand(BrandDTO brandDTO) {
        if(repo.findByBrandIgnoreCase(brandDTO.brand()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, brandDTO.brand() +"Ya se encuentra en la base de datos");
        }
        BrandEntity brand = BrandEntity.builder()
                .brand(brandDTO.brand())
                .build();
        BrandEntity brandSave = repo.save(brand);
        return new BrandDTOResponse(brandSave.getId(),brandSave.getBrand());
    }

    @Override
    public BrandDTOResponse updateBrand(BrandDTO brandDTO, Long id) {

        BrandEntity brand = repo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Marca con id :"+id +" no se encuentra en la base de datos"));
        brand.setBrand(brandDTO.brand());
        repo.save(brand);
        return new BrandDTOResponse(brand.getId(),brand.getBrand());
    }

    @Override
    public void deleteBrand(Long id) {
        try {
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar la marca porque está en uso.");
        }

    }
}
