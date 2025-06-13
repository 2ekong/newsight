package com.example.demo.domain.user;

import com.example.demo.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //JPA 기본 생성자
@AllArgsConstructor
@Builder
//도메인 모델 (User)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @JsonIgnore // 클라이언트에 노출되지 않도록
    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 30, nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)    //Role은 enum이니까 EnumType.STRING으로 문자열로 저장
    @Column(length = 10)
    private Role role;  //상수 타입

    @Column(length = 45)
    private String lastLoginIp;

    //== 비즈니스 메서드 ==//
    public void updateLastLoginIp(String ip) {  //로그인 성공 시, 사용자의 마지막 ip를 기록
        this.lastLoginIp = ip;
    }

    public void updatePassword(String newPassword) {    //비밀번호를 변경할 때
        this.password = newPassword;
    }

    public void updateNickname(String newNickname) {    //닉네임 변경 시 사용
        this.nickname = newNickname;
    }

    public String getRoleKey() {
        return this.role.name();
    }
}
