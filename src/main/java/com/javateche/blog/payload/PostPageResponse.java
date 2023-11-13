package com.javateche.blog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostPageResponse {
    private List<PostDTO> content;
    private int pageNo;
    private int pageSize;
    private Long totalRecords;
    private int numberOfRecords;
    private int totalPages;
    private boolean isFirstPage;
    private boolean isLastPage;
}
