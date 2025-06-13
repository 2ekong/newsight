import React from 'react'
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid
} from 'recharts'

export default function EmotionChart({ emotionJson }) {
  if (
    !emotionJson ||
    typeof emotionJson !== 'object' ||
    Object.keys(emotionJson).length === 0
  ) {
    return <p>감정 데이터가 없습니다.</p>
  }

  const data = Object.entries(emotionJson).map(([emotion, value]) => ({
    emotion,
    value: Math.round(value * 100) / 100
  }))

  return (
    <div style={{ width: '100%', height: 300 }}>
      <h4 style={{ textAlign: 'center', marginBottom: '10px' }}>감정 분석 결과</h4>
      <ResponsiveContainer>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="emotion" />
          <YAxis domain={[0, 1]} />
          <Tooltip />
          <Bar dataKey="value" fill="#4A90E2" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
