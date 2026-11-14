
package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.*;
import com.example.projectmanagement.Exception.ResourceNotFoundException;
import com.example.projectmanagement.Model.Project;
import com.example.projectmanagement.Model.Task;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.TaskRepository;
import com.example.projectmanagement.Repository.ProjectRepository;
import com.example.projectmanagement.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;


    public TaskResponseDTO create(TaskRequestDTO request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found!"));

        User creator = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found!"));

        User assignee = null;

        if (request.getAssignedTo() != null) {
            assignee = userRepository.findById(request.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found!"));
        }

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() == null ? "TODO" : request.getStatus());
        task.setPriority(request.getPriority() == null ? "MEDIUM" : request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setProject(project);
        task.setAssignedTo(assignee);
        task.setCreatedBy(creator);

        Task savedTask = taskRepository.save(task);
        entityManager.refresh(savedTask);

        return new TaskResponseDTO(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getPriority(),
                savedTask.getDueDate(),
                savedTask.getProject().getId(),
                savedTask.getAssignedTo() == null ? null : savedTask.getAssignedTo().getId(),
                savedTask.getCreatedBy().getId(),
                savedTask.getCreatedAt(),
                savedTask.getUpdatedAt()
        );

    }

    public List<TaskResponseDTO> findAll() {

        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getDueDate(),
                        task.getProject().getId(),
                        task.getAssignedTo() == null ? null : task.getAssignedTo().getId(),
                        task.getCreatedBy().getId(),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                ))
                .toList();
    }

    public TaskResponseDTO findById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getProject().getId(),
                task.getAssignedTo() == null ? null : task.getAssignedTo().getId(),
                task.getCreatedBy().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public TaskResponseDTO update(Long id, TaskRequestDTO request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        User creator = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Creator user not found!"));

        User assignee = null;

        if (request.getAssignedTo() != null) {
            assignee = userRepository.findById(request.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found!"));
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setProject(project);
        task.setAssignedTo(assignee);
        task.setCreatedBy(creator);

        Task updatedTask = taskRepository.saveAndFlush(task);
        entityManager.refresh(updatedTask);

        return new TaskResponseDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedTask.getPriority(),
                updatedTask.getDueDate(),
                updatedTask.getProject().getId(),
                updatedTask.getAssignedTo() == null ? null : updatedTask.getAssignedTo().getId(),
                updatedTask.getCreatedBy().getId(),
                updatedTask.getCreatedAt(),
                updatedTask.getUpdatedAt()
        );
    }

    public void delete(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        taskRepository.delete(task);
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {

        if (currentStatus.equals("TODO") && newStatus.equals("IN_PROGRESS")) {
            return true;
        }

        if (currentStatus.equals("IN_PROGRESS") && newStatus.equals("IN_REVIEW")) {
            return true;
        }

        if (currentStatus.equals("IN_REVIEW") && newStatus.equals("DONE")) {
            return true;
        }

        return false;
    }

    public TaskResponseDTO updateStatus(Long id, TaskStatusDTO request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        String currentStatus = task.getStatus();
        String newStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Invalid status transition!");
        }

        task.setStatus(newStatus);

        Task updatedTask = taskRepository.saveAndFlush(task);
        entityManager.refresh(updatedTask);

        return new TaskResponseDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedTask.getPriority(),
                updatedTask.getDueDate(),
                updatedTask.getProject().getId(),
                updatedTask.getAssignedTo() == null ? null : updatedTask.getAssignedTo().getId(),
                updatedTask.getCreatedBy().getId(),
                updatedTask.getCreatedAt(),
                updatedTask.getUpdatedAt()
        );
    }
    public TaskResponseDTO assignTask(Long id, TaskAssignmentDTO request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        if (task.getStatus().equals("DONE")) {
            throw new RuntimeException("Cannot assign a completed task!");
        }
        User user = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        task.setAssignedTo(user);
        Task updatedTask = taskRepository.save(task);

        return new TaskResponseDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedTask.getPriority(),
                updatedTask.getDueDate(),
                updatedTask.getProject().getId(),
                updatedTask.getAssignedTo() == null ? null : updatedTask.getAssignedTo().getId(),
                updatedTask.getCreatedBy().getId(),
                updatedTask.getCreatedAt(),
                updatedTask.getUpdatedAt()
        );

    }
    public TaskResponseDTO updatePriority(Long id, TaskPriorityDTO request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found!"));

        String newPriority = request.getPriority();



        task.setPriority(newPriority);

        Task updatedTask = taskRepository.saveAndFlush(task);
        entityManager.refresh(updatedTask);

        return new TaskResponseDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedTask.getPriority(),
                updatedTask.getDueDate(),
                updatedTask.getProject().getId(),
                updatedTask.getAssignedTo() == null ? null : updatedTask.getAssignedTo().getId(),
                updatedTask.getCreatedBy().getId(),
                updatedTask.getCreatedAt(),
                updatedTask.getUpdatedAt()
        );

    }











}
