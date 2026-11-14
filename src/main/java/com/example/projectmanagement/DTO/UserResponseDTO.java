
package com.example.projectmanagement.DTO;
import com.example.projectmanagement.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
