// src/pages/public/AuthCallback.jsx
import React, { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import useAuthStore from '../../stores/authStore'
import Loading from '../../components/Common/Loading'

const AuthCallback = () => {
    const navigate = useNavigate()
    const [searchParams] = useSearchParams(); // useSearchParams 훅 사용
    const login = useAuthStore((state) => state.login)

    useEffect(() => {
        // searchParams.get()을 사용하여 쿼리 파라미터 값을 가져옵니다.
        const accessToken = searchParams.get('accessToken')
        const refreshToken = searchParams.get('refreshToken')

        if (!accessToken || !refreshToken) {
            alert('잘못된 접근입니다. 토큰 정보가 없습니다.')
            navigate('/', { replace: true })
            return
        }

        if(accessToken && refreshToken) {
            console.log('Google 로그인 성공! 토큰을 로컬 스토리지에 저장합니다.')
            login(accessToken, refreshToken)
            navigate('/', { replace: true })
        }
    }, [searchParams, login, navigate]) // 의존성 배열에 searchParams, login, navigate 추가

    return (
        <Loading/>
    )
}

export default AuthCallback