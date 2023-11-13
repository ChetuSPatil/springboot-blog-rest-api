package com.javateche.blog.repository;

import com.javateche.blog.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //its not required because Spring boot jpa provide the SimpleJpaRepository implementation to our repository
@Transactional// its is also not required as SimpleJpaRepository is annotated with @Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
