import React from 'react'
import SocialLogin from './SocialLogin'

// 회원가입 폼(페이지)
const SignUpForm = () => {
  return (
    <div>
      {/* 화살표 눌러서 뒤로가기 */}
      <button>화살표</button>

      <input type="text" name="" id="" placeholder='전화번호'/>
      <button>인증 번호 발송</button>
      {/* 번호 발송 후 남은 시간은 어떻게 코드를 넣을까요? */}
      <input type="text" name="" id="" />

      <br />
      <input type="text" placeholder='비밀번호'/>
      <input type="text" placeholder='닉네임'/>

      {/* SocialLogin.jsx(소셜 로그인 버튼)를 호출 */}
      <SocialLogin/>

    </div>
  )
}

export default SignUpForm