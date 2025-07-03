package com.back.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.back.domain.post.dto.CreatePostRequestDto;
import com.back.domain.post.dto.CreatePostResponseDto;
import com.back.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ApiV1PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("t1: 게시글 등록")
    void t1() throws Exception {
        // given
        CreatePostRequestDto request = new CreatePostRequestDto("제목입니다", "내용입니다");

        // when
        ResultActions resultActions = mvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("createPost"))
                .andExpect(jsonPath("$.title").value("제목입니다"))
                .andExpect(jsonPath("$.content").value("내용입니다"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("t2: 게시글 전체 조회")
    void t2() throws Exception {
        // given: 글 2개 등록
        postService.create(new CreatePostRequestDto("제목1", "내용1"));
        postService.create(new CreatePostRequestDto("제목2", "내용2"));

        // when
        ResultActions resultActions = mvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getPosts"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("제목1"))
                .andExpect(jsonPath("$[1].title").value("제목2"));
    }

    @Test
    @DisplayName("t3: 게시글 단건 조회")
    void t3() throws Exception {
        // given: 글 등록 → id 꺼내기
        CreatePostResponseDto created = postService.create(new CreatePostRequestDto("제목", "내용"));

        // when
        ResultActions resultActions = mvc.perform(get("/api/v1/posts/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("getPost"))
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"));
    }

    @Test
    @DisplayName("t4: 게시글 수정 성공")
    void t4_updatePost_success() throws Exception {
        // given
        // 1. 테스트용 게시글 생성 (PostService를 이용)
        CreatePostResponseDto createdPost = postService.create(new CreatePostRequestDto("기존 제목", "기존 내용"));

        // 2. 수정할 내용 DTO 생성
        CreatePostRequestDto updateRequest = new CreatePostRequestDto("수정된 제목", "수정된 내용");

        // when
        // 3. MockMvc를 사용하여 PUT 요청 전송
        ResultActions resultActions = mvc.perform(put("/api/v1/posts/" + createdPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print()); // 요청/응답 내용 출력

        // then
        // 4. 응답 검증
        resultActions
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("updatePost")) // 아직 구현되지 않은 메서드 이름
                .andExpect(jsonPath("$.id").value(createdPost.getId())) // ID는 동일
                .andExpect(jsonPath("$.title").value("수정된 제목")) // 제목이 수정되었는지 확인
                .andExpect(jsonPath("$.content").value("수정된 내용")) // 내용이 수정되었는지 확인
                .andExpect(jsonPath("$.createDate").exists()) // 생성일은 존재
                .andExpect(jsonPath("$.modifyDate").exists()); // 수정일은 존재

        // 추가 검증: 수정된 게시글을 다시 조회하여 내용이 실제로 변경되었는지 확인 (선택 사항이지만 견고한 테스트)


    }

    @Test
    @DisplayName("t5: 존재하지 않는 게시글 수정 시도 시 404 응답")
    void t5_updatePost_notFound() throws Exception {
        // given
        long nonExistentId = 999L; // 존재하지 않을 것으로 예상되는 ID
        CreatePostRequestDto updateRequest = new CreatePostRequestDto("수정된 제목", "수정된 내용");

        // when
        ResultActions resultActions = mvc.perform(put("/api/v1/posts/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isNotFound()); // HTTP 404 Not Found
    }

    @Test
    @DisplayName("t6: 게시글 삭제 성공")
    void t6_deletePost_success() throws Exception {
        // given
        // 1. 테스트용 게시글 생성
        CreatePostResponseDto createdPost = postService.create(new CreatePostRequestDto("삭제할 제목", "삭제할 내용"));

        // when
        // 2. MockMvc를 사용하여 DELETE 요청 전송
        ResultActions resultActions = mvc.perform(delete("/api/v1/posts/" + createdPost.getId()))
                .andDo(print());

        // then
        // 3. 응답 검증
        resultActions
                .andExpect(status().isNoContent()) // HTTP 204 No Content
                .andExpect(handler().handlerType(ApiV1PostController.class))
                .andExpect(handler().methodName("deletePost"));

        // 4. 삭제된 게시글을 다시 조회하여 존재하지 않음을 확인
        // (IllegalArgumentException이 발생하는지 확인)
        try {
            postService.getPostById(createdPost.getId());
            // 예외가 발생하지 않으면 테스트 실패
            throw new AssertionError("게시글이 삭제되지 않았습니다.");
        } catch (IllegalArgumentException e) {
            // 예상대로 예외가 발생했으므로 성공
            assertThat(e.getMessage()).contains("Post not found");
        }
    }

}
