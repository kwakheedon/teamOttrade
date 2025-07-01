import React from 'react'
import './CountrySelectorModal.css'

// 국가 선택 모달 컴포넌트
const CountrySelectorModal = ({ show, onClose, onSelect }) => {

  // 창 닫을 시 null 처리
  if(!show) return null;

  return (
    <div onClick={() => onClose()}>
      <div className='country-selector-modal-box'>
      </div>
    </div>
  )
}

export default CountrySelectorModal