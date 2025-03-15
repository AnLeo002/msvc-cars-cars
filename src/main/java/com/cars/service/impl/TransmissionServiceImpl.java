package com.cars.service.impl;

import com.cars.controller.dto.TransmissionDTO;
import com.cars.controller.dto.TransmissionDTOResponse;
import com.cars.persistence.TransmissionEntity;
import com.cars.repo.TransmissionRepo;
import com.cars.service.ITransmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class TransmissionServiceImpl implements ITransmissionService {
    private final TransmissionRepo repo;

    public TransmissionServiceImpl(TransmissionRepo repo) {
        this.repo = repo;
    }

    @Override
    public TransmissionDTOResponse findByTransmission(String transmission) {
        return repo.findByTransmissionIgnoreCase(transmission)
                .map(trans -> new TransmissionDTOResponse(trans.getId(), trans.getTransmission(),trans.getSpeeds()))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encuentra esta transmisión en la base de datos"));
    }

    @Override
    public List<TransmissionDTOResponse> findAll() {
        return repo.findAll().stream()
                .map(trans -> new TransmissionDTOResponse(trans.getId(), trans.getTransmission(),trans.getSpeeds()))
                .toList();    }

    @Override
    public TransmissionDTOResponse createTransmission(TransmissionDTO transmissionDTO) {
        if(repo.findByTransmissionIgnoreCaseAndSpeeds(transmissionDTO.transmission(), transmissionDTO.speeds()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta transmisión ya existe en la base de datos");
        }
        TransmissionEntity transmission = TransmissionEntity.builder()
                .transmission(transmissionDTO.transmission())
                .speeds(transmissionDTO.speeds())
                .build();
        TransmissionEntity transSave = repo.save(transmission);

        return new TransmissionDTOResponse(transSave.getId(), transSave.getTransmission(),transSave.getSpeeds());
    }

    @Override
    public TransmissionDTOResponse updateTransmission(TransmissionDTO transmissionDTO, Long id) {
        TransmissionEntity transmission = repo.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encuentra esta transmisión en la base de datos"));

        transmission.setTransmission(transmissionDTO.transmission());
        transmission.setSpeeds(transmissionDTO.speeds());

        TransmissionEntity transmissionSave = repo.save(transmission);
        return new TransmissionDTOResponse(transmissionSave.getId(),transmissionSave.getTransmission(), transmissionSave.getSpeeds());
    }

    @Override
    public void deleteTransmission(Long id) {
        try {
            repo.deleteById(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar esta transmisión  porque está en uso.");
        }

    }
}
