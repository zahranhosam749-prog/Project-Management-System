package com.example.projectmanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusDTO {

    @NotBlank(message = "Task status is required")
    @Pattern(
            regexp = "TODO|IN_PROGRESS|IN_REVIEW|DONE",
            message = "Invalid task status"
    )
    private String status;

}