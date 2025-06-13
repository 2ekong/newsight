package com.example.demo.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
==================================================
[1] Vue → API 요청
    ↓ Authorization: Bearer <token>

[2] JwtAuthenticationFilter (지금 여기)
    ↓
            [2-1] 토큰 추출
            [2-2] 유효성 검사
            [2-3] 인증 객체(SecurityContext)에 저장

[3] 요청이 Controller까지 도달

[4] 인증 실패 시 401 Unauthorized 반환
==================================================
*/
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 여기를 추가해서 로그인, 회원가입 경로는 그냥 통과시킴
        String path = request.getRequestURI();
        if (path.equals("/api/login") || path.equals("/api/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        //1. Authorization 헤더에서 토큰 추출
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7); //"Bearer " 이후부터 토큰만 추출

            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmail(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                //3. 인증 객체 생성(비밀번호는 null로 처리)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                //요청 정보 (IP, 세션 ID 등)를 인증 객체에 추가로 담음
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //4.SecurityContext에 인증 정보 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
