package com.cars.service;

import com.cars.controller.dto.TransmissionDTO;
import com.cars.controller.dto.TransmissionDTOResponse;

import java.util.List;

public interface ITransmissionService {
    TransmissionDTOResponse findByTransmission(String transmission);
    List<TransmissionDTOResponse> findAll();
    TransmissionDTOResponse createTransmission(TransmissionDTO transmissionDTO);
    TransmissionDTOResponse updateTransmission(TransmissionDTO transmissionDTO,Long id);
    void deleteTransmission(Long id);
}
