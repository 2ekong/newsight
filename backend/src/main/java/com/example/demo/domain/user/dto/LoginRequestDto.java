package com.example.demo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/*
Vue가 보내준 JSON을 서버가 다시 검증

@NotBlank, @Email 같은 어노테이션은
→ JSON을 @RequestBody로 받을 때 자동으로 검사됨(보안을 위해 필수 메모메모)

프론트(Vue)는 사용자 경험을 위해,
백엔드(Spring)는 보안과 데이터 무결성을 위해 각자 유효성 검사를 수행한다.
 */


@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
