
package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.UserRequestDTO;
import com.example.projectmanagement.DTO.UserResponseDTO;
import com.example.projectmanagement.Exception.ResourceNotFoundException;
import com.example.projectmanagement.Model.Role;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    // 1. Create User Successfully
    @Test
    void shouldCreateUserSuccessfully() {

        UserRequestDTO request = new UserRequestDTO();

        request.setFullName("Ahmed Mohamed");
        request.setEmail("ahmed@example.com");
        request.setPassword("Password123");
        request.setRole(Role.USER);

        when(passwordEncoder.encode(any(CharSequence.class)))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        UserResponseDTO response = userService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("Ahmed Mohamed", response.getFullName());
        assertEquals("ahmed@example.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());

        verify(userRepository, times(1))
                .save(any(User.class));
    }


    // 2. Find All Users
    @Test
    void shouldReturnAllUsers() {

        User user = new User();

        user.setId(1L);
        user.setFullName("Ahmed Mohamed");
        user.setEmail("ahmed@example.com");
        user.setRole(Role.USER);

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserResponseDTO> response = userService.findAll();

        assertEquals(1, response.size());
        assertEquals("Ahmed Mohamed", response.get(0).getFullName());
        assertEquals("ahmed@example.com", response.get(0).getEmail());
    }


    // 3. Find All Users - Empty List
    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {

        when(userRepository.findAll())
                .thenReturn(List.of());

        List<UserResponseDTO> response = userService.findAll();

        assertTrue(response.isEmpty());
    }


    // 4. Find User By ID
    @Test
    void shouldFindUserByIdSuccessfully() {

        User user = new User();

        user.setId(1L);
        user.setFullName("Ahmed Mohamed");
        user.setEmail("ahmed@example.com");
        user.setRole(Role.USER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponseDTO response = userService.findById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Ahmed Mohamed", response.getFullName());
        assertEquals("ahmed@example.com", response.getEmail());
    }


    // 5. Find User By ID - Not Found
    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.findById(99L));
    }


    // 6. Update User Successfully
    @Test
    void shouldUpdateUserSuccessfully() {

        User user = new User();

        user.setId(1L);
        user.setFullName("Old Name");
        user.setEmail("old@example.com");
        user.setRole(Role.USER);
        user.setPassword("OldPassword123");

        when(passwordEncoder.encode(any(CharSequence.class)))
                .thenReturn("encodedPassword");

        UserRequestDTO request = new UserRequestDTO();

        request.setFullName("Updated Name");
        request.setEmail("updated@example.com");
        request.setRole(Role.ADMIN);
        request.setPassword("NewPassword123");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.update(1L, request);

        assertEquals("Updated Name", response.getFullName());
        assertEquals("updated@example.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());

        verify(userRepository, times(1))
                .save(any(User.class));
    }


    // 7. Update User - Not Found
    @Test
    void shouldThrowExceptionWhenUpdatingMissingUser() {

        UserRequestDTO request = new UserRequestDTO();

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.update(99L, request));

        verify(userRepository, never())
                .save(any(User.class));
    }


    // 8. Delete User Successfully
    @Test
    void shouldDeleteUserSuccessfully() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository, times(1))
                .delete(user);
    }


    // 9. Delete User - Not Found
    @Test
    void shouldThrowExceptionWhenDeletingMissingUser() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.delete(99L));

        verify(userRepository, never())
                .delete(any(User.class));
    }


    // 10. Get User By Email
    @Test
    void shouldFindUserByEmailSuccessfully() {

        User user = new User();

        user.setId(1L);
        user.setFullName("Ahmed Mohamed");
        user.setEmail("ahmed@example.com");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("ahmed@example.com"))
                .thenReturn(Optional.of(user));

        UserResponseDTO response =
                userService.getByEmail("ahmed@example.com");

        assertEquals(1L, response.getId());
        assertEquals("Ahmed Mohamed", response.getFullName());
        assertEquals("ahmed@example.com", response.getEmail());
    }


    // 11. Get User By Email - Not Found
    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {

        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getByEmail("missing@example.com"));
    }
}
