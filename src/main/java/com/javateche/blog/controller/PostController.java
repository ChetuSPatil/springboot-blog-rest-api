package com.javateche.blog.controller;

import com.javateche.blog.entity.Post;
import com.javateche.blog.payload.PostDTO;
import com.javateche.blog.payload.PostPageResponse;
import com.javateche.blog.service.PostService;
import com.javateche.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService; //here we are injecting the interface not class hence making loose coupling here

    @Autowired // Constructor injection is the best way of autowiring
    public PostController(PostService postService){
        this.postService = postService;
    }

    //create blog post;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PostPageResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDirection
    ){
        return new ResponseEntity<>(postService.getAllPosts(pageNo, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    //Get a Post by Id - http://localhost:8080/api/posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long postId){
       // return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.OK);
        return  ResponseEntity.ok(postService.getPostById(postId));
    }

    //Update Post by id
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestBody PostDTO postDTO, @PathVariable(name = "id") Long postId){
        PostDTO postResponse = postService.updatePost(postDTO, postId);
        return ResponseEntity.ok(postResponse);
    }

    //Delete post rest api
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("Post entity deleted successfuly.", HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDTO>> getPostsByCategoryId(@PathVariable("id") Long categoryId){
        List<PostDTO> postDTOS = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDTOS);
    }
}
