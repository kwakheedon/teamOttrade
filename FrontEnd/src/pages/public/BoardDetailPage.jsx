import React, { useEffect, useState } from 'react'
import CommentForm from '../../components/Board/CommentForm'
import './BoardDetailPage.css'
import axiosInstance from '../../apis/authApi'
import { useLocation, useNavigate, useParams } from 'react-router-dom'
import useAuthStore from '../../stores/authStore'
import Loading from '../../components/Common/Loading'
import CommentItem from '../../components/Board/CommentItem'
import PostContent from '../../utils/PostContent'
import UndoButton from '../../components/Common/UndoButton'
import LikeButton from '../../components/Common/LikeButton'

// 댓글 총 개수를 재귀적으로 계산하는 함수
const countTotalComments = (comments) => {
  return comments.reduce((total, comment) => {
    const childCount = Array.isArray(comment.children)
      ? countTotalComments(comment.children)
      : 0
    return total + 1 + childCount
  }, 0)
}

//댓글 배열을 생성일자 순(오래된 순)으로 정렬하고, 하위 자식도 재귀 정렬
const sortComments = (comments) => {
  return comments
    .slice()
    .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
    .map(comment => ({
      ...comment,
      children: Array.isArray(comment.children)
        ? sortComments(comment.children)
        : []
    }))
}

const BoardDetailPage = () => {
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [like, setLike] = useState(null)
  const [replyingTo, setReplyingTo] = useState(null)
  const { id } = useParams()
  const navigate = useNavigate()

  const isAuthenticated = useAuthStore(state => state.isAuthenticated)
  const user = useAuthStore(state => state.user)

  // 게시글 + 댓글(최상위) 불러오기
  const fetchPostDetail = async () => {
    try {
      const res = await axiosInstance.get(`/board/detail/${id}`)
      const data = res.data.data
      setPost(data)
      console.log(res.data.data)
      const rawComments = Array.isArray(data.comment) ? data.comment : []
      setComments(sortComments(rawComments))
      setLike(data.postLikeCount)
    } catch (err) {
      console.error('게시글 상세 조회 실패', err)
    }
  }

  //게시글 수정하러 보내주는 함수
  const handlePostEdit = async () => {
    navigate(`/board/edit/${post.boardId}`, {
      state: {
        type: post.type,
        post: {
          // boardId: post.boardId,
          content: post.content,
          title: post.title,
        }
      }
    })
  }

  //게시글 삭제 함수
  const handlePostDelete = async () => {
    if (!window.confirm('정말 이 글을 삭제하시겠습니까?')) return
    try {
      await axiosInstance.delete(`/board/delete/${id}`)
      alert('게시글이 삭제됐습니다.')
      navigate(-1)
    } catch (err) {
      console.log('게시글 삭제 실패: ', err)
      alert()
    }
  }

  // 댓글 삭제 핸들러 (postId, commentId)
  const handleCommentDelete = async (commentId) => {
    try {
      await axiosInstance.delete(`/board/${id}/comments/${commentId}`)
      // console.log('[handleCommentDelete] 데이터 확인 : ', res)
      fetchPostDetail() //전체 리프레시
    } catch (err) {
      console.error('댓글 삭제 실패', err)
      alert('댓글 삭제에 실패했습니다.')
    }
  }

  //좋아요 토글
  const handleLike = async () => {
    try {
      if(!post.liked) {
        await axiosInstance.post(`/board/${id}/like`)
        console.log("좋아요 성공")
      } else {
        await axiosInstance.delete(`/board/${id}/like`)
        console.log("좋아요 취소 성공")
      }
      fetchPostDetail()
    } catch (err) {
      console.error("좋아요 토글 에러", err)
    }
  }

  // 댓글 작성 성공 후 전체 리프레시
  const handleCommentSubmitSuccess = () => {
    fetchPostDetail()
  }

  useEffect(() => {
    fetchPostDetail()
  }, [id])

  if (!post) return <Loading/>

  //인증됐으며, 그 인증된 사용자가 게시글 작성자인지 확인하는 것 필요?
  const isAuthor = isAuthenticated && user?.id === post.user_id

  return (
    <div className='board-detail-page-box'>
      <div className='board-detail-page-header'>
        <UndoButton/>
        <h1 className="board-category">
          {
            post.type==="free"
              ? "자유게시판"
              : post.type==="info" &&
                "정보 공유"
          }
        </h1>
      </div>
      <div className="board-detail-page">
        <div className="board-title-area">
          <h2>{post.title}</h2>
          <div className="board-meta">
            <span>{post.nickname}</span>
            <span> | {new Date(post.createdAt).toLocaleString()}</span>
          </div>
        </div>
        <div className="board-content">
          <p><PostContent content={post.content} /></p>
        </div>
        <div className="board-like-section">
          <LikeButton
            isLiked={post.liked}
            count={like}
            onClick={handleLike}
          />
        </div>
        {isAuthor && (
          <div className="board-actions">
            <button
              className="button modify"
              onClick={handlePostEdit}
            >
              수정
            </button>
            <button
              className="button delete"
              onClick={handlePostDelete}
            >
              삭제
            </button>
          </div>
        )}
        <div className="bottom-line" />

        <div className="comments-section">
          <h3>댓글 {countTotalComments(comments)}개</h3>

          {isAuthenticated ? (
            <CommentForm
              postId={id}
              onCommentSubmitSuccess={handleCommentSubmitSuccess}
            />
          ) : (
            <div 
              className="login-prompt"
              style={{alignItems: "flex-start"}}
            >
                댓글을 작성하려면 로그인해주세요.
            </div>
          )}

          <div className="comments-list">
            {comments.length > 0 ? (
              comments.map(c => (
                <CommentItem
                  key={c.commentId}
                  comment={c}
                  postUserId={post.user_id}
                  level={0}
                  onDelete={handleCommentDelete}
                  onReplyClick={setReplyingTo}
                  replyingTo={replyingTo}
                  onCommentSubmitSuccess={handleCommentSubmitSuccess}
                  postId={id}
                  // isAuthor={isAuthor}
                />
              ))
            ) : (
              <p>아직 댓글이 없습니다.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

export default BoardDetailPage
