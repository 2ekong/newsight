import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import axiosInstance from '../api/axiosInstance'
import Header from '../components/Header'
import '../styles/EditArticlePage.css'

export default function EditArticlePage() {
  const { id } = useParams()
  const navigate = useNavigate()

  const [title, setTitle] = useState('')
  const [summary, setSummary] = useState('')
  const [emotionJson, setEmotionJson] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    axiosInstance.get(`/api/articles/${id}`)
      .then((res) => {
        setTitle(res.data.title || '')
        setSummary(res.data.summary || '')
        setEmotionJson(res.data.emotionJson || '')
      })
      .catch(() => setError('게시글 정보를 불러오는 데 실패했습니다.'))
  }, [id])

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await axiosInstance.put(`/api/articles/${id}`, {
        title,
        summary,
        emotionJson
      })
      alert('수정 완료!')
      navigate(`/content/${id}`)
    } catch (err) {
      alert('수정 실패!')
    }
  }

  return (
    <>
      <Header />
      <div className="edit-article-container">
        <h2>게시글 수정</h2>
        {error && <p className="error-text">{error}</p>}
        <form onSubmit={handleSubmit}>
          <label>제목</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
          <label>요약</label>
          <textarea
            value={summary}
            onChange={(e) => setSummary(e.target.value)}
            rows={5}
            required
          />
          <label>감정 분석 JSON</label>
          <textarea
            value={emotionJson}
            onChange={(e) => setEmotionJson(e.target.value)}
            rows={5}
          />
          <button type="submit">저장하기</button>
        </form>
      </div>
    </>
  )
}
