import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axiosInstance from '../api/axiosInstance'
import '../styles/SignupPage.css' // CSS 분리 권장

export default function SignupPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [nickname, setNickname] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await axiosInstance.post('/api/signup', { email, password, nickname })
      alert('회원가입 완료!')
      navigate('/login')
    } catch (err) {
      alert('회원가입 실패!')
      console.error(err)
    }
  }

  return (
    <div className="signup-container">
      <div className="signup-left">
        <h1>NEWSIGHT</h1>
        <p>한눈에 보는 뉴스 요약 서비스</p>
      </div>
      <div className="signup-right">
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <input
            type="text"
            placeholder="닉네임"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            required
          />
          <button type="submit">가입하기</button>
        </form>
        <p style={{ marginTop: '1rem' }}>
          이미 계정이 있으신가요? <a href="/login">로그인</a>
        </p>
      </div>
    </div>
  )
}
