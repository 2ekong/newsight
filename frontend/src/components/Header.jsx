import React from 'react'
import { useNavigate } from 'react-router-dom'

export default function Header() {
  const navigate = useNavigate()

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userId');
    navigate('/login')
  }

  return (
    <div style={{
      padding: '1rem',
      backgroundColor: '#222',
      color: '#fff',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }}>
      <div>
        <a href="/" style={{ color: '#fff', marginRight: '1rem' }}>Home</a>
        <a href="/mypage" style={{ color: '#fff', marginRight: '1rem' }}>마이페이지</a>
        <a href="/write" style={{ color: '#fff' }}>글쓰기</a>
      </div>
      <button onClick={handleLogout} style={{ background: 'transparent', color: 'white', border: '1px solid white', padding: '0.5rem 1rem', cursor: 'pointer' }}>
        로그아웃
      </button>
    </div>
  )
}
