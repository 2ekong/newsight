import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import axiosInstance from '../api/axiosInstance'
import Header from '../components/Header'
import CommentSection from '../components/CommentSection'
import EmotionChart from '../components/EmotionChart'
import '../styles/DetailPage.css'

export default function ContentDetailPage() {
  const { id } = useParams()
  const [article, setArticle] = useState(null)
  const [emotionJson, setEmotionJson] = useState(null)
  const [errorMsg, setErrorMsg] = useState('')

  useEffect(() => {
  axiosInstance.get(`/api/articles/${id}`)
    .then((res) => {
      setArticle(res.data)

      if (res.data.emotionJson) {
        try {
          const parsed = typeof res.data.emotionJson === 'string'
            ? JSON.parse(res.data.emotionJson)
            : res.data.emotionJson
          setEmotionJson(parsed)
        } catch (e) {
          console.error('감정 데이터 파싱 실패:', e)
          setEmotionJson(null)
        }
      }
    })
    .catch(() => setErrorMsg('게시글을 불러오는 데 실패했습니다.'))
}, [id])


  if (errorMsg) {
    return (
      <>
        <Header />
        <p className="error-text">{errorMsg}</p>
      </>
    )
  }

  if (!article) {
    return (
      <>
        <Header />
        <p>불러오는 중...⏳</p>
      </>
    )
  }

  return (
    <>
      <Header />
      <div className="detail-container">
        <h1 className="detail-title">{article.title}</h1>
        <div className="detail-date">
          {new Date(article.createdAt).toLocaleString('ko-KR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
          })}
        </div>
        <p className="detail-summary">{article.summary}</p>
        <div className="detail-content">{article.content}</div>

        {emotionJson && (
          <div className="emotion-graph">
            <EmotionChart emotionJson={emotionJson} />
          </div>
        )}


        <CommentSection articleId={article.id} />
      </div>
    </>
  )
}
