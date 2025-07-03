package com.ottrade.ottrade.domain.member.dto;


import com.ottrade.ottrade.domain.member.entity.User; // User 엔티티 임포트
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
    private Long id;             // 수정된 회원의 고유 ID
    private String email;        // 회원의 이메일 (변경 불가능한 필드로 가정)
    private String nickname;     // 변경된 닉네임 (null일 수 있음, 닉네임 변경 없었을 경우)
    private boolean passwordChanged; // 비밀번호가 성공적으로 변경되었는지 여부
    private String message;      // 응답 메시지 (예: "회원 정보가 성공적으로 수정되었습니다.")

    // User 엔티티로부터 MemberUpdateResponse DTO를 생성하는 팩토리 메소드
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