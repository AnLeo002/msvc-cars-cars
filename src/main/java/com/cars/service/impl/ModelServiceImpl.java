package com.cars.service.impl;

import com.cars.controller.dto.ModelDTO;
import com.cars.controller.dto.ModelDTOResponse;
import com.cars.persistence.BrandEntity;
import com.cars.persistence.ModelEntity;
import com.cars.persistence.VersionEntity;
import com.cars.repo.BrandRepo;
import com.cars.repo.ModelRepo;
import com.cars.repo.VersionRepo;
import com.cars.service.IModelService;
import com.cars.service.record.ModelValidateComponents;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModelServiceImpl implements IModelService {
    private final ModelRepo repo;
    private final BrandRepo brandRepo;
    private final VersionRepo versionRepo;
    private final EntityValidatorService validatorService;

    public ModelServiceImpl(ModelRepo repo, BrandRepo brandRepo, VersionRepo versionRepo, EntityValidatorService validatorService) {
        this.repo = repo;
        this.brandRepo = brandRepo;
        this.versionRepo = versionRepo;
        this.validatorService = validatorService;
    }

    @Override
    public ModelDTOResponse findById(Long id) {
        return repo.findById(id).map(model -> new ModelDTOResponse(model.getId(), model.getModel(),model.getBrand().getBrand(),model.getVersionEntities().stream()
                .map(VersionEntity::getVersion)
                .toList()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo no se encuentra en la base de datos"));
    }

    @Override
    public List<ModelDTOResponse> findAll() {
        return repo.findAll().stream().map(model -> new ModelDTOResponse(
                        model.getId(),
                        model.getModel(),
                        model.getBrand().getBrand(),
                        model.getVersionEntities().stream()
                                        .map(VersionEntity::getVersion)
                                        .toList()))
                .toList();
    }

    @Override
    public ModelDTOResponse findByModel(String model) {
        return repo.findByModelIgnoreCase(model).map(modelEntity ->
                new ModelDTOResponse(
                        modelEntity.getId(),
                        modelEntity.getModel(),
                        modelEntity.getBrand().getBrand(),
                        modelEntity.getVersionEntities().stream()
                                .map(VersionEntity::getVersion)
                                .toList()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo no se encuentra en la base de datos"));
    }

    @Override
    public ModelDTOResponse createModel(ModelDTO modelDTO) {
        ModelValidateComponents modelValidate = validatorService.verifiedModelDTO(modelDTO);
        if(repo.findByModelIgnoreCase(modelDTO.model()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"El modelo ya se encuentra en la base de datos");
        }
        ModelEntity model = ModelEntity.builder()
                .model(modelDTO.model())
                .brand(modelValidate.brand())
                .versionEntities(modelValidate.versions())
                .build();
        ModelEntity modelSave = repo.save(model);
        return new ModelDTOResponse(
                modelSave.getId(),
                modelSave.getModel(),
                modelSave.getBrand().getBrand(),
                modelSave.getVersionEntities().stream().map(VersionEntity::getVersion).toList());
    }

    @Override
    public ModelDTOResponse updateModel(ModelDTO modelDTO, Long id) {
        ModelValidateComponents modelValidate = validatorService.verifiedModelDTO(modelDTO);
        ModelEntity model = repo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo no se encuentra en la base de datos"));

        model.setModel(modelDTO.model());
        model.setBrand(modelValidate.brand());
        model.setVersionEntities(modelValidate.versions());

        ModelEntity modelUpdate = repo.save(model);
        return new ModelDTOResponse(
                modelUpdate.getId(),
                modelUpdate.getModel(),
                modelUpdate.getBrand().getBrand(),
                modelUpdate.getVersionEntities().stream().map(VersionEntity::getVersion).toList());
    }

    @Override
    public void deleteModel(Long id) {
        if (!repo.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo no esta presente en la base de datos");
        }
        try{
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"El modelo no puede ser eliminado por que se encuentra en uso");
        }

    }
}
