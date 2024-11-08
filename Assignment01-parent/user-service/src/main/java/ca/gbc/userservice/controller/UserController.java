package ca.gbc.userservice.controller;

import ca.gbc.userservice.dto.TypeResponse;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    final UserService userService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
        UserResponse createdUserResponse = userService.createUser(userRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","api/user/"+createdUserResponse.user_id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdUserResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable("userId") Long userId){
         return userService.getUserById(userId);
    }

    @GetMapping("/isStaff/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String isStaff(@PathVariable("userId") Long userId){
        return userService.getType(userId).type_name();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId,
                                           @RequestBody UserRequest userRequest){
        Long updatedUserId = userService.updateUser(userId, userRequest);

        //sets the location header attribute
        HttpHeaders headers = new HttpHeaders();
        headers.add("location","/api/user/"+updatedUserId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
