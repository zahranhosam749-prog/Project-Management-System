package com.example.projectmanagement.Controller;

import com.example.projectmanagement.DTO.ProjectMemberRequestDTO;
import com.example.projectmanagement.DTO.ProjectMemberResponseDTO;
import com.example.projectmanagement.DTO.ProjectMemberRoleRequestDTO;
import com.example.projectmanagement.Service.ProjectMemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-members")
@AllArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<ProjectMemberResponseDTO> addMember(
            @RequestBody ProjectMemberRequestDTO request) {

        ProjectMemberResponseDTO response =
                projectMemberService.addMember(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{projectId}")
    public ResponseEntity<List<ProjectMemberResponseDTO>> getProjectMembers(
            @PathVariable Long projectId) {

        List<ProjectMemberResponseDTO> members =
                projectMemberService.getProjectMembers(projectId);

        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/{projectId}/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectMemberService.removeMember(projectId, userId);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{projectId}/{userId}/role")
    public ResponseEntity<ProjectMemberResponseDTO> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestBody ProjectMemberRoleRequestDTO request) {

        ProjectMemberResponseDTO response =
                projectMemberService.updateMemberRole(
                        projectId,
                        userId,
                        request
                );

        return ResponseEntity.ok(response);
    }


}