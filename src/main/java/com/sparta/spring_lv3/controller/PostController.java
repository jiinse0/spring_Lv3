package com.sparta.spring_lv3.controller;

import com.sparta.spring_lv3.dto.PostRequestDto;
import com.sparta.spring_lv3.dto.PostResponseDto;
import com.sparta.spring_lv3.dto.StatusResponseDto;
import com.sparta.spring_lv3.entity.Post;
import com.sparta.spring_lv3.security.UserDetailsImpl;
import com.sparta.spring_lv3.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private PostService postService;

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.createPost(requestDto, userDetails.getUser());
    }

    @GetMapping("/post")
    public List<PostResponseDto> getPost() {

        return postService.getPost();
    }

    @GetMapping("/post/{id}")
    public PostResponseDto  getOnePost(@PathVariable Long id) {
        return postService.getOnePost(id);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

    return postService.updatePost(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/{id}")
    public StatusResponseDto deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {

        try {
            postService.deletePost(userDetails.getUser(), id);
            return new StatusResponseDto("게시글 삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST.value());
        } catch (IllegalArgumentException e) {
            return new StatusResponseDto("게시글 삭제가 완료되었습니다.", HttpStatus.OK.value());
        }
    }
}
