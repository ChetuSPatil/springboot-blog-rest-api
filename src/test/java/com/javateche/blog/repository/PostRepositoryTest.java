package com.javateche.blog.repository;

import com.javateche.blog.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Test
    public void testFindById(){
        Post post = postRepository.findById(1l).get();
        System.out.println(post);

    }

    @Test
    public void testSave(){
        Post post = new Post();
        post.setTitle("test title");
        post.setDescription("test description");
        post.setContent("Some test content");

        Post updated = postRepository.save(post);
        System.out.println(updated);
    }
}
