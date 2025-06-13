import React, { useEffect, useState } from 'react'
import axiosInstance from '../api/axiosInstance'
import Header from '../components/Header'
import '../styles/Mypage.css'

export default function Mypage() {
  const [articles, setArticles] = useState([])
  const [loading, setLoading] = useState(true)
  const [errorMsg, setErrorMsg] = useState('')
  const userId = Number(localStorage.getItem('userId'))

  useEffect(() => {
    axiosInstance.get('/api/articles')
      .then((res) => {
        // 최신순 정렬 적용
        const sorted = res.data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
        setArticles(sorted)
      })
      .catch(() => setErrorMsg('데이터를 불러오는 중 오류가 발생했습니다.'))
      .finally(() => setLoading(false))
  }, [])

  const handleDelete = (id) => {
    if (!window.confirm('정말 삭제하시겠습니까?')) return
    axiosInstance.delete(`/api/articles/${id}`)
      .then(() => setArticles((prev) => prev.filter((a) => a.id !== id)))
      .catch(() => alert('삭제 실패'))
  }

  return (
    <>
      <Header />
      <div className="mypage-container">
        <h2 className="mypage-title">내 뉴스 요약 목록</h2>
        {loading && <p>불러오는 중...⏳</p>}
        {errorMsg && <p className="error-text">{errorMsg}</p>}
        <ul className="mypage-list">
          {articles.map((article) => (
            <li key={article.id} className="mypage-card">
              <a href={`/content/${article.id}`} className="mypage-title">
                {article.title}
              </a>
              <div className="mypage-date">
                {new Date(article.createdAt).toLocaleString('ko-KR', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit',
                  hour: '2-digit',
                  minute: '2-digit',
                })}
              </div>
              <div className="mypage-actions">
                <a href={`/edit/${article.id}`} className="btn-edit">수정</a>
                {userId === article.userId && (
                  <button onClick={() => handleDelete(article.id)} className="btn-delete">삭제</button>
                )}
              </div>
            </li>
          ))}
        </ul>
      </div>
    </>
  )
}
