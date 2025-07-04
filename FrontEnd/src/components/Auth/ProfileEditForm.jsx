// ProfileEditForm.jsx
import React from 'react'

const ProfileEditForm = ({
  tel,
  setTel,
  sendAuth,
  showAuthInput,
  authNum,
  setAuthNum,
  timeLeft,
  verifyAuth
}) => {
  const minutes = String(Math.floor(timeLeft / 60)).padStart(2, '0')
  const seconds = String(timeLeft % 60).padStart(2, '0')

  return (
    <div className="profile-edit-container">
      <form
        className="profile-edit-form"
        onSubmit={e => e.preventDefault()}
      >
        <h1>회원 정보 수정</h1>

        {/* 1) 전화번호 입력 & 인증번호 발송 */}
        <input
          className="profile-edit-input-box"
          type="text"
          placeholder="전화번호(- 빼고 입력)"
          value={tel}
          onChange={e => setTel(e.target.value)}
        />
        <button
          type="button"
          className="send-code-btn"
          onClick={sendAuth}
          disabled={showAuthInput}
        >
          인증 번호 발송
        </button>

        {/* 2) 인증번호 입력 & 카운트다운 */}
        {showAuthInput && (
          <>
            <p className="timer">
              남은 시간 : <span>{minutes}:{seconds}</span>
            </p>
            <input
              className="profile-edit-input-box"
              type="text"
              placeholder="인증번호"
              value={authNum}
              onChange={e => setAuthNum(e.target.value)}
            />
            <button
              type="button"
              className="send-code-btn"
              onClick={verifyAuth}
              disabled={timeLeft === 0}
            >
              인증번호 확인
            </button>
          </>
        )}
      </form>
    </div>
  )
}

export default ProfileEditForm
