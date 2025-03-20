package com.cars.service;

import com.cars.controller.dto.ModelDTO;
import com.cars.controller.dto.ModelDTOResponse;

import java.util.List;

public interface IModelService {
    ModelDTOResponse findById(Long id);
    List<ModelDTOResponse> findAll();
    ModelDTOResponse findByModel(String model);
    ModelDTOResponse createModel(ModelDTO modelDTO);
    ModelDTOResponse updateModel(ModelDTO modelDTO, Long id);
    void deleteModel(Long id);
}
