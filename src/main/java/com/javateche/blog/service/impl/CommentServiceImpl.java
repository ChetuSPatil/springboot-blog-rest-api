package com.javateche.blog.service.impl;

import com.javateche.blog.entity.Comment;
import com.javateche.blog.entity.Post;
import com.javateche.blog.exception.BlogAPIException;
import com.javateche.blog.exception.ResourceNotFoundException;
import com.javateche.blog.payload.CommentDTO;
import com.javateche.blog.repository.CommentRepository;
import com.javateche.blog.repository.PostRepository;
import com.javateche.blog.service.CommentSevice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.BadLocationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentSevice {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);

        //retrieve Post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId+""));

        //set post to comment
        comment.setPost(post);

        //save comment entity to database
        Comment newcomment = commentRepository.save(comment);
        return mapToDto(newcomment);
    }

    @Override
    public List<CommentDTO> getAllCommentsByPostId(Long postId){

        //Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId+""));

        //Retrieve comments by post id
        List<Comment> comments = commentRepository.findByPostId(postId);

        List<CommentDTO> commentDTOS = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

        return commentDTOS;
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId){
        /*
        //retrieve post by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId+""));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comments", "id", commentId+""));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        */

        Comment comment = getComment(postId, commentId);

        return mapToDto(comment);
    }

    @Override
    public CommentDTO updateCommentById(Long postId, Long commentId, CommentDTO commentDtoRequest){
        /*
        //retrieve post by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId+""));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comments", "id", commentId+""));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to Post id: "+postId);
        }
        */

        Comment comment = getComment(postId, commentId);

        comment.setName(commentDtoRequest.getName());
        comment.setEmail(commentDtoRequest.getEmail());
        comment.setBody(commentDtoRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId){
        /*
        //retrieve post by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId+""));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comments", "id", commentId+""));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to Post id: "+postId);
        }
        */
        Comment comment = getComment(postId, commentId);
        commentRepository.delete(comment);
    }

    private CommentDTO mapToDto(Comment comment){

        CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
        /*
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setName(comment.getName());
        commentDTO.setEmail(comment.getEmail());
        commentDTO.setBody(comment.getBody());
        */
        return commentDTO;
    }

    private Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = mapper.map(commentDTO, Comment.class);
        /*
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());
        */
        return comment;
    }

    private Comment getComment(Long postId, Long commentId){
        //retrieve post by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId+""));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId+""));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to Post id: "+postId);
        }

        return comment;
    }
}
