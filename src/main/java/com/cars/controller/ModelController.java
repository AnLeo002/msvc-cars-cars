package com.cars.controller;

import com.cars.controller.dto.ModelDTO;
import com.cars.controller.dto.ModelDTOResponse;
import com.cars.service.IModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/model")
public class ModelController {
    private final IModelService service;

    public ModelController(IModelService service) {
        this.service = service;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ModelDTOResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<ModelDTOResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @GetMapping("/{version}")
    public ResponseEntity<ModelDTOResponse> findByVersion(@PathVariable String model){
        return ResponseEntity.ok(service.findByModel(model));
    }
    @PostMapping("/create")
    public ResponseEntity<ModelDTOResponse> createModel(@RequestBody ModelDTO modelDTO){
        return new ResponseEntity<>(service.createModel(modelDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ModelDTOResponse> updateModel(@RequestBody ModelDTO modelDTO, @PathVariable Long id){
        return ResponseEntity.ok(service.updateModel(modelDTO,id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable Long id){
        service.deleteModel(id);
        return new ResponseEntity<>("El modelo fue eliminada correctamente",HttpStatus.NO_CONTENT);

    }
}
