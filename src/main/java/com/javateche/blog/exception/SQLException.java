package com.javateche.blog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SQLException extends RuntimeException{
    private String resourceName;
    private String activityType;
    private String errorMessage;



    public SQLException(String resourceName, String activityType, String errorMessage){
        super(String.format("Couldn't %s the %s, Due to following error, %s", activityType, resourceName, errorMessage)+"----");
        this.resourceName = resourceName;
        this.activityType = activityType;
        this.errorMessage = errorMessage;
    }
}
