import React from 'react'
import './MyPage.css'
import Sidebar from '../../components/Mypage/Sidebar'
import { Outlet } from 'react-router-dom'

// 조회 이력, 회원 수정, 회원 탈퇴 등이 들어갈 페이지
const MyPage = () => {
  return (
    <div className="mypage-container">
      <div>
        <h1 className="mypage-title">마이페이지</h1>
      </div>
      <div className='mypage-content-box'>
        <Sidebar/>
        <div className='mypage-outlet-box'>
          <Outlet/>
        </div>
      </div>
    </div>
  )
}

export default MyPage