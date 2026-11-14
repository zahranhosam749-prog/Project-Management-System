package com.example.projectmanagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectMemberRequestDTO {

    private Long projectId;

    private Long userId;

    private String projectRole = "MEMBER";
}