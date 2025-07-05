import React, { useState } from 'react'
import { useNavigate } from 'react-router'
import AccountDeletePage from '../../pages/private/AccountDeletePage';

const Sidebar = () => {

    const navigate = useNavigate()
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    const handleCloseDeleteModal = () => {
        setIsDeleteModalOpen(false);
    };

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
                onClick={() => setIsDeleteModalOpen(true)}
            >
                회원 탈퇴
            </div>
            {isDeleteModalOpen &&
                <AccountDeletePage onClose={handleCloseDeleteModal}/>
            }
        </aside>
    )
}

export default Sidebar