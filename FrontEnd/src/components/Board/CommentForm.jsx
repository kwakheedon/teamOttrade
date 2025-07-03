import React from 'react'
import './CommentForm.css'

//댓글 관련 컴포넌트를 담을 폼
const CommentForm = () => {
  return (
    <div className="comment-form-container">
      <input type="text" className="comment-input" placeholder='댓글을 입력하세요'/>
    </div>
  )
}

export default CommentForm