package com.back.domain.post.service;

import com.back.domain.post.dto.CreatePostRequestDto;
import com.back.domain.post.dto.CreatePostResponseDto;
import com.back.domain.post.entity.Post;
import com.back.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public CreatePostResponseDto create(CreatePostRequestDto requestDto) {
        // (1) requestDto → Entity 변환
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        // (2) 저장
        Post savedPost = postRepository.save(post);

        // (3) 저장된 엔티티 → 응답 DTO로 변환
        return new CreatePostResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getCreateDate(),
                savedPost.getModifyDate()
        );
    }

    @Override
    public List<CreatePostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new CreatePostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreateDate(),
                        post.getModifyDate()
                ))
                .toList();
    }

    @Override
    public CreatePostResponseDto getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));

        return new CreatePostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreateDate(),
                post.getModifyDate()
        );
    }

    @Override
    public CreatePostResponseDto update(long id, CreatePostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));

        // (1) requestDto → Entity 변환
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());

        // (2) 저장
        Post updatedPost = postRepository.save(post);

        // (3) 저장된 엔티티 → 응답 DTO로 변환
        return new CreatePostResponseDto(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getCreateDate(),
                updatedPost.getModifyDate()
        );
    }

    @Override
    public void delete(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));

        postRepository.delete(post);
    }
}
