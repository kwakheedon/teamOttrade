import React from 'react'
import useAuthStore from '../../stores/authStore'

// 헤더의 로그인/회원가입 버튼
// zustand로 로그인 여부를 파악하고 로그인/로그아웃 전환
const AuthButton = () => {
  const { isAuthenticated } = useAuthStore();

  const handleClick = () => {
    
  }

  return (
    <button onClick={handleClick}>
      {isAuthenticated? '로그아웃' : '로그인'}
    </button>
  )
}

export default AuthButton