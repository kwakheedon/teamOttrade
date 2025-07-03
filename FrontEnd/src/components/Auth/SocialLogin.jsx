import React from 'react'
import google from "../../assets/icons/social_icon/google_icon.jpg"
import './AuthForm.css'

// 소셜 로그인버튼을 저장할 컴포넌트
const SocialLogin = () => {
  return (
    <div className='SocialLogin'>
      <div onClick={() => { window.open('http://localhost:8088/api/oauth2/authorization/google', '_blank') }}>
        <img src={google} alt="구글" />
      </div>
      <div>
        <img src="" alt="네이버" />
      </div>
      <div>
        <img src="" alt="카카오" />
      </div>
    </div>
  )
}

export default SocialLogin