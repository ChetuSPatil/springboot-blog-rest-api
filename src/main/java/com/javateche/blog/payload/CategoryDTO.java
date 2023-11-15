package com.javateche.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    @NotEmpty
    @Size(min = 2, max = 50, message = "Name should 2 to 50 characters")
    private String name;
    @NotEmpty
    private String description;
}
