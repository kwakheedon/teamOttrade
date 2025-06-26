import React from 'react'
import CommentForm from '../../components/Board/CommentForm'
import { useNavigate } from 'react-router'

// Q n A 상세 글 페이지
const QnADetailPage = () => {

  const navigate = useNavigate();

  const back = () => {
    navigate('/qna')
  };

  return (
    <div>
      <h1>QnA</h1>

      {/* 클릭 시 QnA 페이지로 이동 */}
      <button className='btn2' onClick={back}>목록으로</button>

      {/* 질문 제목 */}
      <h2></h2>

      {/* 작성자, 작성일 */}

      {/* 질문 본문 */}

      <button>수정</button>
      <button>삭제</button>

      <h2>답변</h2>
      {/* 댓글 폼 호출 */}
      <CommentForm/>

    </div>
  )
}

export default QnADetailPage