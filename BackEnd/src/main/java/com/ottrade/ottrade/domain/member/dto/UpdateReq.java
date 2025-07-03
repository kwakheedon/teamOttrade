package com.ottrade.ottrade.domain.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

// 회원 정보 수정 요청 DTO
public class UpdateReq {

    // 닉네임 필드 (선택 사항이며, 입력 시 유효성 검증)// 닉네임 중복 체크는 서비스 계층에서 별도로 처리됩니다.
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname; 

    // 비밀번호 수정 관련 필드 (모두 입력될 경우 비밀번호 변경 로직 수행)
    // 현재 비밀번호, 새 비밀번호, 새 비밀번호 확인 필드는 모두 같이 들어와야 합니다.
    // 개별 필드에 @NotBlank를 넣지 않고, 서비스 로직에서 3개 필드가 모두 존재하는지 확인합니다.
    private String currentPassword; // 기존 비밀번호 (암호화된 비밀번호와 비교)

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
             message = "새 비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자로 입력해주세요.")
    private String newPassword; // 새로운 비밀번호

    
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
             message = "새 비밀번호 확인은 영문, 숫자, 특수문자를 포함하여 8~20자로 입력해주세요.")
    private String confirmNewPassword; // 새로운 비밀번호 확인 (newPassword와 일치하는지 검증)


    
}