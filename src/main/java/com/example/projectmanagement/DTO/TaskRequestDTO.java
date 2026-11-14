package com.example.projectmanagement.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequestDTO {

    @NotBlank(message = "Task title is required")
    @Size(max = 150, message = "Task title must not exceed 150 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Task status is required")
    @Pattern(
            regexp = "TODO|IN_PROGRESS|IN_REVIEW|DONE",
            message = "Invalid task status"
    )
    private String status;

    @NotBlank(message = "Task priority is required")
    @Pattern(
            regexp = "LOW|MEDIUM|HIGH|URGENT",
            message = "Invalid task priority"
    )
    private String priority;

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be positive")
    private Long projectId;

    @Positive(message = "Assigned user ID must be positive")
    private Long assignedTo;

    @NotNull(message = "Creator ID is required")
    @Positive(message = "Creator ID must be positive")
    private Long createdBy;
}