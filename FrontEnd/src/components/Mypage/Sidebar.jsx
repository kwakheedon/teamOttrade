import React, { useState } from 'react'
import { NavLink } from 'react-router-dom'
import './Sidebar.css'
import AccountDeletePage from '../../pages/private/AccountDeletePage'

export default function Sidebar() {
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)

  return (
    <aside className="sidebar-box">
      {/* 조회 이력 */}
      <NavLink
        to="/mypage/history"
        className={({ isActive }) =>
          `side-btn${isActive ? ' selected' : ''}`
        }
      >
        조회 이력
      </NavLink>

      {/* 회원 정보 수정 */}
      <NavLink
        to="/mypage/edit"
        className={({ isActive }) =>
          `side-btn${isActive ? ' selected' : ''}`
        }
      >
        회원 정보 수정
      </NavLink>

      {/* 회원 탈퇴 (경로 이동 없이 모달만) */}
      <div
        className={`side-btn${isDeleteModalOpen ? ' selected' : ''}`}
        onClick={() => setIsDeleteModalOpen(true)}
      >
        회원 탈퇴
      </div>

      {isDeleteModalOpen && (
        <AccountDeletePage
          onClose={() => setIsDeleteModalOpen(false)}
        />
      )}
    </aside>
  )
}
