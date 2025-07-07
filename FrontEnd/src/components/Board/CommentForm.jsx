// src/components/Board/CommentForm.jsx
import React, { useState } from 'react'
import './CommentForm.css'
import axios from '../../apis/authApi'

/**
 * CommentForm 컴포넌트
 * Props:
 * - postId: 게시글 ID
 * - parentCommentId: 대댓글일 경우 부모 댓글 ID (기본 null)
 * - onCommentSubmitSuccess: 작성 후 호출되는 콜백
 * - onCancel: 대댓글 취소 시 호출되는 콜백 (기본 null)
 */
const CommentForm = ({ postId, parentCommentId = null, onCommentSubmitSuccess, }) => {
  const [commentContent, setCommentContent] = useState('')

  const handleSubmitComment = async () => {
    if (!commentContent.trim()) {
      alert(parentCommentId ? '대댓글 내용을 입력해주세요.' : '댓글 내용을 입력해주세요.')
      return
    }

    try {
      // payload에 parentCommentId 포함
      const payload = { content: commentContent }
      if (parentCommentId) payload.parentId = parentCommentId

      await axios.post(`/board/${postId}/comments`, payload)
      setCommentContent('')
      onCommentSubmitSuccess()
    } catch (error) {
      console.error('댓글 작성 실패:', error)
      alert('댓글 작성에 실패했습니다. 서버 오류 또는 권한 문제일 수 있습니다.')
    }
  }

  return (
    <div className="comment-form-container">
      <input
        type="text"
        className="comment-input"
        placeholder={parentCommentId ? '대댓글을 입력하세요' : '댓글을 입력하세요'}
        value={commentContent}
        onClick={e => e.stopPropagation()}
        onChange={e => setCommentContent(e.target.value)}
        onKeyDown={e => e.key === 'Enter' && handleSubmitComment()}
      />

      <div className="comment-form-actions">
        <button className="submit-comment-button" onClick={handleSubmitComment}>
          작성하기
        </button>
      </div>
    </div>
  )
}

export default CommentForm
