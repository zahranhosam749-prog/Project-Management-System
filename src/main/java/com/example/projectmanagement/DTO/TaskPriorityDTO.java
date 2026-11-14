package com.example.projectmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskPriorityDTO {

    @NotBlank(message = "Task priority is required")
    @Pattern(
            regexp = "LOW|MEDIUM|HIGH|URGENT",
            message = "Invalid task priority"

    )
    private String priority;
}