import React from 'react'
import { useNavigate } from 'react-router'

const Sidebar = () => {

    const navigate = useNavigate()
//history, edit, delete
    return (
        <aside className="sidebar-box">
            <div
                className="side-btn"
                onClick={() => navigate('/mypage/history')}
            >
                조회 이력
            </div>

            <div
                className="side-btn"
                onClick={() => navigate('/mypage/edit')}
            >
                회원 정보 수정
            </div>
            <div
                className="side-btn"
                onClick={() => navigate('/mypage/delete')}
            >
                회원 탈퇴
            </div>
        </aside>
    )
}

export default Sidebar