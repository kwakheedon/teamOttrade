import React from 'react'
import CommentForm from '../../components/Board/CommentForm'

//자유/정보 공유의 세부 내용을 보여줄 페이지
const BoardDetailPage = () => {
  return (
    <div className="board-detail-page">
      <h1 className="board-category">자유게시판</h1>

      <div className="board-title-area">
        <h2 className="board-post-title">제목입니다.</h2>
        <div className="board-meta">
          <span className="board-author">작성자</span>
          <span className="board-date"> | 작성일</span>
        </div>
      </div>

      <div className="board-content">
        <p></p>
      </div>

      <div className="board-actions">
        <button className="button modify">수정</button>
        <button className="button delete">삭제</button>
      </div>

      <CommentForm />

      <div className="comments-section">
        <h3 className="comments-count">댓글 n개</h3>
        <div className="comment-item">
          <div className="comment-meta">
            <span className="comment-nickname">닉네임</span>
          </div>
          <p className="comment-text"></p>
        </div>
        <div className="comment-item">
          <div className="comment-meta">
            <span className="comment-nickname author">닉네임</span>
            <span className="comment-role">작성자</span>
            <span className="comment-delete-button">×</span>
          </div>
          <p className="comment-text"></p>
        </div>
      </div>
    </div>
  )
}

export default BoardDetailPage