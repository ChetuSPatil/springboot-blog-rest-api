package com.javateche.blog.service.impl;

import com.javateche.blog.entity.Category;
import com.javateche.blog.entity.Post;
import com.javateche.blog.exception.ResourceNotFoundException;
import com.javateche.blog.exception.SQLException;
import com.javateche.blog.payload.PostDTO;
import com.javateche.blog.payload.PostPageResponse;
import com.javateche.blog.repository.CategoryRepository;
import com.javateche.blog.repository.PostRepository;
import com.javateche.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;

    private ModelMapper mapper;

    @Autowired //if a class has only one attribute then we can omit @Autowired annotation
    public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, ModelMapper mapper){
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        //Get category by id
        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()+""));

        //Convert PostDTO to Post entity
        Post post = mapToEntity(postDTO);
        post.setCategory(category);

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
    public PostPageResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDirection) {

        //List<Post> posts = postRepository.findAll();

        //Creating sort object
        //Sort sort = Sort.by(sortBy).ascending();
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post>  postPage = postRepository.findAll(pageable);

        //get content from page object
        List<Post> posts = postPage.getContent();

        //List<PostDTO> postDTOS = posts.stream().map(post -> new PostDTO(post.getId(), post.getTitle(), post.getDescription(), post.getContent())).collect(Collectors.toList());
        List<PostDTO> postDTOS1 = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        //List<PostDTO> postDTOS2 = posts.stream().map(new PostServiceImpl(postRepository) :: mapToDto).collect(Collectors.toList());

        PostPageResponse postPageResponse = new PostPageResponse();
        postPageResponse.setContent(postDTOS1);
        postPageResponse.setPageNo(postPage.getNumber());
        postPageResponse.setFirstPage(postPage.isFirst());
        postPageResponse.setLastPage(postPage.isLast());
        postPageResponse.setNumberOfRecords(postPage.getNumberOfElements());
        postPageResponse.setTotalRecords(postPage.getTotalElements());
        postPageResponse.setPageSize(postPage.getSize());
        postPageResponse.setTotalPages(postPage.getTotalPages());

        return postPageResponse;
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

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()+""));

        //set the update values
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        post.setCategory(category);

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

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId+""));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map(post -> mapper.map(post, PostDTO.class)).collect(Collectors.toList());
    }

    //Convert entity into dto
    private PostDTO mapToDto(Post post){

        PostDTO postDTO = mapper.map(post, PostDTO.class);

        /*
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setContent(post.getContent());
        */

        return postDTO;
    }

    //Convert dto to entity
    private Post mapToEntity(PostDTO postDTO){

        Post post = mapper.map(postDTO, Post.class);

        /*
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        */

        return post;
    }
}
