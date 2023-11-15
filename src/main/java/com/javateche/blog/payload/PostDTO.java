package com.javateche.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    //title should not be null or empty
    //title should have atleast 2 characters
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 chars")
    private String title;

    //Post description should be not null or empty
    //Post description should have at least 10 characters
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;

    //Post content should not be null or empty
    @NotEmpty
    private String content;
    private Set<CommentDTO> comments = new HashSet<>();

    private Long categoryId;
}
