package com.team04.buy_gurus.user.service;

import com.team04.buy_gurus.exception.ex_user.DuplicateEmailException;
import com.team04.buy_gurus.exception.ex_user.DuplicateNicknameException;
import com.team04.buy_gurus.exception.ex_user.UserNotFoundException;
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
    public void signup(SignupRequestDto request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new DuplicateNicknameException();
        }

        userRepository.save(User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .provider(Provider.NONE)
                .build());
    }

    public UserInfoResponseDto loadUserInfo(String email) {

        return userRepository.findByEmail(email)
                .map(user -> new UserInfoResponseDto(
                        // user.getImageUrl(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getRole()))
                .orElseThrow(UserNotFoundException::new);
    }

    public UserEditResponseDto editUserInfo(String email, UserEditRequestDto request) {

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
                .orElseThrow(UserNotFoundException::new);
    }

    public void withdrawal(String email) {

        userRepository.findByEmail(email)
                .ifPresentOrElse(userRepository::delete,
                        () -> {
                            throw new UserNotFoundException();
                        });
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}
