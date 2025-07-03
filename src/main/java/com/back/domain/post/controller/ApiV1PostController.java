package com.back.domain.post.controller;

import com.back.domain.post.dto.CreatePostRequestDto;
import com.back.domain.post.dto.CreatePostResponseDto;
import com.back.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<CreatePostResponseDto> createPost(@RequestBody CreatePostRequestDto requestDto) {
        CreatePostResponseDto responseDto = postService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CreatePostResponseDto>> getPosts() {
        List<CreatePostResponseDto> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatePostResponseDto> getPost(@PathVariable long id) {
        CreatePostResponseDto post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatePostResponseDto> updatePost(
            @PathVariable long id, @RequestBody CreatePostRequestDto requestDto) {
        CreatePostResponseDto responseDto = postService.update(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}