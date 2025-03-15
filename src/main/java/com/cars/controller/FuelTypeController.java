package com.cars.controller;

import com.cars.controller.dto.FuelTypeDTO;
import com.cars.controller.dto.FuelTypeDTOResponse;
import com.cars.service.IFuelTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuel")
public class FuelTypeController {
    private final IFuelTypeService service;

    public FuelTypeController(IFuelTypeService service) {
        this.service = service;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<FuelTypeDTOResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{fuel}")
    public ResponseEntity<FuelTypeDTOResponse> findById(@PathVariable String fuel){
        return ResponseEntity.ok(service.findByFuel(fuel));
    }
    @PostMapping("/create")
    public ResponseEntity<FuelTypeDTOResponse> createFuel(@RequestBody FuelTypeDTO fuelTypeDTO){
        return new ResponseEntity<>(service.createFuelType(fuelTypeDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<FuelTypeDTOResponse> updateFuel(@RequestBody FuelTypeDTO fuelTypeDTO, @PathVariable Long id){
        return ResponseEntity.ok(service.updateFuelType(fuelTypeDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFuel(@PathVariable Long id){
        service.deleteFuelType(id);
        return new ResponseEntity<>("El tipo de combustible fue eliminado correctamente",HttpStatus.NO_CONTENT);

    }
}
