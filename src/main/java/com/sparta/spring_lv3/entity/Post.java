package com.sparta.spring_lv3.entity;

import com.sparta.spring_lv3.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // @OneToMany : 일대다
    // mappedBy : 연관관계의 주인을 지정
    //            "post"로 설정되어 있기 때문에 Post 엔티티가 연관관계의 주인
    // cascade : 연관된 엔티티에 대한 영속성 작업을 어떻게 처리할지를 설정
    // CascadeType.REMOVE : Post 엔티티가 삭제될 때 연관된 모든 Comment 엔티티도 함께 삭제되도록 설정
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
