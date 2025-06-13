import React, { useEffect, useState, useCallback } from 'react'
import axiosInstance from '../api/axiosInstance'
import '../styles/CommentSection.css'

export default function CommentSection({ articleId }) {
  const [comments, setComments] = useState([])
  const [content, setContent] = useState('')
  const userId = Number(localStorage.getItem('userId'))

  const fetchComments = useCallback(() => {
    axiosInstance.get(`/api/comments/${articleId}`)
      .then((res) => {
        const sorted = [...res.data].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
        setComments(sorted)
      })
      .catch(console.error)
  }, [articleId])


  useEffect(() => {
    fetchComments()
  }, [fetchComments])

  const handleSubmit = (e) => {
    e.preventDefault()
    axiosInstance.post('/api/comments', { articleId, content })
      .then(() => {
        setContent('')
        fetchComments()
      })
      .catch(console.error)
  }

  const handleDelete = (commentId) => {
    axiosInstance.delete(`/api/comments/${commentId}`)
      .then(() => {
        setComments((prev) => prev.filter((c) => c.id !== commentId))
      })
      .catch(() => alert('삭제 실패'))
  }

  return (
    <div className="comment-section">
      <h3>댓글</h3>
      <form onSubmit={handleSubmit} className="comment-form">
        <input
          type="text"
          placeholder="댓글을 입력하세요"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />
        <button type="submit">작성</button>
      </form>
      <ul className="comment-list">
        {comments.map((c) => (
          <li key={c.id} className="comment-item">
            <div>
              <span className="comment-nickname">{c.nickname}</span>
              <span className="comment-date">
                {new Date(c.createdAt).toLocaleString('ko-KR', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit',
                  hour: '2-digit',
                  minute: '2-digit'
                })}
              </span>
            </div>
            <p>{c.content}</p>
            {userId === c.userId && (
              <button onClick={() => handleDelete(c.id)} className="comment-delete-btn">
                삭제
              </button>
            )}
          </li>
        ))}
      </ul>
    </div>
  )
}
