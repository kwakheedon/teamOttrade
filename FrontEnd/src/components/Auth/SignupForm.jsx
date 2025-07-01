import React, { useState } from 'react'
import axios from 'axios'
import SocialLogin from './SocialLogin'
import './SignUpForm.css'

// 회원가입 폼(페이지)
const SignupForm = () => {

  const [signupInput, setSignupInput] = useState({
    phone: "",
    password: "",
    nickname: "",
    email: ""
  })

  const clickHandler = async () => {
    const path = "/api/auth/signup"
    try {
      const res = await axios.post(path, signupInput)
      console.log(res.data)
    } catch (err) {
      console.error(err)
    }
  }
  return (
    <div className='signup-form-box'>
      <div className="signup-card">

        {/* 화살표 눌러서 뒤로가기 */}
        <button
          className='signup-to-main-button'
        >←
        </button>

        <h1 className="signup-title">회원 가입</h1>
        <input type="text" name="phone" id="" placeholder='전화번호' onChange={(e) => {
            setSignupInput({
              ...signupInput,
              phone: e.target.value
            })}}/>
        <button className='send-button'>인증 번호 발송</button>
        {/* 번호 발송 후 남은 시간은 어떻게 코드를 넣을까요? */}
        <input type="text" name="" id="" />

        <br />
        <input type="text" name='password' placeholder='비밀번호' onChange={(e) => {
          setSignupInput({
            ...signupInput,
            password: e.target.value
          })}}/>
        <input type="text" name='nickname' placeholder='닉네임' onChange={(e) => {
          setSignupInput({
            ...signupInput,
            nickname: e.target.value
          })}}/>
        <input type="text" name='email' placeholder='이메일'  onChange={(e) => {
          setSignupInput({
            ...signupInput,
            email: e.target.value
          })}}/>

        <button onClick={clickHandler} className='send-button'>회원가입</button>

        {/* SocialLogin.jsx(소셜 로그인 버튼)를 호출 */}
        <SocialLogin/>
      </div>

    </div>
  );
};

export default SignupForm
