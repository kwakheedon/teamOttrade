package com.ottrade.ottrade.domain.member.dto;


import com.ottrade.ottrade.domain.member.entity.User; 
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// 회원 정보 수정 응답 DTO
public class UpdateRes {
    private Long id;             
    private String email;        
    private String nickname;     
    private boolean passwordChanged; 
    private String message;      

    
    public static UpdateRes fromEntity(User user, boolean passwordChanged, String message) {
        return UpdateRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .passwordChanged(passwordChanged)
                .message(message)
                .build();
    }
}