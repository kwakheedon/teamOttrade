// src/components/Signup/SignupForm.jsx
import React, { useEffect, useState } from 'react'
import axios from '../../apis/publicApi'
import SocialLogin from './SocialLogin'
import './SignUpForm.css'
import { useNavigate } from 'react-router-dom'
import UndoButton from '../Common/UndoButton'

const SignupForm = () => {
  const navigate = useNavigate()
  const [step, setStep] = useState(1)
  const [signupInput, setSignupInput] = useState({
    phone: "",
    password: "",
    nickname: "",
    email: ""
  })
  const [authNum, setAuthNum] = useState('')
  const [timeLeft, setTimeLeft] = useState(0)

  // 카운트다운 타이머 효과
  useEffect(() => {
    let timer
    if (step === 2) {
      setTimeLeft(120) // 2분 초기화
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

  // mm:ss 포맷으로 변환
  const formatTime = (seconds) => {
    const m = String(Math.floor(seconds / 60)).padStart(2, '0')
    const s = String(seconds % 60).padStart(2, '0')
    return `${m}:${s}`
  }

  // 1) 인증번호 요청
  const handleAuth = async () => {
    try {
      await axios.post('/auth/sms', { to: signupInput.phone })
      setStep(2)
    } catch (err) {
      console.error("handleAuth 에러", err)
      alert('인증번호 발송에 실패했습니다.')
    }
  }

  // 2) 인증번호 확인
  const handleVerify = async () => {
    try {
      await axios.post('/auth/sms/verify', {
        phoneNumber: signupInput.phone,
        verificationCode: authNum
      })
      setStep(3)
    } catch (err) {
      console.error("handleVerify 에러", err)
      alert('인증번호가 올바르지 않습니다.')
    }
  }

  // 3) 회원가입 완료
  const handleSignup = async () => {
    try {
      await axios.post('/auth/signup', {
        email: signupInput.email,
        password: signupInput.password,
        nickname: signupInput.nickname,
        phone: signupInput.phone,
      })
      alert('성공적으로 회원가입이 되었습니다. 환영합니다!')
      navigate('/')
    } catch (err) {
      console.error("handleSignup 에러", err)
      alert('회원가입에 실패했습니다.')
    }
  }

  return (
    <div className='signup-form-box'>
      <div className="signup-card">
        <UndoButton className='undo-button'/>
        <h1 className="signup-title">회원 가입</h1>
        <div className='signup-card-content'>
          {/* ① 휴대폰 입력 단계 */}
          {step === 1 && (
            <>
              <input
                type="text"
                name="phone"
                placeholder='전화번호'
                value={signupInput.phone}
                onChange={(e) => setSignupInput({ ...signupInput, phone: e.target.value })}
              />
              <button className='send-button' onClick={handleAuth}>
                인증번호 발송
              </button>
            </>
          )}

          {/* ② 인증번호 입력 단계 */}
          {step === 2 && (
            <>
              <input
                type="text"
                value={signupInput.phone}
                disabled
              />
              <div className='timer'>남은 시간: {formatTime(timeLeft)}</div>
              <input
                type="text"
                placeholder='인증번호'
                value={authNum}
                onChange={(e) => setAuthNum(e.target.value)}
              />
              <button className='send-button' onClick={handleVerify}>
                인증번호 확인
              </button>
            </>
          )}

          {/* ③ 프로필(비밀번호·닉네임·이메일) 입력 단계 */}
          {step === 3 && (
            <>
              <input
                type="password"
                name='password'
                placeholder='비밀번호'
                value={signupInput.password}
                onChange={(e) => setSignupInput({ ...signupInput, password: e.target.value })}
              />
              <input
                type="text"
                name='nickname'
                placeholder='닉네임'
                value={signupInput.nickname}
                onChange={(e) => setSignupInput({ ...signupInput, nickname: e.target.value })}
              />
              <input
                type="email"
                name='email'
                placeholder='이메일'
                value={signupInput.email}
                onChange={(e) => setSignupInput({ ...signupInput, email: e.target.value })}
              />
              <button className='send-button' onClick={handleSignup}>
                회원가입
              </button>
            </>
          )}
        </div>
        {/* Social Login 버튼은 항상 노출 */}
        <SocialLogin/>
      </div>
    </div>
  )
}

export default SignupForm
