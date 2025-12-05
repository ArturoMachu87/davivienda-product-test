package com.example.product.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author Arturo Machuca
 */

@Data
@Builder
public class ErrorResponse {
    private String message;
    private String code;
    private int status;
    private String path;
    private String technicalMessage;
    private Object errors;
    private LocalDateTime timestamp;
}
