package com.example.projectmanagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateDTO {
    private String content;
    private Long userId;
}
