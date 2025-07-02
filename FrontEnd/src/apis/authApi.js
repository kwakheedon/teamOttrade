import axios from 'axios';
import useAuthStore from '../stores/authStore';

const instance = axios.create({
    baseURL: '/api',
    timeout: 5000000,
});

//요청 시 헤더에 accessToken 삽입
instance.interceptors.request.use((config) => {
    const token = useAuthStore.getState().accessToken;
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

//accessToken만료시 refreshToken으로 토큰 요청 (401 응답 캐치)
instance.interceptors.response.use(res => 
    res,
    async err => {
        const origReq = err.config
        if (
            err.response?.status === 401 &&
            !origReq._retry &&
            !origReq.url.includes('/auth/reissue')
        ) {
            origReq._retry = true
            try {
                const newToken = await useAuthStore.getState().refreshAuth()
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