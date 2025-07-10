// src/pages/public/NotFoundPage.jsx
import React from 'react'
import { useNavigate } from 'react-router-dom'
import NotFoundPage_image from '../../assets/image/sadOtter.png'
import './NotFoundPage.css'

export default function NotFoundPage() {
  const navigate = useNavigate()

  return (
    <div className="notfound-container">
      <div className="notfound-content">
        <div>
            <img src={NotFoundPage_image} alt="404" />
        </div>
        <div>
            <p className="notfound-message">
            죄송합니다. 요청하신 페이지를 찾을 수 없습니다.
            </p>
            <button
            className="notfound-button"
            onClick={() => navigate('/')}
            >
            홈으로 돌아가기
            </button>   
        </div>
      </div>
    </div>
  )
}
