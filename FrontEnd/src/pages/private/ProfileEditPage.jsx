import React from 'react'
import ProfileEditForm from '../../components/Auth/ProfileEditForm'

//사용자의 프로필을 수정 컴포넌트를 넣을 수정 페이지
const ProfileEditPage = () => {
  return (
    <div>
      {/* 각 페이지 이동 */}
      <button>조회 이력</button>
      <button>회원 정보 수정</button>
      <button>회원 탈퇴</button>

      {/* ProfileEditForm.jsx(프로필 수정 폼) 호출 */}
      <ProfileEditForm/>

    </div>
  )
}

export default ProfileEditPage