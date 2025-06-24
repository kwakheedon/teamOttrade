import React from 'react'
import AccountDeleteForm from '../../components/Auth/AccountDeleteForm'

//회원 탈퇴를 할 때 출력할 페이지
const AccountDeletePage = () => {
  return (
    <div>
      {/* 각 페이지 이동 */}
      <button>조회 이력</button>
      <button>회원 정보 수정</button>
      <button>회원 탈퇴</button>

      {/* AccountDeleteForm.jsx(회원탈퇴 폼) 호출 */}
      <AccountDeleteForm/>

    </div>
  )
}


export default AccountDeletePage