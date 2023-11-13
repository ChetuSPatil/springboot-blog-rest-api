package com.javateche.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    //Name should not be null or empty
    @NotEmpty(message = "Name should not be null or empty")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    //Email should not be null or empty
    //Email field validation
    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    //Comment body should not be null or empty
    //Comment body must be minimum 10 character
    @NotEmpty
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String body;
}
