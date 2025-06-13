import React, { useState } from 'react'
import axiosInstance from '../api/axiosInstance'
import Header from '../components/Header'
import { useNavigate } from 'react-router-dom'
import EmotionChart from '../components/EmotionChart'
import '../styles/WritePage.css'

export default function WritePage() {
  const [url, setUrl] = useState('')
  const [summary, setSummary] = useState(null)
  const [emotionJson, setEmotionJson] = useState(null)
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const [errorMsg, setErrorMsg] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleSummarize = async (e) => {
    e.preventDefault()
    setErrorMsg('')
    setSummary(null)
    setEmotionJson(null)
    setTitle('')
    setContent('')
    setLoading(true)

    try {
      const res = await axiosInstance.post('/api/summarize', { originalUrl: url })
      setSummary(res.data.summary)
      setEmotionJson(JSON.parse(res.data.emotionJson))
      setContent(res.data.content)
      setTitle(res.data.title || '')
    } catch (err) {
      if (err.response && err.response.data && typeof err.response.data === 'string') {
        setErrorMsg(err.response.data)
      } else {
        setErrorMsg('요약 생성 중 오류가 발생했습니다.')
      }
    } finally {
      setLoading(false)
    }
  }

  const handleRegister = async () => {
    try {
      await axiosInstance.post('/api/articles', {
        originalUrl: url,
        title,
        summary,
        content,
        emotionJson: JSON.stringify(emotionJson)
      })
      navigate('/mypage')
    } catch (err) {
      alert('등록 실패')
    }
  }

  return (
    <>
      <Header />
      <div className="write-container">
        <h2>뉴스 요약하기</h2>
        <form onSubmit={handleSummarize} className="write-form">
          <input
            type="text"
            placeholder="뉴스 URL 입력"
            value={url}
            onChange={(e) => setUrl(e.target.value)}
            required
          />
          <button type="submit">요약</button>
        </form>

        {loading && <p>요약 중입니다...⏳</p>}
        {errorMsg && <p className="error-text">{errorMsg}</p>}

        {summary && (
          <div className="write-result">
            <input
              type="text"
              placeholder="제목 입력"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="write-title-input"
              required
            />
            <p>{summary}</p>
            <EmotionChart emotionJson={emotionJson} />
            <button onClick={handleRegister}>등록하기</button>
          </div>
        )}
      </div>
    </>
  )
}