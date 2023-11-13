package com.javateche.blog.service;

import com.javateche.blog.payload.CommentDTO;

import java.util.List;

public interface CommentSevice {
    CommentDTO createComment(Long postId, CommentDTO commentDTO);

    List<CommentDTO> getAllCommentsByPostId(Long postId);

    CommentDTO getCommentById(Long postId, Long commentId);

    CommentDTO updateCommentById(Long postId, Long commentId, CommentDTO commentDtoRequest);

    void deleteCommentById(Long postId, Long commentId);
}
