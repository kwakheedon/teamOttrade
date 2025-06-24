import { create } from 'zustand'

// 인증 상태를 관리하는 저장소
// 토큰은 나중에 바꿔야 함
const useAuthStore = create((set) => ({
    isAuthenticated: false,
    accessToken: null,

    //로그인 시 실행
    login: (token) => {
        localStorage.setItem('accessToken', token)
        set({
            isAuthenticated: true,
            accessToken: token
        })
    },

    //로그아웃 시 실행
    logout: () => {
        localStorage.removeItem('accessToken')
            set({
            isAuthenticated: false,
            accessToken: null
        })
    },

    //토큰 상태 복구용
    checkAuth: () => {
        const token = localStorage.getItem('accessToken')
        if(token) {
            set({
                isAuthenticated: true,
                accessToken: token
            })
        }
    }
}))

export default useAuthStore