
package com.example.projectmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long id;

    private String name;

    private String description;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long createdBy;

    private LocalDateTime createdAt;
}
