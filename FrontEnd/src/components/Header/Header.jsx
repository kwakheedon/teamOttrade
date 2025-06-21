import React from 'react'
import Logo from './Logo'
import HeaderNav from './HeaderNav'
import AuthButton from '../Auth/AuthButton'

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