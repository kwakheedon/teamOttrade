import React from 'react'
import logo from "../../assets/icons/logo_temp3.png"
import { Link } from 'react-router'

//헤더의 로고 img 컴포넌트
const Logo = () => {
  return (
    <div className='logo-box' >
      <Link to={'/'}><img src={logo} alt="로고"/></Link>
    </div>
  )
}

export default Logo