package com.ottrade.ottrade.security.user;

import com.ottrade.ottrade.domain.member.entity.Role;
import com.ottrade.ottrade.domain.member.entity.User;
import com.ottrade.ottrade.domain.member.repository.UserRepository;
import com.ottrade.ottrade.security.user.oauth.GoogleUserInfo;
import com.ottrade.ottrade.security.user.oauth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // TODO: 다른 OAuth2 제공자(네이버, 카카오) 추가 시 분기 처리 필요
        OAuth2UserInfo oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        String email = oAuth2UserInfo.getEmail();

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.updateOAuthInfo(oAuth2UserInfo.getName(), oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId()))
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .nickname(oAuth2UserInfo.getName())
                            .provider(oAuth2UserInfo.getProvider())
                            .providerId(oAuth2UserInfo.getProviderId())
                            .role(Role.user)
                            .build();
                    return userRepository.save(newUser);
                });

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
}