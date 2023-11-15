package com.javateche.blog.service;

import com.javateche.blog.payload.PostDTO;
import com.javateche.blog.payload.PostPageResponse;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    PostPageResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDirection);
    PostDTO getPostById(Long id);
    PostDTO updatePost(PostDTO postDTO, Long id);
    void deletePost(Long id);

    List<PostDTO> getPostsByCategory(Long categoryId);
}
