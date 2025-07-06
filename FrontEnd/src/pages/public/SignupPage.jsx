import React from 'react'
import './SignupPage.css'
import SignupForm from '../../components/Auth/SignupForm'
import useAuthStore from '../../stores/authStore'
import { useNavigate } from 'react-router-dom'

const SignupPage = () => {
    const isAuthenticated = useAuthStore((state)=>state.isAuthenticated)
    const navigate = useNavigate()

    if(isAuthenticated) {
        alert("이미 로그인 상태이므로 회원가입 페이지를 이탈합니다.")
        navigate('/', { replace: true })
    }

    return (
        <div className='signup-page-box'>
            <SignupForm/>
        </div>
    )
}

export default SignupPage