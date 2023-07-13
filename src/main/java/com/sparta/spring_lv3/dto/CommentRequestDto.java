package com.sparta.spring_lv3.dto;

import com.sparta.spring_lv3.entity.Post;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private Long commentId;
    private String comment;
}
