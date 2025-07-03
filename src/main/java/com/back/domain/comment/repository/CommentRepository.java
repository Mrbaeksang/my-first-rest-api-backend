package com.back.domain.comment.repository;

import com.back.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(long postId);
    Optional<Comment> findByIdAndPostId(long commentId, long postId);
}
