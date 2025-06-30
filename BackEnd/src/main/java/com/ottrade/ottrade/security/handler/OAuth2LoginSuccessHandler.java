package com.ottrade.ottrade.security.handler;

import com.ottrade.ottrade.domain.member.dto.AuthDto;
import com.ottrade.ottrade.domain.member.service.AuthService;
import com.ottrade.ottrade.security.user.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails oAuth2User = (CustomUserDetails) authentication.getPrincipal();

        // AuthService를 통해 토큰 발급
        AuthDto.TokenResponse tokenResponse = authService.issueTokens(oAuth2User.getUser());

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/callback") // 프론트엔드 주소
                .queryParam("accessToken", tokenResponse.getAccessToken())
                .queryParam("refreshToken", tokenResponse.getRefreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
