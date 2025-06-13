package com.example.demo.domain.user;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.SignupRequestDto;
import com.example.demo.domain.user.dto.UserUpdateRequestDto;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    /*
    1. email로 사용자 조회
    2. 없으면 → 예외 발생
    3. 있으면 → password 일치 확인
    4. 일치하면 → JWT 발급
    5. accessToken 문자열 반환
     */

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signup(SignupRequestDto dto) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATED);
        }

        // 닉네임 중복 확인
        if (userRepository.existsByNickname(dto.getNickname())) {
            throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATED);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // User 엔티티 생성
        User user = User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .role(Role.USER) // 기본 권한
                .build();

        // 저장
        userRepository.save(user);
    }

    public User loginAndReturnUser(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.USER_PASSWORD_INVALID);
        }

        return user;
    }

    public String createTokenForUser(User user) {
        return jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
    }

    public User updateUserInfo(String email, UserUpdateRequestDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (dto.getNickname() != null && !dto.getNickname().equals(user.getNickname())) {
            user.updateNickname(dto.getNickname());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(dto.getPassword());
            user.updatePassword(encoded);
        }

        return userRepository.save(user);
    }
}