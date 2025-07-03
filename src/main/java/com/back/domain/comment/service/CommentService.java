package com.back.domain.comment.service;

import com.back.domain.comment.dto.CreateCommentRequestDto;
import com.back.domain.comment.dto.CreateCommentResponseDto;

import java.util.List;

public interface CommentService {
    CreateCommentResponseDto createComment(CreateCommentRequestDto requestDto, long postId);

    List<CreateCommentResponseDto> getComments(long postId);

    CreateCommentResponseDto getComment(long postId, long commentId);
}