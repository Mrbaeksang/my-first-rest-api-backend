package com.back.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentResponseDto {
    private long id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long postId;
}
