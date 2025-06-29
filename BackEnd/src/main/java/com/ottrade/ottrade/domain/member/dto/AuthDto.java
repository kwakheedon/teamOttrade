package com.ottrade.ottrade.domain.member.dto;

import com.ottrade.ottrade.domain.member.entity.Role;
import com.ottrade.ottrade.domain.member.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthDto {
 
	// DTO 통합 
	
    @Getter
    @NoArgsConstructor
    public static class SignUpRequest {
        private String email;
        private String password;
        private String nickname;
        private String phone;

        public User toEntity(PasswordEncoder passwordEncoder) {
            return User.builder()
                    .email(this.email)
                    .password(passwordEncoder.encode(this.password))
                    .nickname(this.nickname)
                    .phone(this.phone)
                    .role(Role.user)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
    	private String phone;
        private String password;
    }
    
    @Getter
    @NoArgsConstructor
    public static class ReissueRequest {
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
    }
    
    @Getter
    @Builder
    public static class UserInfoResponse {
        private Integer id;
        private String email;
        private String nickname;
        private String phone;
        private Role role;

        public static UserInfoResponse fromEntity(User user) {
            return UserInfoResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .phone(user.getPhone())
                    .role(user.getRole())
                    .build();
        }
    }
}