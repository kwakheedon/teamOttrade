import React from 'react'

//공지사항 작성 페이지
const NoticeWritePage = () => {
  return (
    <div>
      <h1>공지사항</h1>

      {/* BoardForm.jsx(게시글 폼) 호출 */}
      <BoardForm/>

      {/* 게시 버튼 누르면 공지사항 페이지로 */}
      <button>게시</button>
      
    </div>
  )
}

export default NoticeWritePage