package com.cars.controller;

import com.cars.controller.dto.TransmissionDTO;
import com.cars.controller.dto.TransmissionDTOResponse;
import com.cars.service.ITransmissionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trans")
public class TransmissionController {
    private final ITransmissionService service;

    public TransmissionController(ITransmissionService service) {
        this.service = service;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TransmissionDTOResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/find")
    public ResponseEntity<TransmissionDTOResponse> findByName(@RequestBody TransmissionDTO transmissionDTO){
        return ResponseEntity.ok(service.findByTransmissionAndSpeeds(transmissionDTO));
    }
    @PostMapping("/create")
    public ResponseEntity<TransmissionDTOResponse> createTransmission(@RequestBody TransmissionDTO transmissionDTO){
        return new ResponseEntity<>(service.createTransmission(transmissionDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<TransmissionDTOResponse> updateTransmission(@RequestBody TransmissionDTO transmissionDTO, @PathVariable Long id){
        return ResponseEntity.ok(service.updateTransmission(transmissionDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransmission(@PathVariable Long id){
        service.deleteTransmission(id);
        return new ResponseEntity<>("La transmisión fue eliminada correctamente",HttpStatus.NO_CONTENT);

    }
}
