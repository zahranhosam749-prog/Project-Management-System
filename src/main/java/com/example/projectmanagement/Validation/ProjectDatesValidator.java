package com.example.projectmanagement.Validation;

import com.example.projectmanagement.DTO.ProjectRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProjectDatesValidator implements ConstraintValidator<ValidProjectDates, ProjectRequestDTO> {

    @Override
    public boolean isValid(ProjectRequestDTO request, ConstraintValidatorContext context) {

        if (request == null || request.getStartDate() == null || request.getEndDate() == null) {
            return true;
        }

        return request.getEndDate().isAfter(request.getStartDate());
    }
}