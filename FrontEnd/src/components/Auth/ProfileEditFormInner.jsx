// ProfileEditFormInner.jsx
import React from 'react'

const ProfileEditFormInner = ({
    currentPassword,
    setCurrentPassword,
    newPassword,
    setNewPassword,
    confirmNewPassword,
    setConfirmNewPassword,
    nickname,
    setNickname,
    editProfile
}) => {
    return (
        <div className='profile-edit-form-inner-box'>
        <input
            className='profile-edit-input-box'
            type="password"
            placeholder="기존 비밀번호"
            value={currentPassword}
            onChange={e => setCurrentPassword(e.target.value)}
        />
        <input
            className='profile-edit-input-box'
            type="password"
            placeholder="새 비밀번호"
            value={newPassword}
            onChange={e => setNewPassword(e.target.value)}
        />
        <input
            className='profile-edit-input-box'
            type="password"
            placeholder="새 비밀번호 확인"
            value={confirmNewPassword}
            onChange={e => setConfirmNewPassword(e.target.value)}
        />
        <input
            className='profile-edit-input-box'
            type="text"
            placeholder="새 닉네임"
            value={nickname}
            onChange={e => setNickname(e.target.value)}
        />

        <button
            type="button"
            className="submit-btn"
            onClick={editProfile}
        >
            수정 확인
        </button>
        </div>
    )
}

export default ProfileEditFormInner
