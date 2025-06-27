import React, { useState } from 'react'
import { Link } from 'react-router'
import { motion, AnimatePresence } from "framer-motion"
import SocialLogin from './SocialLogin'
import axios from 'axios'

// 로그인, 회원가입 폼
const AuthForm = ({ closeAuthForm, isOpen }) => {

  const [loginInput, setLoginInput] = useState({})

  const loginHandler = async () => {
    try {
      // const res = await axios.post()
      console.log(loginInput)
    } catch (err) {
      console.error(err)
    }
  }

  return (
    <div className='auth-form-box'>
      {/* 로그인 모달 창 끄는 맨 오른쪽 위 선형 버튼 */}
      <motion.button
        className='close-auth-button'
        initial={{
          opacity: 0
        }}
        animate={{
          opacity: 1
        }}
        onClick={() => closeAuthForm()}>
      </motion.button>

      <div className='login-form-box'>
        <h1>로그인</h1>
        <div className='login-form'>
          <div>
            <input type="text" placeholder='전화번호' onChange={e => setLoginInput({
              ...loginInput,
              tel: e.target.value
            })}/>
            <input type="password" placeholder='비밀번호' onChange={e => setLoginInput({
              ...loginInput,
              password: e.target.value
            })}/>
          </div>
          <button onClick={loginHandler}>로그인</button>
        </div>
      </div>

      {/* 회원가입 페이지로 이동 */}
      <Link to="/signup">새 계정 만들기↗</Link>

      {/* SocialLogin.jsx(소셜 로그인 버튼)를 호출 */}
      <SocialLogin/>

    </div>
  )
}

export default AuthForm