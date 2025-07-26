package com.ottrade.ottrade.security.user;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ottrade.ottrade.domain.member.entity.Role;
import com.ottrade.ottrade.domain.member.entity.User;
import com.ottrade.ottrade.domain.member.repository.UserRepository;
import com.ottrade.ottrade.security.user.oauth.GoogleUserInfo;
//import com.ottrade.ottrade.security.user.oauth.KakaoUserInfo; // KakaoUserInfo 임포트
import com.ottrade.ottrade.security.user.oauth.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	
	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 소셜 서비스 제공자(Google, Kakao 등)에 따라 정보를 분기 처리
		OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(userRequest, oAuth2User.getAttributes());

		User user = findOrCreateUser(oAuth2UserInfo);

		// Spring Security가 관리할 수 있는 형태로 반환
		return new CustomUserDetails(user, oAuth2User.getAttributes());
	}

	// 소셜 서비스 제공자에 따라 적절한 OAuth2UserInfo 객체를 생성하는 메소드
	private OAuth2UserInfo getOAuth2UserInfo(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		if (registrationId.equals("google")) {
			return new GoogleUserInfo(attributes);
		}
//        } else if (registrationId.equals("kakao")) {
//            return new KakaoUserInfo(attributes);
//        }
// 			향후 Naver,zkzk 등 다른 제공자 추가 시 여기에 'else if'를 추가

		throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
	}

	// DB에서 유저 정보를 찾고, 없다면 새로 생성하는 메소드
	private User findOrCreateUser(OAuth2UserInfo oAuth2UserInfo) {
		String email = oAuth2UserInfo.getEmail();

		return userRepository.findByEmail(email)
				.map(existingUser -> existingUser.updateOAuthInfo(oAuth2UserInfo.getName(),
						oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId()))
				.orElseGet(() -> createNewUser(oAuth2UserInfo));
	}

	// 새로운 유저를 생성하는 메소드
	private User createNewUser(OAuth2UserInfo oAuth2UserInfo) {
		User newUser = User.builder().email(oAuth2UserInfo.getEmail()).nickname(oAuth2UserInfo.getName())
				.provider(oAuth2UserInfo.getProvider()).providerId(oAuth2UserInfo.getProviderId())
				.password(UUID.randomUUID().toString()) 
				.role(Role.user) 
				.build();
		return userRepository.save(newUser);
	}
}
