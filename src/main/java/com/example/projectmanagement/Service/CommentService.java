package com.example.projectmanagement.Service;
import com.example.projectmanagement.DTO.CommentDeleteDTO;
import com.example.projectmanagement.DTO.CommentRequestDTO;
import com.example.projectmanagement.DTO.CommentResponseDTO;
import com.example.projectmanagement.DTO.CommentUpdateDTO;
import com.example.projectmanagement.Model.Comment;
import com.example.projectmanagement.Model.Task;
import com.example.projectmanagement.Model.User;
import com.example.projectmanagement.Repository.CommentRepository;
import com.example.projectmanagement.Repository.ProjectMemberRepository;
import com.example.projectmanagement.Repository.TaskRepository;
import com.example.projectmanagement.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final EntityManager entityManager;

    public CommentResponseDTO addComment(CommentRequestDTO request) {

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found!"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Long projectId = task.getProject().getId();

        boolean isMember = projectMemberRepository
                .existsByProjectIdAndUserId(projectId, user.getId());

        if (!isMember) {
            throw new RuntimeException("User is not a member of this project!");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new RuntimeException("Comment content is required!");
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(user);

        Comment savedComment = commentRepository.saveAndFlush(comment);
        entityManager.refresh(savedComment);

        return new CommentResponseDTO(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getTask().getId(),
                savedComment.getUser().getId(),
                savedComment.getUser().getFullName(),
                savedComment.getCreatedAt()
        );
    }


    public List<CommentResponseDTO> getTaskComments(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found!"));

        boolean isMember = projectMemberRepository
                .existsByProjectIdAndUserId(task.getProject().getId(), userId);

        if (!isMember) {
            throw new RuntimeException("You are not allowed to view comments!");
        }

        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getTask().getId(),
                        comment.getUser().getId(),
                        comment.getUser().getFullName(),
                        comment.getCreatedAt()
                )).toList();
    }


    public CommentResponseDTO getCommentById(Long id, Long userId) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        Long projectId = comment.getTask().getProject().getId();

        boolean isMember = projectMemberRepository
                .existsByProjectIdAndUserId(projectId, userId);

        if (!isMember) {
            throw new RuntimeException("You are not allowed to view this comment!");
        }

        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getUser().getFullName(),
                comment.getCreatedAt()
        );
    }


    public void deleteComment(Long id, CommentDeleteDTO request) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        if (!comment.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("You are not allowed to delete this comment!");
        }

        commentRepository.delete(comment);
    }


    public CommentResponseDTO updateComment(Long id, CommentUpdateDTO request) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));

        if (!comment.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("You are not allowed to update this comment!");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new RuntimeException("Comment content is required!");
        }


        comment.setContent(request.getContent());

        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponseDTO(
                updatedComment.getId(),
                updatedComment.getContent(),
                updatedComment.getTask().getId(),
                updatedComment.getUser().getId(),
                updatedComment.getUser().getFullName(),
                updatedComment.getCreatedAt()
        );
    }
    public List<CommentResponseDTO> getAllComments(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return commentRepository.findAll()
                .stream()
                .filter(comment ->
                        projectMemberRepository.existsByProjectIdAndUserId(
                                comment.getTask().getProject().getId(),
                                userId
                        )
                )
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),
                        comment.getContent(),
                        comment.getTask().getId(),
                        comment.getUser().getId(),
                        comment.getUser().getFullName(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

}