# newsight
#  뉴스 기반 감정 분석 서비스

GPT 기반 뉴스 요약 및 감정 분석 웹 애플리케이션입니다.  
사용자가 뉴스 기사를 등록하면 AI가 내용을 요약하고 감정(긍정/부정 등)을 분석하여 결과를 보여주며, 댓글 기능과 JWT 인증도 함께 제공합니다.

---

## 프로젝트 개요

- **주요 기능**: 뉴스 등록, GPT 요약/감정 분석, 댓글 기능, JWT 로그인
- **AI 연동**: GPT API를 이용한 기사 요약 및 감정 분석
- **보안**: JWT 토큰 기반 인증/인가 처리
- **DB 설계**: 기사(Article)와 댓글(Comment) 도메인 분리 구조

---

## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| **Frontend** | React, Axios, React Router, PrivateRoute |
| **Backend** | Spring Boot, Spring Security, Spring Data JPA |
| **AI API** | OpenAI GPT (RestTemplate 기반 연동) |
| **인증** | JWT (Access Token 발급 및 필터 처리) |
| **기타** | JPA Auditing, 예외 핸들링, DTO 계층 설계 |

---

## 주요 기능 설명

### 사용자 기능
- 뉴스 기사 등록 및 요약 결과 확인
- GPT 기반 감정 분석 결과 시각화
- 댓글 작성/조회/삭제 가능
- 로그인/회원가입을 통한 인증 기반 사용

### 관리자/보안 처리
- JWT 인증 필터(`JwtAuthenticationFilter`)
- 토큰 생성 및 유효성 검사 (`JwtTokenProvider`)
- 인증 사용자만 접근 가능한 라우팅 처리 (`PrivateRoute`)

---

## 폴더 구조 (요약)

📦 backend/
┣ 📂domain/
┃ ┣ 📂article/ # 뉴스 도메인 (엔티티, 컨트롤러, 서비스 등)
┃ ┣ 📂comment/ # 댓글 도메인
┃ ┗ 📂dto/ # Request/Response DTO
┣ 📂global/
┃ ┣ 📂ai/ # GPT 연동 로직
┃ ┣ 📂security/ # JWT 필터 및 토큰 유틸
┃ ┗ 📂config/ # 공통 설정

📦 frontend/
┣ 📂src/
┃ ┣ 📂pages/ # 주요 페이지 (뉴스 등록, 보기 등)
┃ ┣ 📂api/ # Axios 인스턴스
┃ ┗ 📜PrivateRoute.jsx


---

## 📷 화면 예시 (원한다면 추가)

> ( 뉴스 등록 → 요약 결과 확인 → 댓글 작성 흐름)

---

## 🙋 프로젝트에서 배운 점

- JWT 인증 흐름과 토큰 기반 인가 처리 전반
- 외부 API(GPT)를 연동해 비즈니스 로직에 통합하는 실전 경험
- React + Axios + Spring Boot 조합으로 프론트-백엔드 통신 구조 설계
- 실전형 도메인 분리 및 DTO 계층화 설계

---

## 📌 실행 방법 (선택사항)

```bash
# Backend
cd backend
./gradlew build
java -jar build/libs/...

# Frontend
cd frontend
npm install
npm start
