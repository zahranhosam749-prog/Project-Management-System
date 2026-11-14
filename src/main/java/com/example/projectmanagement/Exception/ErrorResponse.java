package com.example.projectmanagement.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter

public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;
}
