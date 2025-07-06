import axios from 'axios';
import useAuthStore from '../stores/authStore';

const instance = axios.create({
    baseURL: '/api',
    timeout: 5000000,
});

//요청 시 헤더에 accessToken 삽입
instance.interceptors.request.use(config => {
    const token = useAuthStore.getState().accessToken
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    } else {
        // 토큰이 없으면 이전에 남아있을 헤더를 삭제
        delete config.headers.Authorization
    }
    return config
})

//accessToken만료시 refreshToken으로 토큰 요청 (401 응답 캐치)
instance.interceptors.response.use(res => 
    res,
    async err => {
        const origReq = err.config
        const status = err.response?.status

        if (
            (status === 401 || status === 500) &&
            !origReq._retry &&
            !origReq.url.includes('/auth/reissue')
        ) {
            origReq._retry = true
            try {
                console.log("accessToken만료, refreshToken 재발급 실행")
                const newToken = await useAuthStore.getState().refreshAuth()
                instance.defaults.headers.common.Authorization = `Bearer ${newToken}`
                origReq.headers.Authorization = `Bearer ${newToken}`
                return instance(origReq)
            } catch (refreshErr) {
                // refresh 실패 시 자동 로그아웃 처리
                return Promise.reject(refreshErr)
            }
        }
        return Promise.reject(err)
    }
)

export default instance;