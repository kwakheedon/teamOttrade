import React from 'react'
import NoticeWritePage from '../pages/admin/NoticeWritePage'
import AdminRoute from './AdminRoute'

//관리자 권한이 필요한 페이지 모음
const AdminRoutes = () => {
  return (
    <div>
      {/* 공지 작성 페이지 */}
      <Route path='/noticewrite' element={<AdminRoute><NoticeWritePage/></AdminRoute>}/>
    </div>
  )
}

export default AdminRoutes