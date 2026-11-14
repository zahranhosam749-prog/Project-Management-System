
package com.example.projectmanagement.Controller;

import com.example.projectmanagement.DTO.*;
import com.example.projectmanagement.Service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Tasks")
public class TaskController {

    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid@RequestBody TaskRequestDTO request) {

        TaskResponseDTO responseDTO = taskService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll() {

        List<TaskResponseDTO> responseDTOS = taskService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id) {

        TaskResponseDTO responseDTO = taskService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(
            @PathVariable Long id,
           @Valid @RequestBody TaskRequestDTO request) {

        TaskResponseDTO responseDTO = taskService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        taskService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(
            @PathVariable Long id,
          @Valid  @RequestBody TaskStatusDTO request) {

        TaskResponseDTO responseDTO = taskService.updateStatus(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    @PatchMapping("/{id}/assignment")
    public ResponseEntity<TaskResponseDTO> assignTask(
            @PathVariable Long id,
            @Valid@RequestBody TaskAssignmentDTO request) {

        TaskResponseDTO responseDTO = taskService.assignTask(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    @PatchMapping("/{id}/priority")
    public ResponseEntity<TaskResponseDTO> updatePriority(
            @PathVariable Long id,
           @Valid @RequestBody TaskPriorityDTO request) {

        TaskResponseDTO responseDTO = taskService.updatePriority(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }






}
