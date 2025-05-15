package com.cars.controller;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.service.ICarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    private final ICarService service;

    public CarController(ICarService service) {
        this.service = service;
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<CarDTOResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/find/{plate}")
    public ResponseEntity<CarDTOResponse> findByPlate(@PathVariable String plate){
        return ResponseEntity.ok(service.findById(plate));
    }
    @PostMapping("/create")
    public ResponseEntity<CarDTOResponse> createCar(@RequestBody @Valid CarDTO carDTO){
        return new ResponseEntity<>(service.createCar(carDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<CarDTOResponse> updateCar(@RequestBody @Valid CarDTO carDTO){
        return ResponseEntity.ok(service.updateCar(carDTO));
    }
    @DeleteMapping("/delete/{plate}")
    public ResponseEntity<String> deleteCar(@PathVariable String plate){
        service.deleteCar(plate);
        return new ResponseEntity<>("El vehiculo fue eliminado correctamente",HttpStatus.NO_CONTENT);

    }
}
