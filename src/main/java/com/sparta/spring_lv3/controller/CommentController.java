package com.sparta.spring_lv3.controller;

import com.sparta.spring_lv3.dto.CommentRequestDto;
import com.sparta.spring_lv3.dto.CommentResponseDto;
import com.sparta.spring_lv3.dto.StatusResponseDto;
import com.sparta.spring_lv3.security.UserDetailsImpl;
import com.sparta.spring_lv3.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.createComment(postId, requestDto, userDetails.getUser());
    }

    @PutMapping("/comment/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId,
                                            @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comment/{commentId}")
    public StatusResponseDto deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {


        commentService.deleteComment(commentId, userDetails.getUser());



        return new StatusResponseDto("댓글 삭제가 완료되었습니다.", HttpStatus.OK.value());
    }
}
