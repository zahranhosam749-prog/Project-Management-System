
package com.example.projectmanagement.Controller;

import com.example.projectmanagement.DTO.AuthRequestDTO;
import com.example.projectmanagement.DTO.AuthResponseDTO;
import com.example.projectmanagement.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody AuthRequestDTO request) {

        return ResponseEntity.ok(authService.login(request));
    }
}
