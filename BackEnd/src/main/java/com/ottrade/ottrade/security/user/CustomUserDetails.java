package com.ottrade.ottrade.security.user;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ottrade.ottrade.domain.member.entity.User;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final User user;
    private Map<String, Object> attributes;
    private static final long serialVersionUID = 1L;
    // DB에서 데이터를 Security가 이해할수있게 신분증 생성하는 클래스  
    
    // 일반 로그인 생성자
    public CustomUserDetails(User user) {
        this.user = user;
    }

    // OAuth2 로그인 생성자
    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    
    // 리턴할 역할(신분증) 부여 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
    }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getEmail(); } //email 사용
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return String.valueOf(user.getId()); }
    @Override public Map<String, Object> getAttributes() { return attributes; }
}