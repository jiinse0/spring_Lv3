package com.sparta.spring_lv3.security;

import com.sparta.spring_lv3.entity.User;
import com.sparta.spring_lv3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 2. 인증정보 받아오기
 * UsernamePasswordAuthenticationFilter > UserDetailsService 구현 > loadUserByUsername() > UserDetails > Authentication (createSuccessAuthentication()에서 만들어짐)
 * 스프링 시큐리티의 UserDetailsService 인터페이스를 구현한 클래스
 * 사용자의 인증 정보를 데이터베이스나 다른 소스에서 가져오는 역할을 합니다.
 * 인증 과정에서 사용자의 세부 정보를 검색하여 반환
 */
@Service
@RequiredArgsConstructor
// UserDetailsService 인터페이스를 구현한 사용자 정보 조회 서비스
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    // loadUserByUsername() : 주어진 사용자 이름을 이용하여 데어터 베이스에서 사용자 정보를 조회하고,
    // 해당 정보를 UserDetailsImpl 객체로 변환하여 반환하는 메서드
    // 조회된 사용자 정보가 없을 경우 UsernameNotFoundException 을 발생시킨다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        return new UserDetailsImpl(user);
    }
}