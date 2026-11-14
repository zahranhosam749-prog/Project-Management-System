
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectMemberServiceTest {

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectMemberService projectMemberService;


    // 1. Add Member Successfully
    @Test
    void shouldAddMemberSuccessfully() {

        Project project = new Project();
        project.setId(1L);

        User user = new User();
        user.setId(2L);

        ProjectMemberRequestDTO request = new ProjectMemberRequestDTO();
        request.setProjectId(1L);
        request.setUserId(2L);
        request.setProjectRole("MANAGER");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectMemberRepository.existsByProjectIdAndUserId(1L, 2L))
                .thenReturn(false);

        when(projectMemberRepository.save(any(ProjectMember.class)))
                .thenAnswer(invocation -> {
                    ProjectMember member = invocation.getArgument(0);
                    member.setId(1L);
                    return member;
                });

        ProjectMemberResponseDTO response =
                projectMemberService.addMember(request);

        assertEquals(1L, response.getId());
        assertEquals(1L, response.getProjectId());
        assertEquals(2L, response.getUserId());
        assertEquals("MANAGER", response.getProjectRole());

        verify(projectMemberRepository, times(1))
                .save(any(ProjectMember.class));
    }


    // 2. Add Member - Default Role
    @Test
    void shouldAddMemberWithDefaultRole() {

        Project project = new Project();
        project.setId(1L);

        User user = new User();
        user.setId(2L);

        ProjectMemberRequestDTO request = new ProjectMemberRequestDTO();
        request.setProjectId(1L);
        request.setUserId(2L);
        request.setProjectRole(null);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectMemberRepository.existsByProjectIdAndUserId(1L, 2L))
                .thenReturn(false);

        when(projectMemberRepository.save(any(ProjectMember.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProjectMemberResponseDTO response =
                projectMemberService.addMember(request);

        assertEquals("MEMBER", response.getProjectRole());
    }


    // 3. Add Member - Project Not Found
    @Test
    void shouldThrowExceptionWhenAddingMemberToMissingProject() {

        ProjectMemberRequestDTO request = new ProjectMemberRequestDTO();
        request.setProjectId(99L);
        request.setUserId(2L);

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> projectMemberService.addMember(request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }


    // 4. Add Member - User Not Found
    @Test
    void shouldThrowExceptionWhenAddingMissingUser() {

        Project project = new Project();
        project.setId(1L);

        ProjectMemberRequestDTO request = new ProjectMemberRequestDTO();
        request.setProjectId(1L);
        request.setUserId(99L);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> projectMemberService.addMember(request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }


    // 5. Add Member - Already Exists
    @Test
    void shouldThrowExceptionWhenUserIsAlreadyMember() {

        Project project = new Project();
        User user = new User();

        ProjectMemberRequestDTO request = new ProjectMemberRequestDTO();
        request.setProjectId(1L);
        request.setUserId(2L);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectMemberRepository.existsByProjectIdAndUserId(1L, 2L))
                .thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> projectMemberService.addMember(request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }


    // 6. Add Member - Invalid Role
    @Test
    void shouldThrowExceptionWhenAddingMemberWithInvalidRole() {

        Project project = new Project();
        User user = new User();

        ProjectMemberRequestDTO request = new ProjectMemberRequestDTO();
        request.setProjectId(1L);
        request.setUserId(2L);
        request.setProjectRole("INVALID");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectMemberRepository.existsByProjectIdAndUserId(1L, 2L))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> projectMemberService.addMember(request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }


    // 7. Get Project Members
    @Test
    void shouldReturnProjectMembers() {

        Project project = new Project();
        project.setId(1L);

        User user = new User();
        user.setId(2L);

        ProjectMember member = new ProjectMember();
        member.setId(1L);
        member.setProject(project);
        member.setUser(user);
        member.setProjectRole("MEMBER");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(projectMemberRepository.findByProjectId(1L))
                .thenReturn(List.of(member));

        List<ProjectMemberResponseDTO> response =
                projectMemberService.getProjectMembers(1L);

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getProjectId());
        assertEquals(2L, response.get(0).getUserId());
        assertEquals("MEMBER", response.get(0).getProjectRole());
    }


    // 8. Get Project Members - Project Not Found
    @Test
    void shouldThrowExceptionWhenGettingMembersOfMissingProject() {

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> projectMemberService.getProjectMembers(99L));

        verify(projectMemberRepository, never())
                .findByProjectId(99L);
    }


    // 9. Remove Member Successfully
    @Test
    void shouldRemoveMemberSuccessfully() {

        Project project = new Project();
        User user = new User();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectMemberRepository.existsByProjectIdAndUserId(1L, 2L))
                .thenReturn(true);

        projectMemberService.removeMember(1L, 2L);

        verify(projectMemberRepository, times(1))
                .deleteByProjectIdAndUserId(1L, 2L);
    }


    // 10. Remove Member - Project Not Found
    @Test
    void shouldThrowExceptionWhenRemovingMemberFromMissingProject() {

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> projectMemberService.removeMember(99L, 2L));

        verify(projectMemberRepository, never())
                .deleteByProjectIdAndUserId(anyLong(), anyLong());
    }


    // 11. Remove Member - User Not Found
    @Test
    void shouldThrowExceptionWhenRemovingMissingUser() {

        Project project = new Project();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> projectMemberService.removeMember(1L, 99L));

        verify(projectMemberRepository, never())
                .deleteByProjectIdAndUserId(anyLong(), anyLong());
    }


    // 12. Remove Member - User Is Not A Member
    @Test
    void shouldThrowExceptionWhenRemovingNonMember() {

        Project project = new Project();
        User user = new User();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(projectMemberRepository.existsByProjectIdAndUserId(1L, 2L))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> projectMemberService.removeMember(1L, 2L));

        verify(projectMemberRepository, never())
                .deleteByProjectIdAndUserId(anyLong(), anyLong());
    }


    // 13. Update Member Role Successfully
    @Test
    void shouldUpdateMemberRoleSuccessfully() {

        Project project = new Project();
        project.setId(1L);

        User user = new User();
        user.setId(2L);

        ProjectMember member = new ProjectMember();
        member.setId(1L);
        member.setProject(project);
        member.setUser(user);
        member.setProjectRole("MEMBER");

        ProjectMemberRoleRequestDTO request =
                new ProjectMemberRoleRequestDTO();

        request.setProjectRole("MANAGER");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(projectMemberRepository.findByProjectId(1L))
                .thenReturn(List.of(member));

        when(projectMemberRepository.save(any(ProjectMember.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProjectMemberResponseDTO response =
                projectMemberService.updateMemberRole(1L, 2L, request);

        assertEquals("MANAGER", response.getProjectRole());

        verify(projectMemberRepository, times(1))
                .save(member);
    }


    // 14. Update Member Role - Project Not Found
    @Test
    void shouldThrowExceptionWhenUpdatingRoleInMissingProject() {

        ProjectMemberRoleRequestDTO request =
                new ProjectMemberRoleRequestDTO();

        request.setProjectRole("MANAGER");

        when(projectRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> projectMemberService.updateMemberRole(99L, 2L, request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }


    // 15. Update Member Role - User Is Not A Member
    @Test
    void shouldThrowExceptionWhenUpdatingRoleOfNonMember() {

        Project project = new Project();

        ProjectMemberRoleRequestDTO request =
                new ProjectMemberRoleRequestDTO();

        request.setProjectRole("MANAGER");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(projectMemberRepository.findByProjectId(1L))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> projectMemberService.updateMemberRole(1L, 99L, request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }


    // 16. Update Member Role - Missing Role
    @Test
    void shouldThrowExceptionWhenUpdatingWithMissingRole() {

        Project project = new Project();
        User user = new User();

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);

        ProjectMemberRoleRequestDTO request =
                new ProjectMemberRoleRequestDTO();

        request.setProjectRole(" ");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(projectMemberRepository.findByProjectId(1L))
                .thenReturn(List.of(member));

        assertThrows(RuntimeException.class,
                () -> projectMemberService.updateMemberRole(1L, 2L, request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }


    // 17. Update Member Role - Invalid Role
    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidRole() {

        Project project = new Project();
        User user = new User();

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);

        ProjectMemberRoleRequestDTO request =
                new ProjectMemberRoleRequestDTO();

        request.setProjectRole("INVALID");

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(projectMemberRepository.findByProjectId(1L))
                .thenReturn(List.of(member));

        assertThrows(RuntimeException.class,
                () -> projectMemberService.updateMemberRole(1L, 2L, request));

        verify(projectMemberRepository, never())
                .save(any(ProjectMember.class));
    }
}
