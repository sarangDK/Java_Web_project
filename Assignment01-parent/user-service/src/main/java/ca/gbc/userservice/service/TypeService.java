package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.TypeRequest;
import ca.gbc.userservice.dto.TypeResponse;

import java.util.List;

public interface TypeService {
    TypeResponse createType(TypeRequest typeRequest);
    List<TypeResponse> getAllTypes();
    Long updateType(Long typeId, TypeRequest typeRequest);
    void deleteType(Long typeId);
}
