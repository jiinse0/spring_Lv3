package com.sparta.spring_lv3.jwt;

import com.sparta.spring_lv3.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/*
 * JWT(JSON Web Token)의 생성, 검증 및 해석과 관련된 유틸리티 클래스
 * 토큰을 생성하고 해석하여 사용자를 인증
 * 토큰의 생성, 서명 검증, 페이로드 해석 등을 처리
 */
@Component
@RequiredArgsConstructor
public class JwtUtil { // JWT (JSON Web Token)을 생성하고 검증하는 클래스
    // JwtUtil : JWT 토큰의 생성, 헤더에서 토큰 추출, 토큰의 유효성 검사, 토큰에서 사용자 정보 추출, 토큰 블랙리스트 확인 등의 기능을 제공

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 키값. 사용자 권한도 토큰안에 넣어주기 때문에 그때 사용하는 키값
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    // JWT 서명에 사용되는 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // 인스턴스 생성 및 의존성 주입이 완료된 후에 실행되어야 함
    // JwtUtil 인스턴스 생성 후 secretKey 값을 Base64 디코딩하여 key 를 초기화하는 역할
    @PostConstruct // 초기화 메서드를 나타내는 어노테이션으로, 객체 생성 후 한 번 실행된다. (자동 실행)
    public void init() {
        // Base64로 인코딩된 시크릿 키를 디코딩하여 바이트 배열로 변환
        byte[] bytes = Base64.getDecoder().decode(secretKey);

        // 바이트 배열을 사용하여 Key 객체를 생성
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기 Keys.hmacShaKeyFor(bytes);
    public String resolveToken(HttpServletRequest request) { // 요청정보를 넘겨받아서 헤더 값에서 토큰을 빼온다.
        String bearerToken= request.getHeader(AUTHORIZATION_HEADER); // 요청 헤더에서 Authorization 헤더 값을 가져온다.
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7); // Bearer 접두사를 제외한 토큰 문자열을 반환
        }
        return null;
    }

    // 토큰 생성
    // 주어진 사용자 이름과 권한 정보를 기반으로 JWT 토큰을 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        // 토큰 만료시간 = 60분
        long TOKEN_TIME = 60 * 60 * 1000L;

        // 토큰을 생성하여 반환
        return BEARER_PREFIX +
                Jwts.builder() //  JWT 토큰을 생성하기 위한 빌더 객체를 생성, Jwts : JWT 토큰을 만드는 클래스
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); // 실제 string 형태의 jwt 토큰을 응답
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // 토큰의 위변조, 만료 체크 (공통으로 사용되기 때문에 외워두거나 저장해두자)
            // JWT 토큰을 파싱하고 검증하는 과정을 수행
            // key는 서명 검증에 사용되는 비밀키
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;

            // JWT 토큰의 서명이 유효하지 않거나 위조된 경우 발생하는 예외를 처리
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");

            // JWT 토큰이 만료된 경우 발생하는 예외를 처리
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {

            // 지원되지 않는 JWT 토큰인 경우 발생하는 예외 처리
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {

            // JWT 토큰이 잘못된 경우 발생하는 예외 처리
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }

        // 유효성 검사가 실패한 경우 false를 반환
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    // 추출된 정보는 Claims 객체로 반환
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        // parserBuilder() : JWT 파서 빌더 생성, 파싱과 검증에 사용
        // setSigningKey(key) : 파서에 사용할 서명 키를 설정. 이전에 초기화된 Key 객체인 key를 사용
        // build() : 설정이 완료된 파서를 빌드하여 JwtParser 객체 생성
        // parseClaimsJws(token) : 주어진 토큰을 파싱하여 이러한 구성 요소를 분석
                  /*
                  실제로 호출된 parseClaimsJws(token) 메서드는 다음 작업을 수행
                  1. token 매개변수로 전달된 JWT 토큰을 파싱하여 토큰의 구성 요소를 분석합니다.
                  2. 토큰의 헤더와 페이로드를 추출하고, 서명을 확인합니다.
                  3. 토큰의 서명이 유효한지 확인하고, 만료 여부 등을 검증합니다.
                  4. 검증된 토큰의 클레임(Claims) 정보를 반환합니다.
                  */
        // getBody() : Jws<Claims> 긱체에 본문(Claims) 정보만을 추출하여 Claims 객체로 반환
    }
}