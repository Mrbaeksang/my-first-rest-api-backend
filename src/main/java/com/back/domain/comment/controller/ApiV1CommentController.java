package com.back.domain.comment.controller;

import com.back.domain.comment.dto.CreateCommentRequestDto;
import com.back.domain.comment.dto.CreateCommentResponseDto;
import com.back.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class ApiV1CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CreateCommentResponseDto> createComment(
            @PathVariable long postId,
            @RequestBody CreateCommentRequestDto requestDto) {
        CreateCommentResponseDto responseDto = commentService.createComment(requestDto, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CreateCommentResponseDto>> getComments(@PathVariable long postId) {
        List<CreateCommentResponseDto> comments = commentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CreateCommentResponseDto> getComment(
            @PathVariable long postId,
            @PathVariable long commentId) {
        CreateCommentResponseDto comment = commentService.getComment(postId, commentId);
        return ResponseEntity.ok(comment);
    }
}