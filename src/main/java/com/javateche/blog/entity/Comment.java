package com.javateche.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_comments_id")
    private Long id;
    @Column(name = "name")
    private String name;
    private String email;

    @Column(length = 1000)
    private String body;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_post_id", nullable = false)//referencedColumnName is optional, but value should be actual table name not entity attribute name //referencedColumnName = "pk_post_id",
    private Post post;
}
