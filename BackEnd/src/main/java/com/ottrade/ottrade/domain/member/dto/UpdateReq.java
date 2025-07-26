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

   
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname; 

    private String currentPassword; // 기존 비밀번호 (암호화된 비밀번호와 비교)

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
             message = "새 비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자로 입력해주세요.")
    private String newPassword; 

    
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
             message = "새 비밀번호 확인은 영문, 숫자, 특수문자를 포함하여 8~20자로 입력해주세요.")
    private String confirmNewPassword; 


    
}