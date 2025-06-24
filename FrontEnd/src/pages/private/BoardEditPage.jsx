import React from 'react'
import ProfileEditForm from '../../components/Auth/ProfileEditForm'

// 게시글 수정 시 이용할 페이지
// 기능 추가등을 고려했을 때 작성과 수정은 분리하는 게 좋다.
const BoardEditPage = () => {
  return (
    <div>
      <ProfileEditForm/>
    </div>
  )
}

export default BoardEditPage