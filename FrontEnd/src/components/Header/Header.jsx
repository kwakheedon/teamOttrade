import React from 'react'
import './Header.css'
import Logo from './Logo'
import HeaderNav from './HeaderNav'
import AuthButton from '../Auth/AuthButton'

// 사이트 최상단에 배치하는 헤더 컴포넌트
const Header = () => {
  return (
    <header>
      <div className='header-box'>
        <Logo/>
        <HeaderNav/>
        <AuthButton/>
      </div>
    </header>
  )
}

export default Header