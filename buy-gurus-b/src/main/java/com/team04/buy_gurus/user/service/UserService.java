package com.team04.buy_gurus.user.service;

import com.team04.buy_gurus.exception.ex_user.ex.DuplicateEmailException;
import com.team04.buy_gurus.exception.ex_user.ex.DuplicateNicknameException;
import com.team04.buy_gurus.exception.ex_user.ex.UnverifiedEmailException;
import com.team04.buy_gurus.exception.ex_user.ex.UserNotFoundException;
import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import com.team04.buy_gurus.sellerinfo.repository.SellerInfoRepository;
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
    private final SellerInfoRepository sellerInfoRepository;

    @Transactional
    public void signup(SignupRequestDto request) {

        checkEmailVerified(request.getEmail());

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
                        checkEmailVerified(request.getEmail());
                        user.updateEmail(request.getEmail());
                    }
                    userRepository.save(user);
                    return new UserEditResponseDto(request.getNickname(), request.getEmail());
                })
                .orElseThrow(UserNotFoundException::new);
    }

    public void sellerRegistration(String email) {
        // TODO
        // 중복 Seller 가입 체크를 깔끔하게 작성!
        Optional<User> user = userRepository.findByEmail(email);

        user.ifPresentOrElse(User::updateRole,
                () -> {
                    throw new UserNotFoundException();
                }
        );

        // 간단한 중복 Seller 가입 체크
        sellerInfoRepository.findByUseremail(email).ifPresentOrElse(si -> {}, () -> {
            User sellerUser = user.get();
            SellerInfo sellerInfo = SellerInfo.builder()
                    .user(sellerUser)
                    .build();
            sellerInfoRepository.save(sellerInfo);
            sellerUser.updateSellerInfo(sellerInfo);
        });


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
