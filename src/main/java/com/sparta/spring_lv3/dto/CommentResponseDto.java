package com.sparta.spring_lv3.dto;

import com.sparta.spring_lv3.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long postId;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto (Comment comment) {
        this.postId = comment.getPost().getId();
        this.username = comment.getComment();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreateAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
