package com.sparta.spring_lv3.service;

import com.sparta.spring_lv3.dto.PostRequestDto;
import com.sparta.spring_lv3.dto.PostResponseDto;
import com.sparta.spring_lv3.entity.Comment;
import com.sparta.spring_lv3.entity.Post;
import com.sparta.spring_lv3.entity.User;
import com.sparta.spring_lv3.entity.UserRoleEnum;
import com.sparta.spring_lv3.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getPost() {
        return postRepository.findAllByOrderByCreateAtDesc()
                .stream()
                .map(PostResponseDto :: new)
                .toList();
    }

    public PostResponseDto getOnePost(Long id) {
        Post post = findByPost(id);

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findByPost(id);

        postAuthority(user, post);

        post.update(requestDto);

        return new PostResponseDto(post);
    }

    public void deletePost(User user, Long id) {
        Post post = findByPost(id);

        postAuthority(user, post);
    }

    private Post findByPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("등록된 게시글이 없습니다.")
        );
    }

    private void postAuthority(User user, Post post) {
        if (!(post.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
