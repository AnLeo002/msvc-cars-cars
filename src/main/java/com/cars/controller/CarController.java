package com.cars.controller;

import com.cars.controller.dto.CarDTO;
import com.cars.controller.dto.CarDTOResponse;
import com.cars.service.ICarService;
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
    @GetMapping("/find/{id}")
    public ResponseEntity<CarDTOResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping("/create")
    public ResponseEntity<CarDTOResponse> createCar(@RequestBody CarDTO carDTO){
        return new ResponseEntity<>(service.createCar(carDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<CarDTOResponse> updateCar(@RequestBody CarDTO carDTO, @PathVariable Long id){
        return ResponseEntity.ok(service.updateCar(carDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id){
        service.deleteCar(id);
        return new ResponseEntity<>("El vehiculo fue eliminado correctamente",HttpStatus.NO_CONTENT);

    }
}
