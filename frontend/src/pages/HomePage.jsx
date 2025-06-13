import React, { useEffect, useState } from 'react'
import axiosInstance from '../api/axiosInstance'
import Header from '../components/Header'
import '../styles/HomePage.css'

export default function HomePage() {
  const [articles, setArticles] = useState([])
  const [sort, setSort] = useState('latest')

  useEffect(() => {
    axiosInstance.get(`/api/articles/public?sort=${sort}`)
      .then((res) => setArticles(res.data))
      .catch((err) => console.error(err))
  }, [sort])

  return (
    <>
      <Header />
      <div className="home-container">
        <h2 className="home-title">뉴스 게시판</h2>
        <div className="sort-buttons">
          <button onClick={() => setSort('latest')} disabled={sort === 'latest'}>최신순</button>
          <button onClick={() => setSort('oldest')} disabled={sort === 'oldest'}>오래된순</button>
        </div>

        <ul className="article-list">
          {articles.map((a) => (
            <li key={a.id} className="article-card">
              <a href={`/content/${a.id}`} className="article-title">
                {a.title}
              </a>
              <div className="article-date">
                {new Date(a.createdAt).toLocaleString('ko-KR', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit',
                  hour: '2-digit',
                  minute: '2-digit',
                })}
              </div>
              <p className="article-summary">{a.summary.slice(0, 60)}...</p>
            </li>
          ))}
        </ul>
      </div>
    </>
  )
}
