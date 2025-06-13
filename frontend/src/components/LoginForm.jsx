import React, { useState } from 'react'
import axios from 'axios'
import { useNavigate, Link } from 'react-router-dom'

export default function LoginForm() {
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [errorMsg, setErrorMsg] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    setErrorMsg('')
    try {
      const response = await axios.post('http://localhost:8080/api/login', {
        email,
        password
      })

      const token = response.data.accessToken
      const userId = response.data.userId

      if (!token || !userId) {
        setErrorMsg('로그인 응답이 올바르지 않습니다.')
        return
      }

      localStorage.setItem('accessToken', token)
      localStorage.setItem('userId', userId)

      navigate('/')
    } catch (err) {
      if (err.response && err.response.data && typeof err.response.data === 'string') {
        setErrorMsg(err.response.data)
      } else {
        setErrorMsg('로그인 중 오류가 발생했습니다.')
      }
    }
  }

  return (
    <form className="login-form" onSubmit={handleSubmit}>
      <h2>로그인</h2>
      {errorMsg && <div style={{ color: 'red', marginBottom: '1rem' }}>{errorMsg}</div>}
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
      <button type="submit">로그인</button>
      <p>계정이 없으신가요? <Link to="/signup">회원가입</Link></p>
    </form>
  )
}
