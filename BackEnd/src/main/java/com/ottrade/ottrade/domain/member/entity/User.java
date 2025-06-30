package com.ottrade.ottrade.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID 타입은 Long을 권장합니다.

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

    // 리프레시 토큰 저장
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
}