
package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.ProjectRequestDTO;
import com.example.projectmanagement.DTO.ProjectResponseDTO;
import com.example.projectmanagement.Exception.ResourceNotFoundException;
import com.example.projectmanagement.Model.Project;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.ProjectRepository;
import com.example.projectmanagement.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;


    // 1. Create Project Successfully
    @Test
    void shouldCreateProjectSuccessfully() {

        User user = new User();
        user.setId(1L);

        ProjectRequestDTO request = new ProjectRequestDTO();

        request.setName("Test Project");
        request.setDescription("Testing project creation");
        request.setStatus("PLANNING");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(10));
        request.setCreatedBy(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> {
                    Project project = invocation.getArgument(0);
                    project.setId(1L);
                    return project;
                });

        ProjectResponseDTO response = projectService.create(request);

        assertEquals("Test Project", response.getName());
        assertEquals("PLANNING", response.getStatus());
        assertEquals(1L, response.getCreatedBy());

        verify(projectRepository, times(1))
                .save(any(Project.class));
    }


    // 2. Create Project - User Not Found
    @Test
    void shouldThrowExceptionWhenCreatorNotFound() {

        ProjectRequestDTO request = new ProjectRequestDTO();
        request.setCreatedBy(99L);

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> projectService.create(request));

        verify(projectRepository, never())
                .save(any(Project.class));
    }


    // 3. Find All Projects
    @Test
    void shouldReturnAllProjects() {

        User user = new User();
        user.setId(1L);

        Project project = new Project();

        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Testing find all");
        project.setStatus("PLANNING");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(10));
        project.setCreatedBy(user);

        when(projectRepository.findAll())
                .thenReturn(List.of(project));

        List<ProjectResponseDTO> response =
                projectService.findAll();

        assertEquals(1, response.size());
        assertEquals("Test Project", response.get(0).getName());
        assertEquals(1L, response.get(0).getCreatedBy());
    }


    // 4. Find Project By ID
    @Test
    void shouldFindProjectByIdSuccessfully() {

        User user = new User();
        user.setId(1L);

        Project project = new Project();

        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Testing find by ID");
        project.setStatus("PLANNING");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(10));
        project.setCreatedBy(user);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponseDTO response =
                projectService.findById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Test Project", response.getName());
        assertEquals(1L, response.getCreatedBy());
    }


    // 5. Find Project By ID - Not Found
    @Test
    void shouldThrowExceptionWhenProjectNotFound() {

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> projectService.findById(99L));
    }


    // 6. Update Project Successfully
    @Test
    void shouldUpdateProjectSuccessfully() {

        User user = new User();
        user.setId(1L);

        Project project = new Project();

        project.setId(1L);
        project.setName("Old Project");
        project.setCreatedBy(user);

        ProjectRequestDTO request = new ProjectRequestDTO();

        request.setName("Updated Project");
        request.setDescription("Updated description");
        request.setStatus("IN_PROGRESS");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(15));
        request.setCreatedBy(1L);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProjectResponseDTO response =
                projectService.update(1L, request);

        assertEquals("Updated Project", response.getName());
        assertEquals("Updated description", response.getDescription());
        assertEquals("IN_PROGRESS", response.getStatus());

        verify(projectRepository, times(1))
                .save(any(Project.class));
    }


    // 7. Update Project - Not Found
    @Test
    void shouldThrowExceptionWhenUpdatingMissingProject() {

        ProjectRequestDTO request = new ProjectRequestDTO();

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> projectService.update(99L, request));

        verify(projectRepository, never())
                .save(any(Project.class));
    }


    // 8. Delete Project Successfully
    @Test
    void shouldDeleteProjectSuccessfully() {

        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        projectService.delete(1L);

        verify(projectRepository, times(1))
                .delete(project);
    }


    // 9. Delete Project - Not Found
    @Test
    void shouldThrowExceptionWhenDeletingMissingProject() {

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> projectService.delete(99L));

        verify(projectRepository, never())
                .delete(any(Project.class));
    }
}
