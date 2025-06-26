import React from 'react'
import { useNavigate } from 'react-router'

//회원 정보 삭제 폼
//private 파일 안에서만 사용할 것
const AccountDeleteForm = () => {

  const navigate = useNavigate();

  const y = () => {
    navigate('/')
  }

  const n = () => {
    navigate('/profile/delete')
  }

  return (
    <div>
      <h1>탈퇴하시겠습니까?</h1>

      {/* '네' 클릭 시 메인화면으로 이동
          '아니오' 클릭 시 페이지 그대로 */}
      <button onClick={y}>네</button>
      <button onClick={n}>아니오</button>
    </div>
  )
}

export default AccountDeleteForm