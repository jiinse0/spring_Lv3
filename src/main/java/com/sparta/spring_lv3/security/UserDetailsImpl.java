package com.sparta.spring_lv3.security;

import com.sparta.spring_lv3.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 3. 인증정보 구현체
 * UserDetails 구현체 > UserDetailsImpl
 * 스프링 시큐리티의 UserDetails 인터페이스를 구현한 클래스
 * 사용자의 세부 정보를 나타내는 역할
 * 사용자 이름, 비밀번호, 권한 등의 정보를 저장
 */

public class UserDetailsImpl implements UserDetails { // Authentication 인증 정보를 담기위한 유저 정보 인터페이스

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    // 사용자가 소유한 권한 목록을 반환하는 메서드
    // Spring Security 에서는 사용자의 권한을 이 메서드를 통해 얻는다.
    // 빈 ArrayList를 반환하고 있으므로 사용자에게는 권한이 없는 것으로 처리된다.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    // 사용자 계정의 만료 여부를 반환하는 메서드 (true : 계정 만료되지 않은 것으로 처리)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // 사용자 계정의 잠김 여부를 반환하는 메서드 (true : 인증정보가 만료되지 않은 것으로 처리)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 사용자의 인증(비밀번호)의 만료 여부를 반환하는 메서드 (true : 인증정보가 만료되지 않은 것으로 처리)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // 사용자 계정의 활성화 여부를 반환하는 메서드 (true : 계정이 활성화된 것으로 처리)
    public boolean isEnabled() {
        return true;
    }
}