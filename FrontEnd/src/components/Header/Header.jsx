import React, { useEffect, useState } from 'react'
import throttle from 'lodash/throttle'
import './Header.css'
import Logo from './Logo'
import HeaderNav from './HeaderNav'
import AuthButton from '../Auth/AuthButton'
import HeaderNavButton from './HeaderNavButton'

// 사이트 최상단에 배치하는 헤더 컴포넌트
const Header = () => {
  const [scrolled, setScrolled] = useState(false)

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 0)
    }
    window.addEventListener('scroll', handleScroll)

    //200ms 에 한번만 실행 되도록
    const throttled = throttle(handleScroll, 200)

    //처음에 스크롤이 됐는지 확인하는 용
    handleScroll()

    //메모리 누수 방지를 위해 언마운트시 리스너 제거
    return () => {
      window.removeEventListener('scroll', handleScroll)
      throttled.cancel()
    }
  }, [])

  return (
    <header className={`${scrolled? 'scrolled' : ''}`}>
      <div className='header-box'>
        <Logo/>
        <HeaderNavButton/>
        {/* <HeaderNav/> */}
        <AuthButton/>
      </div>
    </header>
  )
}

export default Header