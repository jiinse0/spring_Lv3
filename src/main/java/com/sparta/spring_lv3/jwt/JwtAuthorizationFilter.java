package com.sparta.spring_lv3.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spring_lv3.dto.StatusResponseDto;
import com.sparta.spring_lv3.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * JWT 토큰을 사용하여 인증 및 권한 부여를 수행하는 필터
 * 요청의 인증 헤더를 검사하고, 토큰을 검증하여 사용자를 인증하고 권한을 확인
 * 필터 체인에서 이 필터를 등록하여 인증 및 권한 부여를 처리
 * 클라이언트로 부터 요청을 받아들이고, 토큰을 검증한 후 인증 처리를 수행한 뒤 다음 필터로 요청을 전달
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter { // OncePerRequestFilter : 요청 하나당 한번씩 수행하는 필터

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper; // objectMapper : JSON 직렬화 및 역직력화를 위한 인스턴스

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    // 로그인 토큰없이 시도 시 로그인 안내 메서드
    // 요청을 필터링하고 JWT 의 유효성을 검사하는 메서드 (토큰의 존재 여부, 유효성 검사, 블랙리스트 확인을 수행)
    @Override
    // 실제 필터링 작업을 수행하는 메서드
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청에서 토큰 추출
        // resolveToken 메서드를 통해 요청정보 헤더에서 토큰정보를 빼온다.
        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            if(!jwtUtil.validateToken(token)){ // 유효하지 않은 토큰인 경우 조건문 내부 코드 실행
                // 유효하지 않은 토큰에 대한 응답 데이터 생성
                StatusResponseDto responseDto = new StatusResponseDto("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value());
                // 응답의 HTTP 상태 코드 설정, 이 경우 400으로 설정
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                // 응답의 컨텐츠 타입을 설정, JSON 형식의 데이터를 반환하고, 문자 인코딩은 UTF-8로 설정
                response.setContentType("application/json; charset=UTF-8");
                // 응답의 PrintWriter를 사용해 responseDto 객체를 JSON 형식으로 직렬화하여 응답 본문에 작성
                response.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return;
            }

            // 유효한 토큰인 경우, swtUtil 객체를 사용하여 토큰에서 사용자 정보를 추출
            // 추출된 정보는 Claims 객체에 저장
            Claims info = jwtUtil.getUserInfoFromToken(token);

            // 추출된 사용자명을 인자로 햐여 setAuthentication 메서드 호출
            // 이를 통해 인증 처리가 수행되고, 사용자 정보가 인증 객체에 설정
            setAuthentication(info.getSubject());
        }

        filterChain.doFilter(request, response);
    }

    // 사용자명에 대한 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext(); // 빈 SecurityContext 생성
        Authentication authentication = createAuthentication(username); // 주어진 사용자명으로부터 Authentication 객체 생성
        context.setAuthentication(authentication);
        // username -> user 조회 -> userDetails 에 담고 -> authentication의 principal 에 담고
        // -> securityContent 에 담고 -> SecurityContextHolder 에 담고
        // -> 이제 @AuthenticationPrincipal 로 조회할 수 있음
        SecurityContextHolder.setContext(context);
    }

    // 주어진 사용자명으로부터 Authentication 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 사용자 정보 로드
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 인증객체 반환
    }
}