package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.ProjectMemberRequestDTO;
import com.example.projectmanagement.DTO.ProjectMemberResponseDTO;
import com.example.projectmanagement.DTO.ProjectMemberRoleRequestDTO;
import com.example.projectmanagement.Model.Project;
import com.example.projectmanagement.Model.ProjectMember;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.ProjectMemberRepository;
import com.example.projectmanagement.Repository.ProjectRepository;
import com.example.projectmanagement.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectMemberResponseDTO addMember(ProjectMemberRequestDTO request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        boolean alreadyMember = projectMemberRepository
                .existsByProjectIdAndUserId(
                        request.getProjectId(),
                        request.getUserId()
                );

        if (alreadyMember) {
            throw new RuntimeException("User is already a member of this project!");
        }
        String role = request.getProjectRole();

        if (role == null || role.isBlank()) {
            role = "MEMBER";
        }

        role = role.toUpperCase();

        if (!Set.of("OWNER", "MANAGER", "MEMBER").contains(role)) {
            throw new RuntimeException("Invalid project role!");
        }

        ProjectMember member = new ProjectMember();

        member.setProject(project);
        member.setUser(user);
        member.setProjectRole(role);
        ProjectMember savedMember = projectMemberRepository.save(member);

        return new ProjectMemberResponseDTO(
                savedMember.getId(),
                savedMember.getProject().getId(),
                savedMember.getUser().getId(),
                savedMember.getProjectRole(),
                savedMember.getJoinedAt()
        );
    }
    public List<ProjectMemberResponseDTO> getProjectMembers(Long projectId) {

        projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        List<ProjectMember> members =
                projectMemberRepository.findByProjectId(projectId);

        return members.stream()
                .map(member -> new ProjectMemberResponseDTO(
                        member.getId(),
                        member.getProject().getId(),
                        member.getUser().getId(),
                        member.getProjectRole(),
                        member.getJoinedAt()
                ))
                .toList();
    }
    public void removeMember(Long projectId, Long userId) {


        projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));


        boolean isMember = projectMemberRepository
                .existsByProjectIdAndUserId(projectId, userId);

        if (!isMember) {
            throw new RuntimeException("User is not a member of this project!");
        }
        projectMemberRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    public ProjectMemberResponseDTO updateMemberRole(
            Long projectId,
            Long userId,
            ProjectMemberRoleRequestDTO request) {

        projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        ProjectMember member = projectMemberRepository
                .findByProjectId(projectId)
                .stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User is not a member of this project!"));

        String role = request.getProjectRole();

        if (role == null || role.isBlank()) {
            throw new RuntimeException("Project role is required!");
        }

        role = role.toUpperCase();

        if (!Set.of("OWNER", "MANAGER", "MEMBER").contains(role)) {
            throw new RuntimeException("Invalid project role!");
        }

        member.setProjectRole(role);

        ProjectMember updatedMember = projectMemberRepository.save(member);

        return new ProjectMemberResponseDTO(
                updatedMember.getId(),
                updatedMember.getProject().getId(),
                updatedMember.getUser().getId(),
                updatedMember.getProjectRole(),
                updatedMember.getJoinedAt()
        );
    }

}
