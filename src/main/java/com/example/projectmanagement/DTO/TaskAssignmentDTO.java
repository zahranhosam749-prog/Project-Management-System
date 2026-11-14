package com.example.projectmanagement.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAssignmentDTO {

    @NotNull(message = "Assigned user ID is required")
    @Positive(message = "Assigned user ID must be positive")
    private Long assignedTo;
}