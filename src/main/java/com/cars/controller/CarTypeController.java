package com.cars.controller;

import com.cars.controller.dto.CarTypeDTO;
import com.cars.controller.dto.CarTypeDTOResponse;
import com.cars.service.ICarTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class CarTypeController {
    private final ICarTypeService service;

    public CarTypeController(ICarTypeService service) {
        this.service = service;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<CarTypeDTOResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<CarTypeDTOResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping("/create")
    public ResponseEntity<CarTypeDTOResponse> createCarType(@RequestBody @Valid CarTypeDTO carTypeDTO){
        return new ResponseEntity<>(service.createType(carTypeDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<CarTypeDTOResponse> updateCarType(@RequestBody @Valid CarTypeDTO carTypeDTO, @PathVariable Long id){
        return ResponseEntity.ok(service.updateTypeCar(carTypeDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCarType(@PathVariable Long id){
        service.deleteTypeCar(id);
        return new ResponseEntity<>("El tipo de vehiculo fue eliminado correctamente",HttpStatus.NO_CONTENT);

    }
}
