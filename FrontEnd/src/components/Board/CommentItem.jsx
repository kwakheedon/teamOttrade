import React from 'react'
import useAuthStore from '../../stores/authStore'
import CommentForm from './CommentForm'



// ---- 대댓글(자식 댓글)까지 재귀 렌더링하는 컴포넌트 ----
const CommentItem = ({ comment, postUserId, level = 0, onDelete, onReplyClick, replyingTo, onCommentSubmitSuccess, postId }) => {
  const indentStyle = { marginLeft: `${level * 20}px` }
    const isMyComment = useAuthStore(
        state => state.isAuthenticated && state.userId === comment.user_id
    )

    return (
        <div style={indentStyle} className="comment-item">
        <div className="comment-meta">
            <span className="comment-nickname">{comment.nickname}</span>
            {comment.user_id === postUserId && (
            <span className="comment-role">작성자</span>
            )}
            {isMyComment && (
            <button
                className="comment-delete-button"
                onClick={() => onDelete(comment.commentId)}
            >
                ×
            </button>
            )}
        </div>
        <p className="comment-text">{comment.content}</p>

        {/* 답글 버튼 */}
        <button className="reply-button" onClick={() => onReplyClick(comment.commentId)}>
            답글
        </button>

        {/* 대댓글 입력 폼 */}
        {replyingTo === comment.commentId && (
            <CommentForm
            postId={postId}
            parentCommentId={comment.commentId}
            onCommentSubmitSuccess={() => {
                onCommentSubmitSuccess()
                onReplyClick(null)
            }}
            onCancel={() => onReplyClick(null)}
            />
        )}

        {/* 하위 children 배열이 있으면 재귀 렌더 */}
        {Array.isArray(comment.children) && comment.children.length > 0 && (
            comment.children.map(child => (
            <CommentItem
                key={child.commentId}
                comment={child}
                postUserId={postUserId}
                level={level + 1}
                onDelete={onDelete}
                onReplyClick={onReplyClick}
                replyingTo={replyingTo}
                onCommentSubmitSuccess={onCommentSubmitSuccess}
                postId={postId}
            />
            ))
        )}
        </div>
    )
}

export default CommentItem