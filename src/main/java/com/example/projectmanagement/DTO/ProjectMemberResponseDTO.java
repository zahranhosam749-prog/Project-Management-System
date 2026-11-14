package com.example.projectmanagement.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberResponseDTO {

    private Long id;

    private Long projectId;

    private Long userId;

    private String projectRole;

    private LocalDateTime joinedAt;
}