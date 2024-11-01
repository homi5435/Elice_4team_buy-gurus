package com.team04.buy_gurus.user.service;

import com.team04.buy_gurus.exception.ex_user.ex.DuplicateEmailException;
import com.team04.buy_gurus.exception.ex_user.ex.DuplicateNicknameException;
import com.team04.buy_gurus.exception.ex_user.ex.UnverifiedEmailException;
import com.team04.buy_gurus.exception.ex_user.ex.UserNotFoundException;
import com.team04.buy_gurus.user.dto.*;
import com.team04.buy_gurus.user.entity.Provider;
import com.team04.buy_gurus.user.entity.Role;
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void signup(SignupRequestDto request) {

        checkEmailVerified(request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }

        userRepository.save(User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .provider(Provider.NONE)
                .build());
    }

    public UserInfoResponseDto loadUserInfo(Long userId) {

        return userRepository.findById(userId)
                .map(user -> new UserInfoResponseDto(
                        // user.getImageUrl(),
                        user.getId(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getRole()))
                .orElseThrow(UserNotFoundException::new);
    }

    public UserEditResponseDto editUserInfo(Long userId, UserEditRequestDto request) {

        return userRepository.findById(userId)
                .map(user -> {
                    if (!user.getNickname().equals(request.getNickname())) {
                        user.updateNickname(request.getNickname());
                    }
                    if (userRepository.findByEmail(request.getEmail()).isEmpty()){
                        checkEmailVerified(request.getEmail());
                        user.updateEmail(request.getEmail());
                    }
                    userRepository.save(user);
                    return new UserEditResponseDto(request.getNickname(), request.getEmail());
                })
                .orElseThrow(UserNotFoundException::new);
    }

    public void resetPassword(ResetPasswordRequestDto request) {

        checkEmailVerified(request.getEmail());

        userRepository.findByEmail(request.getEmail())
                .ifPresentOrElse(user -> {
                    user.updatePassword(passwordEncoder.encode(request.getPassword()));
                    userRepository.save(user);
                },
                        () -> {
                            throw new UserNotFoundException();
                        });
    }

    public void withdrawal(Long userId) {

        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,
                        () -> {
                            throw new UserNotFoundException();
                        });
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public void checkEmailVerified(String email) {

        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String isVerified = valueOps.get(email + ":verified");

        if (isVerified != null) {
            redisTemplate.delete(email + ":verified");
        } else {
            throw new UnverifiedEmailException();
        }
    }
}
