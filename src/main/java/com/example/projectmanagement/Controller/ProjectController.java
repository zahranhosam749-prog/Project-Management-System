
package com.example.projectmanagement.Controller;

import com.example.projectmanagement.DTO.ProjectRequestDTO;
import com.example.projectmanagement.DTO.ProjectResponseDTO;
import com.example.projectmanagement.Service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(
           @Valid @RequestBody ProjectRequestDTO request) {

        ProjectResponseDTO responseDTO = projectService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAll() {

        List<ProjectResponseDTO> responseDTOS = projectService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getById(@PathVariable Long id) {

        ProjectResponseDTO responseDTO = projectService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(
            @PathVariable Long id,
           @Valid @RequestBody ProjectRequestDTO request) {

        ProjectResponseDTO responseDTO = projectService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        projectService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
