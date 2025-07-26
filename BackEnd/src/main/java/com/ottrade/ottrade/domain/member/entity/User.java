package com.ottrade.ottrade.domain.member.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String provider;
    private String providerId;
    private String refreshToken;

    @Builder
    public User(Long id, String email, String password, String nickname, String phone, Role role, String provider, String providerId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    
    
    
    // 소셜 로그인 시 사용자 정보 업데이트
    public User updateOAuthInfo(String nickname, String provider, String providerId) {
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        return this;
    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    //리프레시 토큰 삭제 (로그아웃)
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    
    //닉네임 업데이트
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 비밀번호 업데이트 
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
    
    
    
    
}