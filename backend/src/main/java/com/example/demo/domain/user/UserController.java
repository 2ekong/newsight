package com.example.demo.domain.user;

import com.example.demo.domain.user.dto.LoginRequestDto;
import com.example.demo.domain.user.dto.SignupRequestDto;
import com.example.demo.domain.user.dto.UserResponseDto;
import com.example.demo.domain.user.dto.UserUpdateRequestDto;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDto dto) {
        User user = userService.loginAndReturnUser(dto);
        String token = userService.createTokenForUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", token);
        response.put("userId", user.getId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateUserInfo(@RequestBody UserUpdateRequestDto dto,
                                                          HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getEmail(token);
        User updatedUser = userService.updateUserInfo(email, dto);
        return ResponseEntity.ok(new UserResponseDto(updatedUser));
    }
}
