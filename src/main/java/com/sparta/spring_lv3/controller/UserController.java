package com.sparta.spring_lv3.controller;

import com.sparta.spring_lv3.dto.AuthRequestDto;
import com.sparta.spring_lv3.dto.StatusResponseDto;
import com.sparta.spring_lv3.jwt.JwtUtil;
import com.sparta.spring_lv3.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public StatusResponseDto signup(@Valid @RequestBody AuthRequestDto requestDto) {

        try {
            userService.signup(requestDto);
            return new StatusResponseDto("회원가입이 완료되었습니다.", HttpStatus.CREATED.value());
        } catch (IllegalArgumentException e) {
            return new StatusResponseDto("중복된 회원이 존재합니다.", HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/login")
    public StatusResponseDto login(@RequestBody AuthRequestDto requestDto, HttpServletResponse response) {

        try {
            userService.login(requestDto);
        } catch (IllegalArgumentException e) {
            return new StatusResponseDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value());
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUsername(), requestDto.getRole()));

        return new StatusResponseDto("로그인 완료되었습니다.",HttpStatus.OK.value());
    }

}
