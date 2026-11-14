package com.example.projectmanagement.Controller;

import com.example.projectmanagement.DTO.UserRequestDTO;
import com.example.projectmanagement.DTO.UserResponseDTO;
import com.example.projectmanagement.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Users")
public class UserController {
    private UserService userService;


@PostMapping
public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO request){
    UserResponseDTO responseDTO = userService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
}
@GetMapping
 public ResponseEntity<List<UserResponseDTO>> getAll(){
    List<UserResponseDTO> responseDTOS = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
}
@GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
    UserResponseDTO responseDTO = userService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
}
@PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id,@Valid @RequestBody UserRequestDTO request){
    UserResponseDTO responseDTO = userService.update(id, request);
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

}
@GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getByEmail(@PathVariable String email){
    UserResponseDTO responseDTO = userService.getByEmail(email);
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
}
@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
    userService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
}
}
