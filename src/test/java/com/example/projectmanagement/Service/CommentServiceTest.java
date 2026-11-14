
package com.example.projectmanagement.Service;

import com.example.projectmanagement.DTO.CommentDeleteDTO;
import com.example.projectmanagement.DTO.CommentRequestDTO;
import com.example.projectmanagement.DTO.CommentResponseDTO;
import com.example.projectmanagement.DTO.CommentUpdateDTO;
import com.example.projectmanagement.Model.Comment;
import com.example.projectmanagement.Model.Project;
import com.example.projectmanagement.Model.Task;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.CommentRepository;
import com.example.projectmanagement.Repository.ProjectMemberRepository;
import com.example.projectmanagement.Repository.TaskRepository;
import com.example.projectmanagement.Repository.UserRepository;
import jakarta.persistence.EntityManager;
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
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CommentService commentService;


    private Project createProject() {
        Project project = new Project();
        project.setId(2L);
        return project;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Ahmed Mohamed Ali");
        return user;
    }

    private Task createTask() {
        Task task = new Task();
        task.setId(3L);
        task.setProject(createProject());
        return task;
    }

    private Comment createComment() {
        Comment comment = new Comment();
        comment.setId(5L);
        comment.setContent("Test comment");
        comment.setTask(createTask());
        comment.setUser(createUser());
        return comment;
    }


    @Test
    void shouldAddCommentSuccessfully() {

        Task task = createTask();
        User user = createUser();

        CommentRequestDTO request = new CommentRequestDTO();
        request.setContent("New comment");
        request.setTaskId(3L);
        request.setUserId(1L);

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(true);

        when(commentRepository.saveAndFlush(any(Comment.class)))
                .thenAnswer(invocation -> {
                    Comment comment = invocation.getArgument(0);
                    comment.setId(5L);
                    return comment;
                });

        CommentResponseDTO response = commentService.addComment(request);

        assertEquals(5L, response.getId());
        assertEquals("New comment", response.getContent());
        assertEquals(3L, response.getTaskId());
        assertEquals(1L, response.getUserId());
        assertEquals("Ahmed Mohamed Ali", response.getUserName());

        verify(entityManager).refresh(any(Comment.class));
    }


    @Test
    void shouldThrowExceptionWhenAddingCommentToMissingTask() {

        CommentRequestDTO request = new CommentRequestDTO();
        request.setTaskId(3L);
        request.setUserId(1L);

        when(taskRepository.findById(3L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.addComment(request)
        );

        assertEquals("Task not found!", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenAddingCommentWithMissingUser() {

        CommentRequestDTO request = new CommentRequestDTO();
        request.setTaskId(3L);
        request.setUserId(1L);

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(createTask()));

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.addComment(request)
        );

        assertEquals("User not found!", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenAddingCommentByNonMember() {

        CommentRequestDTO request = new CommentRequestDTO();
        request.setTaskId(3L);
        request.setUserId(1L);
        request.setContent("Test comment");

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(createTask()));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(createUser()));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.addComment(request)
        );

        assertEquals(
                "User is not a member of this project!",
                exception.getMessage()
        );
    }


    @Test
    void shouldThrowExceptionWhenAddingEmptyComment() {

        CommentRequestDTO request = new CommentRequestDTO();
        request.setTaskId(3L);
        request.setUserId(1L);
        request.setContent("   ");

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(createTask()));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(createUser()));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.addComment(request)
        );

        assertEquals("Comment content is required!", exception.getMessage());
    }


    @Test
    void shouldReturnTaskCommentsSuccessfully() {

        Comment comment = createComment();

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(createTask()));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(true);

        when(commentRepository.findByTaskId(3L))
                .thenReturn(List.of(comment));

        List<CommentResponseDTO> response =
                commentService.getTaskComments(3L, 1L);

        assertEquals(1, response.size());
        assertEquals("Test comment", response.get(0).getContent());
        assertEquals("Ahmed Mohamed Ali", response.get(0).getUserName());
    }


    @Test
    void shouldThrowExceptionWhenGettingCommentsForMissingTask() {

        when(taskRepository.findById(3L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.getTaskComments(3L, 1L)
        );

        assertEquals("Task not found!", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenNonMemberViewsTaskComments() {

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(createTask()));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.getTaskComments(3L, 1L)
        );

        assertEquals(
                "You are not allowed to view comments!",
                exception.getMessage()
        );
    }


    @Test
    void shouldFindCommentByIdSuccessfully() {

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(createComment()));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(true);

        CommentResponseDTO response =
                commentService.getCommentById(5L, 1L);

        assertEquals(5L, response.getId());
        assertEquals("Test comment", response.getContent());
        assertEquals("Ahmed Mohamed Ali", response.getUserName());
    }


    @Test
    void shouldThrowExceptionWhenCommentNotFoundById() {

        when(commentRepository.findById(5L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.getCommentById(5L, 1L)
        );

        assertEquals("Comment not found!", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenNonMemberViewsComment() {

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(createComment()));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.getCommentById(5L, 1L)
        );

        assertEquals(
                "You are not allowed to view this comment!",
                exception.getMessage()
        );
    }


    @Test
    void shouldDeleteCommentSuccessfully() {

        Comment comment = createComment();

        CommentDeleteDTO request = new CommentDeleteDTO();
        request.setUserId(1L);

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(comment));

        assertDoesNotThrow(
                () -> commentService.deleteComment(5L, request)
        );

        verify(commentRepository).delete(comment);
    }


    @Test
    void shouldThrowExceptionWhenDeletingMissingComment() {

        CommentDeleteDTO request = new CommentDeleteDTO();
        request.setUserId(1L);

        when(commentRepository.findById(5L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.deleteComment(5L, request)
        );

        assertEquals("Comment not found!", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenUserDeletesAnotherUsersComment() {

        CommentDeleteDTO request = new CommentDeleteDTO();
        request.setUserId(2L);

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(createComment()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.deleteComment(5L, request)
        );

        assertEquals(
                "You are not allowed to delete this comment!",
                exception.getMessage()
        );

        verify(commentRepository, never()).delete(any(Comment.class));
    }


    @Test
    void shouldUpdateCommentSuccessfully() {

        Comment comment = createComment();

        CommentUpdateDTO request = new CommentUpdateDTO();
        request.setUserId(1L);
        request.setContent("Updated comment");

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(comment));

        when(commentRepository.save(any(Comment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CommentResponseDTO response =
                commentService.updateComment(5L, request);

        assertEquals("Updated comment", response.getContent());
        assertEquals(5L, response.getId());
        assertEquals("Ahmed Mohamed Ali", response.getUserName());
    }


    @Test
    void shouldThrowExceptionWhenUpdatingMissingComment() {

        CommentUpdateDTO request = new CommentUpdateDTO();
        request.setUserId(1L);
        request.setContent("Updated comment");

        when(commentRepository.findById(5L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.updateComment(5L, request)
        );

        assertEquals("Comment not found!", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenUserUpdatesAnotherUsersComment() {

        CommentUpdateDTO request = new CommentUpdateDTO();
        request.setUserId(2L);
        request.setContent("Updated comment");

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(createComment()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.updateComment(5L, request)
        );

        assertEquals(
                "You are not allowed to update this comment!",
                exception.getMessage()
        );
    }


    @Test
    void shouldThrowExceptionWhenUpdatingWithEmptyContent() {

        CommentUpdateDTO request = new CommentUpdateDTO();
        request.setUserId(1L);
        request.setContent("   ");

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(createComment()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.updateComment(5L, request)
        );

        assertEquals("Comment content is required!", exception.getMessage());
    }


    @Test
    void shouldReturnAllCommentsForProjectMember() {

        Comment comment = createComment();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(createUser()));

        when(commentRepository.findAll())
                .thenReturn(List.of(comment));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(true);

        List<CommentResponseDTO> response =
                commentService.getAllComments(1L);

        assertEquals(1, response.size());
        assertEquals("Test comment", response.get(0).getContent());
    }


    @Test
    void shouldReturnEmptyListWhenUserIsNotMemberOfAnyCommentProject() {

        Comment comment = createComment();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(createUser()));

        when(commentRepository.findAll())
                .thenReturn(List.of(comment));

        when(projectMemberRepository.existsByProjectIdAndUserId(2L, 1L))
                .thenReturn(false);

        List<CommentResponseDTO> response =
                commentService.getAllComments(1L);

        assertTrue(response.isEmpty());
    }


    @Test
    void shouldThrowExceptionWhenGettingAllCommentsForMissingUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.getAllComments(1L)
        );

        assertEquals("User not found!", exception.getMessage());
    }
}
