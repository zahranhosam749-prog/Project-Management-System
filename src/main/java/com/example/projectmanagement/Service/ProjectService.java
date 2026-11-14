
package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.ProjectRequestDTO;
import com.example.projectmanagement.DTO.ProjectResponseDTO;
import com.example.projectmanagement.Exception.ResourceNotFoundException;
import com.example.projectmanagement.Model.Project;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.ProjectRepository;
import com.example.projectmanagement.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectResponseDTO create(ProjectRequestDTO request) {

        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(
                request.getStatus() == null ? "PLANNING" : request.getStatus()
        );
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setCreatedBy(user);

        Project savedProject = projectRepository.save(project);

        return new ProjectResponseDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getStatus(),
                savedProject.getStartDate(),
                savedProject.getEndDate(),
                savedProject.getCreatedBy().getId(),
                savedProject.getCreatedAt()
        );
    }

    public List<ProjectResponseDTO> findAll() {

        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(project -> new ProjectResponseDTO(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getStatus(),
                        project.getStartDate(),
                        project.getEndDate(),
                        project.getCreatedBy().getId(),
                        project.getCreatedAt()
                ))
                .collect(Collectors.toList());

    }

    public ProjectResponseDTO findById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found!"));

        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate(),
                project.getCreatedBy().getId(),
                project.getCreatedAt()
        );
    }

    public ProjectResponseDTO update(Long id, ProjectRequestDTO request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found!"));

        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setCreatedBy(user);

        Project updatedProject = projectRepository.save(project);

        return new ProjectResponseDTO(
                updatedProject.getId(),
                updatedProject.getName(),
                updatedProject.getDescription(),
                updatedProject.getStatus(),
                updatedProject.getStartDate(),
                updatedProject.getEndDate(),
                updatedProject.getCreatedBy().getId(),
                updatedProject.getCreatedAt()
        );
    }

    public void delete(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found!"));

        projectRepository.delete(project);
    }




}
