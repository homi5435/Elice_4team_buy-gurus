package com.team04.buy_gurus.oauth.service;

import com.team04.buy_gurus.oauth.CustomOAuth2User;
import com.team04.buy_gurus.oauth.OAuthAttributes;
import com.team04.buy_gurus.user.entity.Provider;
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("로드유저 메서드 동작");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Provider provider = getProvider(registrationId);
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(provider, userNameAttributeName, attributes);

        User user = getUser(extractAttributes, provider);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }

    private Provider getProvider(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return Provider.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return Provider.KAKAO;
        }
        return Provider.GOOGLE;
    }

    private User getUser(OAuthAttributes attributes, Provider provider) {
        User user = userRepository.findByProviderAndProviderId(provider,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if (user == null) {
            return saveUser(attributes, provider);
        }

        return user;
    }

    private User saveUser(OAuthAttributes attributes, Provider provider) {
        User user = attributes.toEntity(provider, attributes.getOauth2UserInfo());
        return userRepository.save(user);
    }
}
