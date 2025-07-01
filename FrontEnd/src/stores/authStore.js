import axios from 'axios'
import { create } from 'zustand'

// 인증 상태를 관리하는 저장소
// 토큰은 나중에 바꿔야 함
const useAuthStore = create((set, get) => ({
    isAuthenticated: false,
    accessToken: null,
    refreshToken: null,

    //로그인 시 실행
    login: (accessToken, refreshToken) => {
        localStorage.setItem('accessToken', accessToken)
        localStorage.setItem('refreshToken', refreshToken)
        set({
            accessToken,
            refreshToken,
            isAuthenticated: true
        })
    },

    //로그아웃 시 실행
    logout: () => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
            set({
                accessToken: null,
                refreshToken: null,
                isAuthenticated: false
        })
    },

    //토큰 상태 복구용
    checkAuth: () => {
        const accessToken = localStorage.getItem('accessToken')
        const refreshToken = localStorage.getItem('refreshToken')
        if(accessToken&&refreshToken) {
            set({
                accessToken,
                refreshToken,
                isAuthenticated: true
            })
        }
    },

    //accessToken 재발급용
    refreshAuth: async () => {
        const { refreshToken } = get()
        if(!refreshToken) {
            get().logout()
            throw new Error('No refresh token')
        }
        try {
            const res = await axios.post('/api/auth/reissue', {refreshToken})
            console.log(res)
            const { accessToken: newToken, refreshToken: newRefresh } = res.data
            localStorage.setItem('accessToken', newToken)
            localStorage.setItem('refreshToken', newRefresh)
            set({ accessToken: newToken, refreshToken: newRefresh, isAuthenticated: true })
            return newToken
        } catch (err) {
            console.log(err)
            // get().logout()
            // throw err
        }
    }
}))

export default useAuthStore