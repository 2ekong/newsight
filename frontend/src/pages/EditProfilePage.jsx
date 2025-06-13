import React, { useEffect, useState } from 'react'
import axiosInstance from '../api/axiosInstance'
import Header from '../components/Header'
import { useNavigate } from 'react-router-dom'
import '../styles/EditProfilePage.css'

export default function EditProfilePage() {
  const [nickname, setNickname] = useState('')
  const [errorMsg, setErrorMsg] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    axiosInstance.get('/api/users/me')
      .then((res) => setNickname(res.data.nickname))
      .catch(() => setErrorMsg('사용자 정보를 불러오지 못했습니다.'))
  }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await axiosInstance.put('/api/users/me', { nickname })
      alert('수정 완료')
      navigate('/mypage')
    } catch (err) {
      setErrorMsg('닉네임 수정 실패')
    }
  }

  return (
    <>
      <Header />
      <div className="edit-profile-container">
        <h2>프로필 수정</h2>
        {errorMsg && <p className="error-text">{errorMsg}</p>}
        <form onSubmit={handleSubmit} className="edit-form">
          <label>닉네임</label>
          <input
            type="text"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            required
          />
          <button type="submit">저장하기</button>
        </form>
      </div>
    </>
  )
}
