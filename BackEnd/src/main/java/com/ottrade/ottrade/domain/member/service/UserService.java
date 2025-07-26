package com.ottrade.ottrade.domain.member.service;


import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.dto.UpdateReq;
import com.ottrade.ottrade.domain.member.dto.UpdateRes;
import com.ottrade.ottrade.domain.member.entity.User;
import com.ottrade.ottrade.domain.member.repository.UserRepository;
import com.ottrade.ottrade.global.exception.CustomException;
import com.ottrade.ottrade.global.exception.ErrorCode;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) 
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
    //마이페이지 조회
	public AuthDto.UserInfoResponse getMyInfo(CustomUserDetails userDetails) {
		User user = userRepository.findById(userDetails.getUser().getId())

		.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return AuthDto.UserInfoResponse.fromEntity(user);

		}
	
    
    //마이페이지 수정
	@Transactional
    public  UpdateRes updateMyInfo (Long userId,  UpdateReq updateReq ) {
        
		  User user = userRepository.findById(userId)
	                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

	        boolean isNicknameChanged = false; 
	        boolean isPasswordChanged = false; 
	        StringBuilder messageBuilder = new StringBuilder(); 

	        // 닉네임 수정 로직
	        if (updateReq.getNickname() != null && !updateReq.getNickname().isEmpty()) {
	            if (!updateReq.getNickname().equals(user.getNickname())) {
	                if (userRepository.existsByNickname(updateReq.getNickname())) {
	                    throw new CustomException(ErrorCode.DUPLICATE_NICKNAME, "이미 사용 중인 닉네임입니다.");
	                }
	                user.updateNickname(updateReq.getNickname());
	                isNicknameChanged = true;
	                messageBuilder.append("닉네임이 성공적으로 변경되었습니다.");
	            }
	        }

	        // 비밀번호 수정 로직
	        if (updateReq.getCurrentPassword() != null && !updateReq.getCurrentPassword().isEmpty() &&
	        	updateReq.getNewPassword() != null && !updateReq.getNewPassword().isEmpty() &&
	        	updateReq.getConfirmNewPassword() != null && !updateReq.getConfirmNewPassword().isEmpty()) {
	            
	            if (!passwordEncoder.matches(updateReq.getCurrentPassword(), user.getPassword())) {
	                throw new CustomException(ErrorCode.WRONG_PASSWORD, "현재 비밀번호가 일치하지 않습니다.");
	            }

	            if (!updateReq.getNewPassword().equals(updateReq.getConfirmNewPassword())) {
	                throw new CustomException(ErrorCode.PASSWORD_MISMATCH, "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
	            }
	            
	            if (passwordEncoder.matches(updateReq.getNewPassword(), user.getPassword())) {
	                throw new CustomException(ErrorCode.SAME_AS_CURRENT_PASSWORD, "새 비밀번호는 현재 비밀번호와 다르게 설정해야 합니다.");
	            }

	            user.updatePassword(passwordEncoder.encode(updateReq.getNewPassword()));
	            isPasswordChanged = true;
	            if (messageBuilder.length() > 0) { // 닉네임도 변경되었을 경우
	                messageBuilder.append(" 및 ");
	            }
	            messageBuilder.append("비밀번호가 성공적으로 변경되었습니다.");
	        }

	        // 변경된 내용이 전혀 없을 경우 예외 처리
	        if (!isNicknameChanged && !isPasswordChanged) {
	            throw new CustomException(ErrorCode.NO_CHANGES_DETECTED, "변경할 정보가 없습니다.");
	        }

	        // 업데이트된 사용자 정보를 바탕으로 UpdateRes DTO 생성 및 반환
	        String finalMessage = messageBuilder.toString();
	        if (finalMessage.isEmpty()) { // 혹시 메시지가 비어있을 경우 기본 메시지 설정
	             finalMessage = "회원 정보가 성공적으로 수정되었습니다.";
	        }
	        return UpdateRes.fromEntity(user, isPasswordChanged, finalMessage);
	    }
	}
