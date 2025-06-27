import React from 'react'
import { Link } from 'react-router'
import SocialLogin from './SocialLogin'
import './AuthForm.css'

// 로그인, 회원가입 폼
const AuthForm = () => {
  return (
    <div>
      {/* 로그인 모달 창 끄는 맨 오른쪽 위 선형 버튼 */}
      <button className='button'></button>

      <h1 className='h1'>로그인</h1>

      <div className="inputBox">
        <div className="fields">
          <input type="text" placeholder="전화번호" />
          <input type="text" placeholder="비밀번호" />
        </div>
        <input type="submit" value="로그인" />
      </div>

      {/* 회원가입 페이지로 이동 */}
      <Link to="/signup">새 계정 만들기↗</Link>

      {/* SocialLogin.jsx(소셜 로그인 버튼)를 호출 */}
      <SocialLogin/>

    </div>
  )
}

export default AuthForm