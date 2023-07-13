package com.sparta.spring_lv3.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private Long commentId;
    private String comment;
}
