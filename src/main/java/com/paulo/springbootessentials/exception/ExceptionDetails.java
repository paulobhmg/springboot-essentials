package com.paulo.springbootessentials.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionDetails {
    private String title;
    private int statusCode;
    private String developerMessage;
    private LocalDateTime timestamp;
    private String path;
}
