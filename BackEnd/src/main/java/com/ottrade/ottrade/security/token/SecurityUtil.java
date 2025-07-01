package com.ottrade.ottrade.security.token;

import com.ottrade.ottrade.security.user.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



//Spring Security 관련 유틸리티 메소드를 제공하는 클래스.
public class SecurityUtil {

    // 이 클래스는 인스턴스화할 수 없음
    private SecurityUtil() { }


    public static Long getCurrentUserId() {
        // 1. SecurityContext에서 Authentication 객체를 가져옵니다.
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. 인증 정보가 없으면 예외를 발생시킵니다.
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("Security Context에 유효한 인증 정보가 없습니다.");
        }

        // 3. Principal 객체를 CustomUserDetails로 캐스팅하여 User ID를 반환합니다.
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser().getId();
    }
}