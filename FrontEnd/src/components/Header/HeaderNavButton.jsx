import React from 'react'
import { FancySwitch } from '@omit/react-fancy-switch'
import { useLocation, useNavigate } from 'react-router'
import CustomFancySwitch from './CustomFancySwitch'

export default function HeaderNavButton() {
  const navigate = useNavigate()
  const location = useLocation()

  const handleChange = (path) => {
    if (path !== location.pathname) {
      navigate(path)
    }
  }

  const options = [
    { value: '/', label: '메인페이지' },
    { value: '/notice',  label: '공지사항' },
    { value: '/community',  label: '커뮤니티' },
    { value: '/mypage',  label: '마이페이지' }
  ]

  return (
    <div className='header-nav-button-box'>
      <CustomFancySwitch
        options={options}
        value={location.pathname}
        onChange={handleChange}
        className="button-switch-container"
        radioClassName="button-switch-radio"
        highlighterClassName="button-switch-highlighter"
      />
      {/* <p>현재 상태: <strong>{value.toUpperCase()}</strong></p> */}
    </div>
  )
}
