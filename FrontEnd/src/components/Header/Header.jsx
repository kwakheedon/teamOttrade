import React from 'react'
import Logo from './Logo'
import HeaderNav from './HeaderNav'
import AuthButton from '../Auth/AuthButton'

// 사이트 최상단에 배치하는 헤더 컴포넌트
const Header = () => {
  return (
    <div>
      <Logo></Logo>
      <HeaderNav></HeaderNav>
      <AuthButton></AuthButton>
    </div>
  )
}

export default Header