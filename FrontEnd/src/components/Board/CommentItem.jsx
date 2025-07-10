import React from 'react'
import useAuthStore from '../../stores/authStore'
import CommentForm from './CommentForm'
import CommentDeleteButton from './CommentDeleteButton'

// ---- 대댓글(자식 댓글)까지 재귀 렌더링하는 컴포넌트 ----
const CommentItem = ({ 
    comment,
    postUserId,
    level = 0,
    onDelete,
    onReplyClick,
    replyingTo,
    onCommentSubmitSuccess,
    postId,
    isAuthor
    }) => {
    const isAuthenticated = useAuthStore(state => state.isAuthenticated)
    const indentStyle = { marginLeft: `${level * 20}px` }
    const isMyComment = useAuthStore(
        state => state.isAuthenticated &&
            state.user.id === comment.user_id &&
            comment.content !== '삭제된 댓글입니다.'
    )
    // console.log('[isMyComment] :', isMyComment)

    const handleToggleReply = e => {
    // 삭제 버튼 같은 하위 요소 클릭 시에는 열고 닫기 동작 막기
    e.stopPropagation()

    if (replyingTo === comment.commentId) {
      onReplyClick(null)   // 이미 열려 있으면 닫기
    } else {
      onReplyClick(comment.commentId)  // 닫혀 있으면 열기
    }
  }

    return (
        <div
            style={indentStyle}
            className={`comment-item`}
            onClick={isAuthenticated ? handleToggleReply : undefined}
        >
        <div className={`comment-meta ${isAuthenticated ? ' replyable' : ''}`}>
            <div className={`comment-header`}>
                <span className="comment-nickname">{comment.nickname}</span>
                {comment.user_id === postUserId && (
                    <span className="comment-role">작성자</span>
                )}
                {(isMyComment || isAuthor) && (
                    <CommentDeleteButton
                        onDelete={(e)=> {
                            e.stopPropagation()
                            onDelete(comment.commentId)
                        }}
                        className = 'comment-delete-button'
                        children = ''
                    />
                )}
            </div>
            <p className="comment-text">{comment.content}</p>
        </div>

        {/* 대댓글 입력 폼 */}
        {replyingTo === comment.commentId && (
            <CommentForm
            postId={postId}
            parentCommentId={comment.commentId}
            onCommentSubmitSuccess={() => {
                onCommentSubmitSuccess()
                onReplyClick(null)
            }}
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
                // isAuthor={isAuthor}
            />
            ))
        )}
        </div>
    )
}

export default CommentItem