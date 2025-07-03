import React from 'react'
import { useNavigate } from 'react-router-dom'
import './QnAPage.css'

// Q n A 게시글 목록 페이지
const QnAPage = () => {

  const navigate = useNavigate();

  const write = () => {
    navigate('/board/write')
  };

  return (
    <div className="qna-container">
      <div className="qna-top">
        <h1 className="qna-title">QnA</h1>
        <button onClick={write} className="qnaBtn">QnA 작성</button>
      </div>
    </div>
  )
}

export default QnAPage