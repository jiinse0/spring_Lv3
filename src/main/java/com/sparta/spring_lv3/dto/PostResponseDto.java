package com.sparta.spring_lv3.dto;

import com.sparta.spring_lv3.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreateAt();
        this.modifiedAt = post.getModifiedAt();

        this.commentList.addAll(post.getCommentList()
                .stream()
                .map(CommentResponseDto::new)
                // .sorted() : 스트림 요소를 정렬
                // Comparator.comparing() : 정렬 기준 지정
                //                          CommentResponseDto 객체의 createdAt 속성을 기준으로 정렬
                // .reversed() : 정렬 순서를 반대로 변경 (내림차순)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .toList());
    }
}
