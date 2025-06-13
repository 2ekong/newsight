package com.example.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 회원 관련
    USER_NOT_FOUND("USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    USER_PASSWORD_INVALID("USER_PASSWORD_INVALID", "비밀번호가 일치하지 않습니다."),
    USER_EMAIL_DUPLICATED("USER_EMAIL_DUPLICATED", "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_DUPLICATED("USER_NICKNAME_DUPLICATED", "이미 사용 중인 닉네임입니다."),

    // JWT 관련
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "토큰이 만료되었습니다."),

    //게시글 관련
    ARTICLE_NOT_FOUND("ARTICLE_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),
    INVALID_AUTHORITY("INVALID_AUTHORITY", "접근 권한이 없습니다."),

    //댓글 관련
    COMMENT_NOT_FOUND("C001", "댓글이 존재하지 않습니다."),
    FORBIDDEN("C002", "권한이 없습니다.");


    private final String code;
    private final String message;
}
