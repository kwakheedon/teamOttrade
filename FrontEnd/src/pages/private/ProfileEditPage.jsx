import React, { useState, useEffect } from 'react'
import '../../components/Auth/ProfileEditForm.css'
import '../../components/Auth/ProfileEditFormInner.css'
import ProfileEditForm from '../../components/Auth/ProfileEditForm'
import ProfileEditFormInner from '../../components/Auth/ProfileEditFormInner'
import axios from '../../apis/authApi';

const ProfileEditPage = () => {
  const [tel, setTel] = useState('')
  const [authNum, setAuthNum] = useState('')
  const [currentPassword, setCurrentPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [confirmNewPassword, setConfirmNewPassword] = useState('')
  const [nickname, setNickname] = useState('')

  const [step, setStep] = useState(1)            // 1: 전화번호 발송 전, 2: 인증번호 입력 중, 3: 인증 완료
  const [timeLeft, setTimeLeft] = useState(0)    // 남은 시간(초)

  // 카운트다운
  useEffect(() => {
    let timer
    if (step === 2) {
      setTimeLeft(120) // 2분(120초)으로 초기화
      timer = setInterval(() => {
        setTimeLeft(prev => {
          if (prev <= 1) {
            clearInterval(timer)
            return 0
          }
          return prev - 1
        })
      }, 1000)
    }
    return () => clearInterval(timer)
  }, [step])

  // 1) 인증번호 요청
  const handleAuth = async () => {
    try {
      await axios.post('/auth/sms', { to: tel })
      setStep(2)
    } catch (err) {
      console.error("handleAuth 에러", err)
    }
  }

  // 2) 인증번호 확인
  const handleVerify = async () => {
    try {
      await axios.post('/auth/sms/verify', { phoneNumber: tel, verificationCode: authNum })
      setStep(3)
    } catch (err) {
      console.error("handleVerify 에러", err)
    }
  }

  // 3) 프로필 수정
  const handleEdit = async () => {
    console.log(nickname, currentPassword, newPassword, confirmNewPassword)
    try {
      await axios.put('/auth/me', {
        nickname: nickname,
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmNewPassword: confirmNewPassword
      })
      alert('프로필이 성공적으로 수정되었습니다.')
      //네이게이트 말고 리다이렉트하는걸로 
    } catch (err) {
      console.error("handleEdit 에러", err)
    }
  }

  return (
    <div>
      <ProfileEditForm
        tel={tel}
        setTel={setTel}
        sendAuth={handleAuth}
        showAuthInput={step === 2}
        authNum={authNum}
        setAuthNum={setAuthNum}
        timeLeft={timeLeft}
        verifyAuth={handleVerify}
      />

      {step === 3 && (
        <ProfileEditFormInner
          currentPassword={currentPassword}
          setCurrentPassword={setCurrentPassword}
          newPassword={newPassword}
          setNewPassword={setNewPassword}
          confirmNewPassword={confirmNewPassword}
          setConfirmNewPassword={setConfirmNewPassword}
          nickname={nickname}
          setNickname={setNickname}
          editProfile={handleEdit}
        />
      )}
    </div>
  )
}

export default ProfileEditPage
