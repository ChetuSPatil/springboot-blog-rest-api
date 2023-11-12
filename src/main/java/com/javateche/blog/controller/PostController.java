package com.javateche.blog.controller;

import com.javateche.blog.entity.Post;
import com.javateche.blog.payload.PostDTO;
import com.javateche.blog.service.PostService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long postId){
       // return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.OK);
        return  ResponseEntity.ok(postService.getPostById(postId));
    }

    //Update Post by id
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable(name = "id") Long postId){
        PostDTO postResponse = postService.updatePost(postDTO, postId);
        return ResponseEntity.ok(postResponse);
    }

    //Delete post rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("Post entity deleted successfuly.", HttpStatus.OK);
    }
}
