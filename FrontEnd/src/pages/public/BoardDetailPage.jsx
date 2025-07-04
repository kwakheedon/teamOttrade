import React, { useEffect, useState } from 'react'
import CommentForm from '../../components/Board/CommentForm'
import './BoardDetailPage.css'
import axios from 'axios'
import axiosInstance from '../../apis/authApi'
import { useNavigate, useParams } from 'react-router-dom'
import useAuthStore from '../../stores/authStore'

//자유 게시판 글의 세부 내용을 보여줄 페이지
const BoardDetailPage = () => {
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuthStore(); 

  const goBack = () => {
    navigate('/board');
  }

  const fetchPostDetail = async () => {
    try {
      const response = await axios.get(`/api/board/detail/${id}`); 
      setPost(response.data.data); 
    } catch (err) {
      console.error(err);
    }
  };

  const fetchComments = async () => {
    try {
      const response = await axiosInstance.get(`/api/board/detail/${id}`); 
      setComments(response.data.data.comment);
    } catch (err) {
      console.error("댓글 불러오기 실패:", err);
    }
  };

  const handleDelete = async () => {
    if (window.confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
      try {
        await axios.delete(`/api/board/delete/${id}`, {
          headers: {
            Authorization: `Bearer ${user.token}` 
          }
        });
        alert("게시글이 삭제되었습니다.");
        navigate('/board'); 
      } catch (error) {
        console.error("게시글 삭제 실패:", error);
        alert("게시글 삭제에 실패했습니다. 권한이 없거나 서버 오류일 수 있습니다.");
      }
    }
  };

  const handleCommentDelete = async (commentId) => {
    if (window.confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
      try {
        await axios.delete(`/api//board/${id}/comments/${id}`, {
          headers: {
            Authorization: `Bearer ${user.token}` 
          }
        });
        alert("댓글이 삭제되었습니다.");
        fetchComments();
      } catch (error) {
        console.error("댓글 삭제 실패:", error);
        alert("댓글 삭제에 실패했습니다. 권한이 없거나 서버 오류일 수 있습니다.");
      }
    }
  };

  const handleCommentSubmitSuccess = () => {
    fetchComments();
  };

  // 수정 버튼 클릭 시 게시글 수정 페이지로 이동하는 함수
  const handleEditClick = () => {
    navigate(`/board/edit/${id}`);
  };

  useEffect(() => {
    if (id) {
      fetchPostDetail();
      fetchComments();
    }
  }, [id]);

  if (!post) {
    return <div>게시글을 불러오는 중입니다...</div>; 
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
          <span> | {post.createdAt}</span>
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
                  {post.user_id === comment.user_id && <span className="comment-role">작성자</span>}
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