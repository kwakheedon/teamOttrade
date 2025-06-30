import React, { useState } from 'react'
import axios from 'axios'
import { Link } from 'react-router'
import SocialLogin from './SocialLogin'
import './AuthForm.css'
import useAuthStore from '../../stores/authStore'

// 로그인, 회원가입 폼
const AuthForm = ({ closeAuthForm }) => {

  const [loginInput, setLoginInput] = useState({ phone: "", password: "" })
  const login = useAuthStore((state) => state.login)

  const loginHandler = async () => {
    try {
      const res = await axios.post("/api/auth/login", loginInput)
      console.log(res.data)
      const { accessToken, refreshToken } = res.data //구조 분해 할당
      login(accessToken, refreshToken)
      closeAuthForm()
    } catch (err) {
      console.error(err)
    }
  }

  return (
    <div className='auth-form-box'>
      {/* 로그인 모달 창 끄는 맨 오른쪽 위 선형 버튼 */}
      <div className='close-auth-box' onClick={() => closeAuthForm()}>
        <button></button>
      </div>

      <h1 className='h1'>로그인</h1>

      <div className="inputBox">
        <div className="fields">
          <input type="text" placeholder="전화번호" onChange={(e) => {
            setLoginInput({
              ...loginInput,
              phone: e.target.value
            })
          }} />
          <input type="password" placeholder="비밀번호" onChange={(e) => {
            setLoginInput({
              ...loginInput,
              password: e.target.value
            })
          }} />
        </div>
        <button
          className='login-button'
          type="submit"
          onClick={loginHandler}
        >로그인
        </button>
      </div>

      {/* 회원가입 페이지로 이동 */}
      <Link
        to="/signup"
        className='signup-component'
      >새 계정 만들기↗
      </Link>

      {/* SocialLogin.jsx(소셜 로그인 버튼)를 호출 */}
      <SocialLogin/>

    </div>
  )
}

export default AuthForm