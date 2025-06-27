package com.cars.controller;

import com.cars.controller.dto.BrandDTO;
import com.cars.controller.dto.BrandDTOResponse;
import com.cars.service.IBrandService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    private final IBrandService service;

    public BrandController(IBrandService service) {
        this.service = service;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<BrandDTOResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<BrandDTOResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping("/create")
    public ResponseEntity<BrandDTOResponse> createBrand(@RequestBody @Valid BrandDTO brandDTO){
        return new ResponseEntity<>(service.createBrand(brandDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<BrandDTOResponse> updateBrand(@RequestBody @Valid BrandDTO brandDTO, @PathVariable Long id){
        return ResponseEntity.ok(service.updateBrand(brandDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long id){
        service.deleteBrand(id);
        return new ResponseEntity<>("La marca de vehiculo fue eliminada correctamente",HttpStatus.NO_CONTENT);

    }
}
