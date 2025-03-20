package com.cars.service;

import com.cars.controller.dto.VersionDTO;
import com.cars.controller.dto.VersionDTOResponse;

import java.util.List;

public interface IVersionService {
    VersionDTOResponse findById(Long id);
    List<VersionDTOResponse> findAll();
    VersionDTOResponse findByVersion(String version);
    VersionDTOResponse createVersion(VersionDTO versionDTO);
    VersionDTOResponse updateVersion(VersionDTO versionDTO, Long id);
    void deleteVersion(Long id);
}
