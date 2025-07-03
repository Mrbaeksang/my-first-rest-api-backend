package com.back.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreatePostResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
