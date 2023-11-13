package com.javateche.blog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorDetails {
    //private Date timestamp;
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
