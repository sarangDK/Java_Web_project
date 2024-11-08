package ca.gbc.userservice.service;

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
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final TypeRepository typeRepository;


    @Override
    public UserResponse createUser(UserRequest userRequest) {

        // check if the type exists with the type_id
        // throw RunTime error when type not found with typeId
        Type type = typeRepository.findById(userRequest.type_id())
                .orElseThrow(() -> new RuntimeException("Type with ID " + userRequest.type_id() + " not found"));

        User user = User.builder()
                .user_name(userRequest.user_name())
                .user_email(userRequest.user_email())
                .type(type)
                .build();

        //persist a product
        userRepository.save(user);
        return new UserResponse(user.getUser_id(), user.getUser_name(),
                user.getUser_email(), user.getType());
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(this::mapToUserResponse).toList();
    }

    private UserResponse mapToUserResponse(User user){
        return new UserResponse(user.getUser_id(), user.getUser_name(),
                user.getUser_email(),user.getType());
    }


    @Override
    public UserResponse getUserById(Long userId) {
        // check to see if user exists with the userId
        // throw RunTime error when user not found with userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        return new UserResponse(user.getUser_id(), user.getUser_name(),
                user.getUser_email(),user.getType());
    }

    @Override
    public TypeResponse getType(Long userId) {
        // check to see if user exists with the userId
        // throw RunTime error when user not found with userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        return new TypeResponse(user.getType().getType_id(), user.getType().getType_name());
    }


    @Override
    public Long updateUser(Long userId, UserRequest userRequest) {

        // check to see if user exists with the userId
        // throw RunTime error when user not found with userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));


        // check if the type exists with the type_id
        // throw RunTime error when type not found with typeId
        Type type = typeRepository.findById(userRequest.type_id())
                .orElseThrow(() -> new RuntimeException("Type with ID " + userRequest.type_id() + " not found"));


        // if user and type exists
        user.setUser_name(userRequest.user_name());
        user.setUser_email(userRequest.user_email());
        user.setType(type);
        return userRepository.save(user).getUser_id();
    }

    @Override
    public void deleteUser(Long userId) {

        // check to see if user exists with the userId
        // throw RunTime error when user not found with userId
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        userRepository.deleteById(userId);
    }
}
