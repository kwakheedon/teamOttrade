import React from 'react'

// 프로필 수정 폼
// private 파일 안에서만 사용할 것
const ProfileEditForm = () => {
  return (
    <div>
      <form action="">
        <h1>회원 정보 수정</h1>

      <input type="text" name="" id="" placeholder='전화번호'/>
      <button>인증 번호 발송</button>
      {/* 번호 발송 후 남은 시간은 어떻게 코드를 넣을까요? */}
      <input type="text" name="" id="" />

      <br />
      <input type="text" placeholder='비밀번호'/>
      <input type="text" placeholder='닉네임'/>

      <button>수정 확인</button>
      </form>
    </div>
  )
}

export default ProfileEditForm