package com.back.domain.comment.service;

import com.back.domain.comment.dto.CreateCommentRequestDto;
import com.back.domain.comment.dto.CreateCommentResponseDto;
import com.back.domain.comment.entity.Comment;
import com.back.domain.comment.repository.CommentRepository;
import com.back.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public CreateCommentResponseDto createComment(CreateCommentRequestDto requestDto, long postId) {
        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .post(postRepository.findById(postId)
                        .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId)))
                .build();

        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDto(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreateDate(),
                savedComment.getModifyDate(),
                savedComment.getPost().getId()
        );
    }

    @Override
    public List<CreateCommentResponseDto> getComments(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> new CreateCommentResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreateDate(),
                        comment.getModifyDate(),
                        comment.getPost().getId()
                ))
                .toList();

    }

    @Override
    public CreateCommentResponseDto getComment(long postId, long commentId) {
        CreateCommentResponseDto comment =  commentRepository.findByIdAndPostId(commentId, postId)
                .map(c -> new CreateCommentResponseDto(
                        c.getId(),
                        c.getContent(),
                        c.getCreateDate(),
                        c.getModifyDate(),
                        c.getPost().getId()
                ))
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));
        return comment;
    }

    @Override
    public CreateCommentResponseDto modifyComment(long postId, long commentId, CreateCommentRequestDto requestDto) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));

        comment.setContent(requestDto.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return new CreateCommentResponseDto(
                updatedComment.getId(),
                updatedComment.getContent(),
                updatedComment.getCreateDate(),
                updatedComment.getModifyDate(),
                updatedComment.getPost().getId()
        );
    }
}