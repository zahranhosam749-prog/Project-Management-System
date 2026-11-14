
package com.example.projectmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {

    private String email;
    private String role;
    private String message;
    private String token;

}
