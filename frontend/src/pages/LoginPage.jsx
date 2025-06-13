import React from 'react';
import LoginForm from '../components/LoginForm';
import '../styles/LoginPage.css';

export default function LoginPage() {
  return (
    <div className="container">
      <div className="left-pane">
        <h1>NEWSIGHT</h1>
        <p>당신의 뉴스, 한눈에 요약하다.</p>
      </div>
      <div className="right-pane">
        <LoginForm />
      </div>
    </div>
  );
}
