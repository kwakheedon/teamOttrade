package com.ottrade.ottrade.security.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.service.AuthService;
import com.ottrade.ottrade.security.user.CustomUserDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    // 구글 로그인 후,  로그인 완료 페이지로 이동하면서 필요한 토큰 전달하는 클래스
    
    
    // application.properties에서 프론트엔드 리디렉션 주소를 주입받음
    @Value("${oauth.redirect-uri.frontend}")
    private String frontendRedirectUri;

    @Override  //AuthService 로 우리쪽 전용토큰발급 , 리디렉션할 URL 생성후 리디렉션
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails oAuth2User = (CustomUserDetails) authentication.getPrincipal();

        AuthDto.TokenResponse tokenResponse = authService.issueTokens(oAuth2User.getUser());

        String targetUrl = createRedirectUrl(tokenResponse);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String createRedirectUrl(AuthDto.TokenResponse tokenResponse) {
        return UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("accessToken", tokenResponse.getAccessToken())
                .queryParam("refreshToken", tokenResponse.getRefreshToken())
                .build().toUriString();
    }
}