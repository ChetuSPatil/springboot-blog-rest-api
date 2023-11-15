package com.javateche.blog.repository;

import com.javateche.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository - not required, because Jpa provide the SimpleJpaRepository class as the default implementation
//SimpleJpaRepository is the default implementation class of the JpaRepository interface, jpa provides this implementation to our repository
//SimpleJpaRepository is also annotated with @transactional so we dont need to annotate our service class with @Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryId(Long categoryId);
}
