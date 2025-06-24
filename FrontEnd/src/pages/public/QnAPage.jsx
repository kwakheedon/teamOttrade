import React from 'react'
import { useNavigate } from 'react-router'

// Q n A 게시글 목록 페이지
const QnAPage = () => {

  const navigate = useNavigate();

  const write = () => {
    navigate('/board/write')
  };

  return (
    <div>
      <h1>QnA</h1>

      <button onClick={write}>QnA 작성</button>

      {/* QnA 글 목록 */}

    </div>
  )
}

export default QnAPage