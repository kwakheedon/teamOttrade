import React from 'react'
import './ProfileEditForm.css'

// 프로필 수정 폼
// private 파일 안에서만 사용할 것
const ProfileEditForm = () => {
  return (
    <div className="profile-edit-container">
      <form className="profile-edit-form">
        <h1>회원 정보 수정</h1>

        <input type="text" placeholder="전화번호" />
        <button type="button" className="send-code-btn">인증 번호 발송</button>
        <p className="timer">남은 시간 : <span>00:00</span></p>
        <input type="text" placeholder="인증번호" />

        <input type="password" placeholder="비밀번호" />
        <input type="text" placeholder="닉네임" />

        <button type="submit" className="submit-btn">수정 확인</button>
      </form>
    </div>
  );
};


export default ProfileEditForm