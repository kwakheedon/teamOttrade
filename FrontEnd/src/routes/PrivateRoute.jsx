import React from 'react'
import useAuthStore from '../stores/authStore'
import { Navigate, Outlet } from 'react-router-dom';
import Loading from '../components/Common/Loading'

//오직 보호 기능만 명시하는 라우터
const PrivateRoute = () => {
  const isAuthenticated = useAuthStore(state => state.isAuthenticated)
  const loading = useAuthStore(state => state.loading)

  if(loading) {
    return <Loading/>
  }

  if(isAuthenticated) {
    return <Outlet/>
  } else {
    alert("로그인 후 이용 가능한 페이지입니다.")
    return <Navigate to="/" replace />
  }
}

export default PrivateRoute