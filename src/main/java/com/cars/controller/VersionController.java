package com.cars.controller;

import com.cars.controller.dto.VersionDTO;
import com.cars.controller.dto.VersionDTOResponse;
import com.cars.service.IVersionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/version")
public class VersionController {
    private final IVersionService service;

    public VersionController(IVersionService service) {
        this.service = service;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<VersionDTOResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<VersionDTOResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @GetMapping("/{version}")
    public ResponseEntity<VersionDTOResponse> findByVersion(@PathVariable String version){
        return ResponseEntity.ok(service.findByVersion(version));
    }
    @PostMapping("/create")
    public ResponseEntity<VersionDTOResponse> createVersion(@RequestBody @Valid VersionDTO versionDTO){
        return new ResponseEntity<>(service.createVersion(versionDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<VersionDTOResponse> updateVersion(@RequestBody @Valid VersionDTO versionDTO, @PathVariable Long id){
        return ResponseEntity.ok(service.updateVersion(versionDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVersion(@PathVariable Long id){
        service.deleteVersion(id);
        return new ResponseEntity<>("La version de vehiculo fue eliminada correctamente",HttpStatus.NO_CONTENT);

    }
}
