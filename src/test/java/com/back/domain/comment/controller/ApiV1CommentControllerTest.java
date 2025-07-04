package com.back.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.back.domain.post.dto.CreatePostRequestDto;
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

import com.back.domain.comment.dto.CreateCommentRequestDto;
import com.back.domain.comment.dto.CreateCommentResponseDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ApiV1CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("t7: 댓글 생성 성공")
    void t7_createComment_success() throws Exception {
        // given
        // 1. 댓글을 달 게시글 생성
        long postId = postService.create(new CreatePostRequestDto("게시글 제목", "게시글 내용")).getId();

        // 2. 생성할 댓글 요청 DTO (아직 Comment 관련 DTO가 없으므로 임시로 Map 사용)
        String commentContent = "새로운 댓글 내용";
        String requestBody = String.format("{\"content\": \"%s\"}", commentContent);

        // when
        // 3. MockMvc를 사용하여 POST 요청 전송
        ResultActions resultActions = mvc.perform(post("/api/v1/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print());

        // then
        // 4. 응답 검증
        resultActions
                .andExpect(status().isCreated()) // HTTP 201 Created
                .andExpect(handler().handlerType(ApiV1CommentController.class)) // 아직 구현되지 않은 컨트롤러
                .andExpect(handler().methodName("createComment")) // 아직 구현되지 않은 메서드
                .andExpect(jsonPath("$.content").value(commentContent))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postId").value(postId));
    // then
        // 4. 응답 검증
        resultActions
                .andExpect(status().isCreated()) // HTTP 201 Created
                .andExpect(handler().handlerType(ApiV1CommentController.class)) // 아직 구현되지 않은 컨트롤러
                .andExpect(handler().methodName("createComment")) // 아직 구현되지 않은 메서드
                .andExpect(jsonPath("$.content").value(commentContent))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postId").value(postId));
    }

    @Test
    @DisplayName("t8: 게시글 댓글 목록 조회 성공")
    void t8_getComments_success() throws Exception {
        // given
        // 1. 댓글을 달 게시글 생성
        long postId = postService.create(new CreatePostRequestDto("댓글 목록 조회용 게시글", "내용")).getId();

        // 2. 댓글 여러 개 생성 (postId에 속하는 댓글)
        String commentContent1 = "첫 번째 댓글";
        String commentContent2 = "두 번째 댓글";
        String commentContent3 = "세 번째 댓글";

        mvc.perform(post("/api/v1/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentRequestDto(commentContent1))))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/v1/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentRequestDto(commentContent2))))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/v1/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentRequestDto(commentContent3))))
                .andExpect(status().isCreated());

        // 3. 다른 게시글 생성 및 해당 게시글에 댓글 추가
        long anotherPostId = postService.create(new CreatePostRequestDto("다른 게시글", "다른 내용")).getId();
        mvc.perform(post("/api/v1/posts/" + anotherPostId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentRequestDto("다른 게시글의 댓글"))))
                .andExpect(status().isCreated());

        // when
        // 3. MockMvc를 사용하여 GET 요청 전송
        ResultActions resultActions = mvc.perform(get("/api/v1/posts/" + postId + "/comments"))
                .andDo(print());

        // then
        // 4. 응답 검증
        resultActions
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("getComments")) // 예상되는 메서드 이름
                .andExpect(jsonPath("$").isArray()) // 응답이 배열인지 확인
                .andExpect(jsonPath("$.length()").value(3)) // 댓글이 3개인지 확인
                .andExpect(jsonPath("$[0].content").value(commentContent1))
                .andExpect(jsonPath("$[1].content").value(commentContent2))
                .andExpect(jsonPath("$[2].content").value(commentContent3))
                .andExpect(jsonPath("$[0].postId").value(postId));
    }

    @Test
    @DisplayName("t9: 특정 댓글 조회 성공")
    void t9_getComment_success() throws Exception {
        // given
        // 1. 테스트용 게시글 생성
        long postId = postService.create(new CreatePostRequestDto("특정 댓글 조회용 게시글", "내용")).getId();

        // 2. 댓글 생성 및 ID 획득
        String commentContent = "조회할 댓글 내용";
        ResultActions createResult = mvc.perform(post("/api/v1/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentRequestDto(commentContent))))
                .andExpect(status().isCreated());

        long commentId = objectMapper.readValue(createResult.andReturn().getResponse().getContentAsString(), CreateCommentResponseDto.class).getId();

        // when
        // 3. MockMvc를 사용하여 GET 요청 전송
        ResultActions resultActions = mvc.perform(get("/api/v1/posts/" + postId + "/comments/" + commentId))
                .andDo(print());

        // then
        // 4. 응답 검증
        resultActions
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("getComment")) // 예상되는 메서드 이름
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.content").value(commentContent))
                .andExpect(jsonPath("$.postId").value(postId));
    }

    @Test
    @DisplayName("t10: 댓글 수정 성공")
    void t10_modifyComment_success() throws Exception {
        // given
        // 1. 테스트용 게시글 생성
        long postId = postService.create(new CreatePostRequestDto("댓글 수정용 게시글", "내용")).getId();

        // 2. 댓글 생성 및 ID 획득
        String originalContent = "수정 전 댓글 내용";
        ResultActions createResult = mvc.perform(post("/api/v1/posts/" + postId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentRequestDto(originalContent))))
                .andExpect(status().isCreated());

        long commentId = objectMapper.readValue(createResult.andReturn().getResponse().getContentAsString(), CreateCommentResponseDto.class).getId();

        // 3. 수정할 내용 준비
        String modifiedContent = "수정된 댓글 내용";
        String requestBody = String.format("{\"content\": \"%s\"}", modifiedContent);

        // when
        // 4. MockMvc를 사용하여 PUT 요청 전송
        ResultActions resultActions = mvc.perform(put("/api/v1/posts/" + postId + "/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print());

        // then
        // 5. 응답 검증
        resultActions
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("modifyComment")) // 예상되는 메서드 이름
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.content").value(modifiedContent))
                .andExpect(jsonPath("$.postId").value(postId));
    }
}