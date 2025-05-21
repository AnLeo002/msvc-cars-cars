package com.cars.service.impl;

import com.cars.controller.dto.VersionDTO;
import com.cars.controller.dto.VersionDTOResponse;
import com.cars.persistence.VersionEntity;
import com.cars.repo.VersionRepo;
import com.cars.service.IVersionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VersionServiceImpl implements IVersionService {
    private final VersionRepo repo;
    public VersionServiceImpl(VersionRepo repo) {
        this.repo = repo;
    }

    @Override
    public VersionDTOResponse findById(Long id) {
        return repo.findById(id)
                .map(version -> new VersionDTOResponse(version.getId(), version.getVersion()))
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encuentra esta transmisión en la base de datos"));
    }

    @Override
    public List<VersionDTOResponse> findAll() {
        return repo.findAll().stream().map(version -> new VersionDTOResponse(version.getId(), version.getVersion()))
                .toList();
    }

    @Override
    public VersionDTOResponse findByVersion(String version) {
        return repo.findByVersionIgnoreCase(version)
                .map(versionEntity -> new VersionDTOResponse(versionEntity.getId(), versionEntity.getVersion()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encuentra esta transmisión en la base de datos"));
    }

    @Override
    public VersionDTOResponse createVersion(VersionDTO versionDTO) {

        if(repo.findByVersionIgnoreCase(versionDTO.version()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Esta versión ya se encuentra en la base de datos");
        }
        VersionEntity version = VersionEntity.builder()
                .version(versionDTO.version())
                .build();
        VersionEntity versionSave = repo.save(version);
        return new VersionDTOResponse(versionSave.getId(), versionSave.getVersion());
    }

    @Override
    public VersionDTOResponse updateVersion(VersionDTO versionDTO, Long id) {
        VersionEntity version = repo.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Esta versión no se encuentra en la base de datos"));
        version.setVersion(versionDTO.version());

        VersionEntity versionUpdate = repo.save(version);
        return new VersionDTOResponse(versionUpdate.getId(), versionUpdate.getVersion());
    }

    @Override
    public void deleteVersion(Long id) {
        if(!repo.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"LA VERSION NO SE ENCUENTRA EN LA BASE DE DATOS");
        }
        try{
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar esta versión porque está en uso.");
        }

    }
}
