package com.ikem.blog.blogrestapi.repository;

import com.ikem.blog.blogrestapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(long postId);
    Optional<Comment> findCommentById(long commentId);
}
