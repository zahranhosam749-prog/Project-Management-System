package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.UserRequestDTO;
import com.example.projectmanagement.DTO.UserResponseDTO;
import com.example.projectmanagement.Exception.ResourceNotFoundException;
import com.example.projectmanagement.Model.Role;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO create (UserRequestDTO request) {

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

       User savedUser = userRepository.save(user);

       return new UserResponseDTO(
               savedUser.getId(),
               savedUser.getFullName(),
               savedUser.getEmail(),
               savedUser.getRole(),
               savedUser.getCreatedAt()
       );
    }
   public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                )).toList();
   }

   public  UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found!"));
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
   }
   public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found!"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
       user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
   }
   public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found!"));
        userRepository.delete(user);
   }

   public UserResponseDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
   }

}
