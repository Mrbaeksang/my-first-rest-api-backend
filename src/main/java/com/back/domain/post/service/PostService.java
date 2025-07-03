package com.back.domain.post.service;

import com.back.domain.post.dto.CreatePostRequestDto;
import com.back.domain.post.dto.CreatePostResponseDto;

import java.util.List;

public interface PostService {
    CreatePostResponseDto create(CreatePostRequestDto requestDto);

    List<CreatePostResponseDto> getPosts();

    CreatePostResponseDto getPostById(long id);

    CreatePostResponseDto update(long id, CreatePostRequestDto requestDto);

    void delete(long id);
}
