package com.javateche.blog.service.impl;

import com.javateche.blog.entity.Post;
import com.javateche.blog.exception.ResourceNotFoundException;
import com.javateche.blog.exception.SQLException;
import com.javateche.blog.payload.PostDTO;
import com.javateche.blog.repository.PostRepository;
import com.javateche.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired //if a class has only one attribute then we can omit @Autowired annotation
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        //Convert PostDTO to Post entity
        Post post = mapToEntity(postDTO);
        Post newPost = null;
        try{
            newPost = postRepository.save(post);
        }catch (Exception e){
            throw new SQLException("Post", "Create", e.getMessage());
        }
        //Convert Post Entity to PostDTO
        PostDTO postResponse = mapToDto(newPost);

        return postResponse;
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postDTOS = posts.stream().map(post -> new PostDTO(post.getId(), post.getTitle(), post.getDescription(), post.getContent())).collect(Collectors.toList());
        List<PostDTO> postDTOS1 = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        List<PostDTO> postDTOS2 = posts.stream().map(new PostServiceImpl(postRepository) :: mapToDto).collect(Collectors.toList());
        return postDTOS1;
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));
        return mapToDto(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id) {
        //get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

        //set the update values
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        //save the updated post
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        //get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));
        postRepository.delete(post);
    }

    //Convert entity into dto
    private PostDTO mapToDto(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setContent(post.getContent());

        return postDTO;
    }

    //Convert dto to entity
    private Post mapToEntity(PostDTO postDTO){
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());

        return post;
    }
}
