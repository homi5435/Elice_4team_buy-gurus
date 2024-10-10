package com.team04.buy_gurus.user.service;

import com.team04.buy_gurus.user.dto.SignupRequestDto;
import com.team04.buy_gurus.user.dto.UserEditRequestDto;
import com.team04.buy_gurus.user.dto.UserEditResponseDto;
import com.team04.buy_gurus.user.dto.UserInfoResponseDto;
import com.team04.buy_gurus.user.entity.Provider;
import com.team04.buy_gurus.user.entity.Role;
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(SignupRequestDto request) throws Exception{

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        return userRepository.save(User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .provider(Provider.NONE)
                .build()).getId();
    }

    public UserInfoResponseDto loadUserInfo(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return userRepository.findByEmail(email)
                .map(user -> new UserInfoResponseDto(
                        // user.getImageUrl(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getRole()))
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    public UserEditResponseDto editUserInfo(Authentication authentication, UserEditRequestDto request) throws Exception {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return userRepository.findByEmail(email)
                .map(user -> {
                    if (userRepository.findByNickname(request.getNickname()).isEmpty()) {
                        user.updateNickname(request.getNickname());
                    }
                    if (userRepository.findByEmail(request.getEmail()).isEmpty()){
                        user.updateEmail(request.getEmail());
                    }
                    userRepository.save(user);
                    return new UserEditResponseDto(request.getNickname(), request.getEmail());
                })
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    public void withdrawal(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        userRepository.findByEmail(email)
                .ifPresentOrElse(user -> userRepository.delete(user),
                        () -> {
                            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
                        });
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
