package com.sparta.spring_lv3.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spring_lv3.jwt.JwtAuthorizationFilter;
import com.sparta.spring_lv3.jwt.JwtUtil;
import com.sparta.spring_lv3.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

/**
 * 1. 인증설정
 * WebSecurityConfig > jwtUtil > UsernamePasswordAuthenticationFilter > SecurityFilterChain > 요청별 인증수행
 * 웹 애플리케이션의 보안 관련 구성을 담당
 * 비밀번호 암호화, 인증 관리자, JWT 인증 필터, 보안 필터 체인, 로그인 페이지, 로그아웃 처리 등을 설정을 구성하고 빈으로 등록한다.
 */
@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    @Bean
    /*BCryptPasswordEncoder 를 Bean 으로 등록하여 비밀번호를 암호화하는 데 사용*/
    public PasswordEncoder passwordEncoder() {
        /*BCryptPasswordEncoder를  반환하도록 설정되어 있어서, BCrypt 해시 알고리즘을 사용하여 비밀번호를 암호화 한다.*/
        return new BCryptPasswordEncoder();
    }

    @Bean
    /* 인증 관리자 (AuthenticationManager) 빈으로 등록하여 인증에 사용
     AuthenticationConfiguration 을 인자로 받아서 인증 관리자를 반환
     throws Exception을 선언하여 예외 처리를 위임*/
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    // JWT 인증 필터 (JwtAuthorizationFilter) 빈을 생성
    // jwtUtil, userDetailsService, objectMapper를 인자로 받아 JwtAuthorizationFilter 객체를 반환
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper);
    }

    @Bean
    // Spring Security의 보안 필터 체인(SecurityFilterChain) 빈을 생성
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        // csrf : CSRF(Cross-Site Request Forgery) 방어 기능을 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정 (세션 관리 방식 설정)
        // STATELESS로 설정하여 세션을 사용하지 않고 stateless한 JWT 방식을 사용하도록 한다.
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 중요
        // authorizeHttpRequests : Http 요청에 대한 인증과 권한 부여 설정
        http.authorizeHttpRequests((authorizeHttpRequests) -> // Http 요청에 대한 인증을 어떻게 할거냐
                authorizeHttpRequests
                        // requestMatchers() : 특정 요청 매처에 대한 접근 권한 설정을 지정
                        //                     괄호 안의 요청 정보가 조건에 맞는지 확인하고, 맞는다면 permitAll() 수행
                        // permitAll() : requestMatchers() 에 해당하는 요청은 다 승인 해 주겠다. 따로 인증 수행 하지 않겠다.
                        // StaticResources : resources 하위에 있는 application.properties
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/auth/**").permitAll() // '/api/auth/'로 시작하는 요청 모두 접근 허가 (회원가입, 로그인)
                        .requestMatchers(HttpMethod.GET, "/api/post/**").permitAll() // 'GET /api/posts'로 시작하는 요청 모두 접근 허가 (게시글 조회)
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 필터 관리
        // JWT 인증 필터를 적용하여 요청의 인증과 권한 검사를 처리한다.
        // UsernamePasswordAuthenticationFilter 수행하기 전에 jwtAuthorizationFilter를 수행하겠다.
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // HttpSecurity 객체를 반환하고, http.build()를 호출하여 보안 필터 체인(SecurityFilterChain)을 생성한다.
        return http.build();
    }
}