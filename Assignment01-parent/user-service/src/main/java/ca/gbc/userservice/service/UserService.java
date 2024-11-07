package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.TypeResponse;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long userId);
    TypeResponse getType(Long userId);
    Long updateUser(Long userId, UserRequest userRequest);
    void deleteUser(Long userId);
}
