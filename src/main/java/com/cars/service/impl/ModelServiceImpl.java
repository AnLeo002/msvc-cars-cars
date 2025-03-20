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

    public ModelServiceImpl(ModelRepo repo, BrandRepo brandRepo, VersionRepo versionRepo) {
        this.repo = repo;
        this.brandRepo = brandRepo;
        this.versionRepo = versionRepo;
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
        BrandEntity brand = brandRepo.findByBrandIgnoreCase(modelDTO.brand())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La marca no se encuentra en la base de datos"));
        Set<VersionEntity> versions = versionRepo.findByVersionIn(modelDTO.versions()).stream().collect(Collectors.toSet());
        if(repo.findByModelIgnoreCase(modelDTO.model()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"El modelo ya se encuentra en la base de datos");
        }
        ModelEntity model = ModelEntity.builder()
                .model(modelDTO.model())
                .brand(brand)
                .versionEntities(versions)
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
        BrandEntity brand =brandRepo.findByBrandIgnoreCase(modelDTO.brand())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"La marca no se encuentra en la base de datos"));
        Set<VersionEntity> versions = versionRepo.findByVersionIn(modelDTO.versions()).stream().collect(Collectors.toSet());

        ModelEntity model = repo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"El modelo no se encuentra en la base de datos"));

        model.setModel(modelDTO.model());
        model.setBrand(brand);
        model.setVersionEntities(versions);

        ModelEntity modelUpdate = repo.save(model);
        return new ModelDTOResponse(
                modelUpdate.getId(),
                modelUpdate.getModel(),
                modelUpdate.getBrand().getBrand(),
                modelUpdate.getVersionEntities().stream().map(VersionEntity::getVersion).toList());
    }

    @Override
    public void deleteModel(Long id) {
        try{
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"El modelo no puede ser eliminado por que se encuentra en uso");
        }

    }
}
