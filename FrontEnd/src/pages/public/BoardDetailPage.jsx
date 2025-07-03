import React, { useEffect, useState } from 'react'
import CommentForm from '../../components/Board/CommentForm'
import './BoardDetailPage.css'
import axios from 'axios'
import { useNavigate, useParams } from 'react-router'
import useAuthStore from '../../stores/authStore'

//자유 게시판 글의 세부 내용을 보여줄 페이지
const BoardDetailPage = () => {
  const [post, setPost] = useState(null);
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore(); // 로그인 상태 가져오기

  const goBack = () => {
    navigate('/board');
  }

  const fetchPostDetail = async () => {
    try {
      // /board/detail/{boardId} 참조
      const response = await axios.get(`/api/board/detail/${id}`); 
      setPost(response.data.data); 
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    if (id) {
      fetchPostDetail();
    }
  }, [id]);

  // post가 null일 경우 로딩 상태 또는 기본값 표시
  if (!post) {
    return <div>게시글을 불러오는 중입니다...</div>; // 사용자에게 더 친화적인 메시지
  }

  return (
    <div className="board-detail-page">
      <h1 className="board-category">자유게시판</h1>
      
      <button className='back-btn' onClick={goBack}>목록으로</button>

      <div className="board-title-area">
        <h2 className="board-post-title">{post.title}</h2>
        <div className="board-meta">
          <span>{post.user_id}</span>
          <span> | {post.createdAt}</span>
        </div>
      </div>

      <div className="board-content">
        <p>{post.content}</p>
      </div>

      <div className="board-actions">
        <button className="button modify">수정</button>
        <button className="button delete">삭제</button>
      </div>
      <div className="bottom-line"></div>

      <div className="comments-section">
        <h3 className="comments-count">댓글 n개</h3>

        {/* 로그인 상태에 따라 CommentForm 조건부 렌더링 */}
        {isAuthenticated ? (
          <CommentForm />
        ) : (
          <p className="login-prompt">댓글을 작성하려면 로그인해주세요.</p>
        )}
        
        <div className="comment-item">
          <div className="comment-meta">
            <span className="comment-nickname">닉네임</span>
          </div>
          <p className="comment-text">댓글 내용 1</p> {/* 댓글 내용 예시 */}
        </div>
        <div className="comment-item">
          <div className="comment-meta">
            <span className="comment-nickname author">닉네임</span>
            <span className="comment-role">작성자</span>
            <span className="comment-delete-button">×</span>
          </div>
          <p className="comment-text">댓글 내용 2</p> {/* 댓글 내용 예시 */}
        </div>
      </div>
    </div>
  )
}

export default BoardDetailPage