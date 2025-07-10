import React, { useState } from 'react'
import { Navigate, useLocation, useNavigate, useParams } from 'react-router-dom'
import BoardForm from '../../components/Board/BoardForm'
import Loading from '../../components/Common/Loading'
import axios from '../../apis/authApi'

// 게시글 수정 시 이용할 페이지
// 기능 추가등을 고려했을 때 작성과 수정은 분리하는 게 좋다.
const BoardEditPage = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const { id } = useParams()

  const { post, type } = location.state || {}
  const [title, setTitle] = useState(post?.title || '')
  const [content, setContent] = useState(post?.content || '')

  if(!post || !type) {
    return <Navigate to='/community' replace/>
  }

  const handleEdit = async () => {
    try {
      await axios.put('/board/update', {
          id,
          title,
          content
      })
      navigate(-1, {replace: true})
    } catch (err) {
      console.error("[handleEdit] 실행 실패: ",err)
    }
  }

  if(!title || !content || !type) {
    return <Loading/>
  }

  return (
  <div className="write-container">
    <h1 className="write-title">
      { type==="free"? "자유게시판" : "정보 공유" }
    </h1>
    <div className='write-content-box'>
      <BoardForm
        title={title}
        content={content}
        setTitle={setTitle}
        setContent={setContent}
      />
        <div className='content-save-or-canceled-box'>
          <button onClick={()=>navigate(-1)}> 취소 </button>
          <button onClick={handleEdit}> 수정 </button>
      </div>
    </div>
  </div>
  )
}

export default BoardEditPage