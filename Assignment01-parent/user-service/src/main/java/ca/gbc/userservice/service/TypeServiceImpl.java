package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.TypeRequest;
import ca.gbc.userservice.dto.TypeResponse;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.Type;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.TypeRepository;
import ca.gbc.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TypeServiceImpl implements TypeService {
    final TypeRepository typeRepository;

    @Override
    public TypeResponse createType(TypeRequest typeRequest){

        Type type = Type.builder()
                .type_name(typeRequest.type_name())
                .build();

        //persist a product
        typeRepository.save(type);
        return new TypeResponse(type.getType_id(), type.getType_name());
    }

    @Override
    public List<TypeResponse> getAllTypes() {
        List<Type> types = typeRepository.findAll();
        // return products.stream().map(product -> mapToProductResponse(product)).toList();
        return types.stream().map(this::mapToTypeResponse).toList();
    }
    // convert type object to type response
    private TypeResponse mapToTypeResponse(Type type){
        return new TypeResponse(type.getType_id(), type.getType_name());
    }

    @Override
    public Long updateType(Long typeId, TypeRequest typeRequest) {

        // check to see if type exists with the typeId
        Type type = typeRepository.findById(typeId).orElse(null);

        // if type exists
        if(type != null){
            type.setType_name(typeRequest.type_name());
            return typeRepository.save(type).getType_id();
        }
        return typeId;
    }

    @Override
    public void deleteType(Long typeId) {
        typeRepository.deleteById(typeId);
    }
}
