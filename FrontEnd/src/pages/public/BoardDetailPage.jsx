import React, { useEffect, useState } from 'react'
import CommentForm from '../../components/Board/CommentForm'
import './BoardDetailPage.css'
import axios from 'axios'
import axiosInstance from '../../apis/authApi'
import { useNavigate, useParams } from 'react-router-dom'
import useAuthStore from '../../stores/authStore'
import Loading from '../../components/Common/Loading'


const CommentItem = ({ comment, level = 0, onDelete }) => {
  const indent = { marginLeft: `${level * 20}px` }

  const isAuthor = useAuthStore(state => 
    state.isAuthenticated && state.user?.id === comment.user_id
  )

  return (
    <div style={indent} className="comment-item">
      <div className="comment-meta">
        <span className="comment-nickname">{comment.user_id}</span>
        {comment.user_id === comment.postAuthorId && (
          <span className="comment-role">작성자</span>
        )}
        {isAuthor && (
          <button
            className="comment-delete-button"
            onClick={() => onDelete(comment.commentId)}
          >×</button>
        )}
      </div>
      <p className="comment-text">{comment.content}</p>
      {/* 자식 댓글(대댓글)이 있으면 재귀 렌더 */}
      {comment.children?.children?.length > 0 && (
        comment.children.children.map(child => (
          <CommentItem
            key={child.commentId}
            comment={{ ...child, postAuthorId: comment.postAuthorId }}
            level={level + 1}
            onDelete={onDelete}
          />
        ))
      )}
    </div>
  )
}


//자유 게시판 글의 세부 내용을 보여줄 페이지
const BoardDetailPage = () => {
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const { id } = useParams();
  const navigate = useNavigate();
  const isAuthenticated = useAuthStore(state => state.isAuthenticated)
  const user = useAuthStore(state => state.user)

  const goBack = () => {
    navigate('/board');
  }

  const fetchPostDetail = async () => {
    try {
      const response = await axios.get(`/api/board/detail/${id}`);
      const data = response.data.data
      setPost(data); 

      //최상위 댓글 배열
      const rootComments = data.comment?.comment
      setComments(rootComments? [ { ...rootComments, postAuthorId: data.user_id } ] : [])
    } catch (err) {
      console.error(err);
    }
  };

  const handleCommentDelete = async (commentId ) => {
    // 삭제 여부에 취소를 할 시 삭제 취소
    if (!window.confirm('정말 삭제하시겠습니까?')) return

    try {
      await axiosInstance.delete(`/board/${id}/comments/${commentId}`)
      // 삭제 후 다시 불러오기
      fetchPostDetail()
    } catch (err) {
      console.error('댓글 삭제 실패', err)
    }
  };

  // 수정 버튼 클릭 시 게시글 수정 페이지로 이동하는 함수
  const handleEditClick = () => {
    navigate(`/board/edit/${id}`);
  };

  useEffect(() => {
    if (id) {
      fetchPostDetail();
    }
  }, [id]);

  if (!post) {
    return <Loading/>
  }

  const isAuthor = isAuthenticated && user && post.user_id === user.id;

  return (
    <div className="board-detail-page">
      <h1 className="board-category">자유게시판</h1>
      
      <button className='back-btn' onClick={goBack}>목록으로</button>

      <div className="board-title-area">
        <h2 className="board-post-title">{post.title}</h2>
        <div className="board-meta">
          <span>{post.user_id}</span>
          {/* <span> | {post.createdAt}</span> */}
        </div>
      </div>

      <div className="board-content">
        <p>{post.content}</p>
      </div>

      {isAuthor && ( 
        <div className="board-actions">
          {/* 수정 버튼에 handleEditClick 함수 연결 */}
          <button className="button modify" onClick={handleEditClick}>수정</button>
          <button className="button delete" onClick={handleDelete}>삭제</button>
        </div>
      )}
      <div className="bottom-line"></div>

      <div className="comments-section">
        <h3 className="comments-count">댓글 {comments.length}개</h3>

        {isAuthenticated ? (
          <CommentForm postId={id} onCommentSubmitSuccess={handleCommentSubmitSuccess} />
        ) : (
          <p className="login-prompt">댓글을 작성하려면 로그인해주세요.</p>
        )}
        
        <div className="comments-list">
          {comments.length > 0 ? (
            comments.map((comment) => (
              <div key={comment.id} className="comment-item">
                <div className="comment-meta">
                  <span className="comment-nickname">{comment.user_id}</span>
                    {/* 게시글 작성자이면 작성자 버튼 뜨도록 */}
                    {post.user_id === comment.user_id && <span className="comment-role">작성자</span>}
                    {/* 댓글 작성자이면 삭제버튼 뜨도록 */}
                    {isAuthenticated && user && comment.user_id === user.id && ( 
                  <span className="comment-delete-button" onClick={() => handleCommentDelete(comment.id)}>×</span>
                  )}
                </div>
                <p className="comment-text">{comment.content}</p>
              </div>
            ))
          ) : (
            <p>아직 댓글이 없습니다.</p>
          )}
        </div>
      </div>
    </div>
  )
}

export default BoardDetailPage