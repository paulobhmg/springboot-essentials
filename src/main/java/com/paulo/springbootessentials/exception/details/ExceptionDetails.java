package com.paulo.springbootessentials.exception.details;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {
    protected String title;
    protected String exception;
    protected int status;
    protected LocalDateTime timestamp;
    protected String path;
    protected String details;
}
