package com.example.projectmanagement.Controller;
import com.example.projectmanagement.DTO.CommentDeleteDTO;
import com.example.projectmanagement.DTO.CommentRequestDTO;
import com.example.projectmanagement.DTO.CommentResponseDTO;
import com.example.projectmanagement.DTO.CommentUpdateDTO;
import com.example.projectmanagement.Service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(
            @RequestBody CommentRequestDTO request) {

        CommentResponseDTO response =
                commentService.addComment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponseDTO>> getTaskComments(
            @PathVariable Long taskId,
            @RequestParam Long userId) {

        List<CommentResponseDTO> response =
                commentService.getTaskComments(taskId, userId);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(
            @PathVariable Long id,
            @RequestParam Long userId) {

        CommentResponseDTO response =
                commentService.getCommentById(id, userId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @RequestBody CommentDeleteDTO request) {

        commentService.deleteComment(id, request);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long id,
            @RequestBody CommentUpdateDTO request) {

        CommentResponseDTO response =
                commentService.updateComment(id, request);

        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllComments(
            @RequestParam Long userId) {

        List<CommentResponseDTO> response =
                commentService.getAllComments(userId);

        return ResponseEntity.ok(response);
    }
}