package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    private final String email;
    private final String nickname;
    private final LocalDateTime createdAt;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.createdAt = user.getCreatedAt();
    }
}
