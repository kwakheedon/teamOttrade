import React from 'react'
import CommentForm from '../../components/Board/CommentForm'

//자유/정보 공유의 세부 내용을 보여줄 페이지
const BoardDetailPage = () => {
  return (
    <div>
      {/* 선택한 곳에 따라 아래의 제목이 둘 중 하나로 변경? */}
      <h1>자유게시판/정보 공유</h1>

      {/* 제목 */}
      <h2></h2>

      {/* 작성자, 작성일 */}

      {/* 내용 */}

      <button>수정</button>
      <button>삭제</button>

      {/* 댓글 폼 호출 */}
      <CommentForm/>

      {/* 댓글 및 닉네임 표시 */}

    </div>
  )
}

export default BoardDetailPage