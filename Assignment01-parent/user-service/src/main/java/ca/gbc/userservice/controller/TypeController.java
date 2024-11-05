package ca.gbc.userservice.controller;

import ca.gbc.userservice.dto.TypeRequest;
import ca.gbc.userservice.dto.TypeResponse;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.Type;
import ca.gbc.userservice.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type")
@RequiredArgsConstructor
public class TypeController {

    final TypeService typeService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TypeResponse> createType(@RequestBody TypeRequest typeRequest){
        TypeResponse createdTypeResponse = typeService.createType(typeRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","api/type/"+createdTypeResponse.type_id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdTypeResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TypeResponse> getAllTypes(){
        return typeService.getAllTypes();
    }


    @PutMapping("/{typeId}")
    public ResponseEntity<?> updateProduct(@PathVariable("typeId") Long typeId,
                                           @RequestBody TypeRequest typeRequest){
        Long updatedTypeId = typeService.updateType(typeId, typeRequest);

        //sets the location header attribute
        HttpHeaders headers = new HttpHeaders();
        headers.add("location","/api/type/"+updatedTypeId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{typeId}")
    public ResponseEntity<?> deleteType(@PathVariable("typeId") Long typeId){
        typeService.deleteType(typeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
