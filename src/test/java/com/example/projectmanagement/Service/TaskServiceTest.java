
package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.TaskRequestDTO;
import com.example.projectmanagement.DTO.TaskResponseDTO;
import com.example.projectmanagement.Model.Project;
import com.example.projectmanagement.Model.Task;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.ProjectRepository;
import com.example.projectmanagement.Repository.TaskRepository;
import com.example.projectmanagement.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTaskSuccessfully() {

        Project project = new Project();
        User creator = new User();
        User assignee = new User();

        TaskRequestDTO request = new TaskRequestDTO();

        request.setTitle("Test Task");
        request.setDescription("Testing task creation");
        request.setStatus("TODO");
        request.setPriority("HIGH");

        request.setProjectId(2L);
        request.setAssignedTo(2L);
        request.setCreatedBy(1L);

        project.setId(2L);
        creator.setId(1L);
        assignee.setId(2L);

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(creator));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(assignee));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDTO response = taskService.create(request);

        assertEquals("Test Task", response.getTitle());
        assertEquals("HIGH", response.getPriority());
        assertEquals(2L, response.getProjectId());
        assertEquals(2L, response.getAssignedTo());
    }
}
