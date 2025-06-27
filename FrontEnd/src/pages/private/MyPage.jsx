import React from 'react'
import './MyPage.css'

// 조회 이력, 회원 수정, 회원 탈퇴 등이 들어갈 페이지
const MyPage = () => {
  return (
    <div className="mypage-container">
      <aside className="sidebar">
        <h1 className="title">마이페이지</h1>
        
        <button className="side-btn">조회 이력</button>
        <button className="side-btn">회원 정보 수정</button>
        <button className="side-btn">회원 탈퇴</button>
      </aside>
    </div>
  )
}

export default MyPage