package com.sparta.spring_lv3.service;

import com.sparta.spring_lv3.dto.AuthRequestDto;
import com.sparta.spring_lv3.entity.User;
import com.sparta.spring_lv3.entity.UserRoleEnum;
import com.sparta.spring_lv3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        String adminToken = requestDto.getAdminToken();
        UserRoleEnum role = UserRoleEnum.USER;

        if (!requestDto.getRole().equals(role)) {
            if (!adminToken.equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("유효한 관리자 토큰이 아닙니다.");
            } role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);

        userRepository.save(user);
    }

    public void login(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
        );

        if (passwordEncoder.matches(requestDto.getPassword(), password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        }
    }
}
