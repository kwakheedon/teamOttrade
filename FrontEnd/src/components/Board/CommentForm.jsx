import React, { useState } from 'react'
import './CommentForm.css'
import useAuthStore from '../../stores/authStore';
import axios from '../../apis/authApi'

//댓글 관련 컴포넌트를 담을 폼
const CommentForm = ({ postId, onCommentSubmitSuccess }) => {
  const [commentContent, setCommentContent] = useState(''); // 댓글 내용을 저장할 상태
  const { user } = useAuthStore(); // 로그인된 사용자 정보 (토큰 포함)

  const handleSubmitComment = async () => {
    if (!commentContent.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    if (!user || !user.token) {
      alert("로그인이 필요합니다.");
      return;
    }

    try {
      // 실제 댓글 작성 API 엔드포인트에 따라 URL과 데이터를 조정해야 합니다.
      // 예: `/api/comments/create` 또는 `/api/board/${postId}/comments`
      const response = await axios.post(`/api/board/${postId}/comments`, {
        parentId: postId,
        content: commentContent,
        // user_id는 백엔드에서 토큰을 통해 추출하거나 필요시 명시적으로 보낼 수 있습니다.
        // user_id: user.id, 
      }, {
        headers: {
          Authorization: `Bearer ${user.token}` 
        }
      });
      
      if (response.data.success) { // 백엔드 응답 구조에 따라 조정
        alert("댓글이 성공적으로 작성되었습니다.");
        setCommentContent(''); // 댓글 입력 필드 초기화
        onCommentSubmitSuccess(); // 부모 컴포넌트에게 댓글이 작성되었음을 알림
      } else {
        alert("댓글 작성에 실패했습니다.");
      }
    } catch (error) {
      console.error("댓글 작성 실패:", error);
      alert("댓글 작성에 실패했습니다. 서버 오류 또는 권한 문제일 수 있습니다.");
    }
  };

  return (
    <div className="comment-form-container">
      <input 
        type="text" 
        className="comment-input" 
        placeholder='댓글을 입력하세요'
        value={commentContent}
        onChange={(e) => setCommentContent(e.target.value)}
        onKeyPress={(e) => {
          if (e.key === 'Enter') {
            handleSubmitComment();
          }
        }}
      />
      <button className="submit-comment-button" onClick={handleSubmitComment}>작성하기</button>
    </div>
  )
}

export default CommentForm