
package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.AuthRequestDTO;
import com.example.projectmanagement.DTO.AuthResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JWTService jwtService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO login(AuthRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
        String token = jwtService.generateToken(authentication.getName());

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        return new AuthResponseDTO(
                authentication.getName(),
                role,
                "Login successful",
                token

        );

    }

}
