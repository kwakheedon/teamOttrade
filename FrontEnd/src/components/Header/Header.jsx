import React from 'react'
import './Header.css'
import Logo from './Logo'
import NavItem from './NavItem'
import AuthButton from '../Auth/AuthButton'

// 사이트 최상단에 배치하는 헤더 컴포넌트
const Header = () => {
  return (
    <header>
      <Logo/>
      <ul className='nav-list'>
        <li><NavItem item={"공지사항"}/></li>
        <li><NavItem item={"커뮤니티"}/></li>
        <li><NavItem item={"QnA"}/></li>
        <li><NavItem item={"마이페이지"}/></li>
      </ul>
      <AuthButton/>
    </header>
  )
}

export default Header