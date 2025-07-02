package com.ottrade.ottrade.domain.member.service;


import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 기능이므로 readOnly = true 설정
public class UserService {

    
    public AuthDto.UserInfoResponse getMyInfo(CustomUserDetails userDetails) {

        return AuthDto.UserInfoResponse.fromEntity(userDetails.getUser());
    }
}
