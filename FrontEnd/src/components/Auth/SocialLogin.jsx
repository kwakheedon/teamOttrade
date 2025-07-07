import React from 'react'
import google from "../../assets/icons/social_icon/google_icon.jpg"
import naver from "../../assets/icons/social_icon/naver_icon.png"
import kakao from "../../assets/icons/social_icon/kakao_icon.png"
import './AuthForm.css'

// 소셜 로그인버튼을 저장할 컴포넌트
const SocialLogin = () => {
  return (
    <div className='SocialLogin'>
      <div onClick={() => { 
        window.open('http://49.50.135.249:8088/api/oauth2/authorization/google',
          '_blank')
        }}
      >
        <img src={google} alt="구글" />
      </div>
      <div>
        <img src={naver} alt="네이버" />
      </div>
      <div>
        <img src={kakao} alt="카카오" />
      </div>
    </div>
  )
}

export default SocialLogin