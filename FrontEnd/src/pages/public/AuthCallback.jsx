import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuthStore from '../../stores/authStore'
import Loading from '../../components/Common/Loading'

const AuthCallback = () => {
    const navigate = useNavigate()

    const params = new URLSearchParams(window.location.search);
    const login = useAuthStore((state) => state.login)

    useEffect(() => {
        //쿼리 파라미터를 불러온다.
        console.log(params)
        const accessToken = params.get('accessToken')
        const refreshToken = params.get('refreshToken')

        if (!accessToken || !refreshToken) {
            alert('일반적인 접근이 아닙니다.')
            navigate('/', { replace: true })
            return
        }

        if(accessToken && refreshToken) {
            console.log('구글 로그인 성공!!!!! 토큰 로컬스토리지에 저장 (테스트용)')
            console.log(refreshToken)
            login(accessToken, refreshToken)
            navigate('/', { replace: true })
        }
    }, [])

    return (
        <Loading/>
    )
}

export default AuthCallback