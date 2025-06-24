import React from 'react'

// 게시글 수정 시 이용할 페이지
// 기능 추가등을 고려했을 때 작성과 수정은 분리하는 게 좋다.
const BoardEditPage = () => {
  return (
    <div>
      <h1>자유게시판/정보 공유/QnA</h1>

      {/* BoardForm.jsx(게시글 폼) 호출 */}
      <BoardForm/>

      {/* 수정 버튼 누르면 자유게시판 페이지로 */}
      <button>수정</button>
    </div>
  )
}

export default BoardEditPage