import React from 'react'
import './CountrySelectorModal.css'
import countryList from '../../assets/data/countryMap.json'

// 국가 선택 모달 컴포넌트
const CountrySelectorModal = ({ show, onClose, onSelect }) => {

  // 창 닫을 시 null 처리
  if(!show) return null;

  return (
    <div
      className='modal-overlay'
      onClick={() => onClose()}
    >
      <div
        className='country-selector-modal-box'
        onClick={(e) => e.stopPropagation()}
      >
        {countryList.map((country) => {
          const code = Object.keys(country)[0]
          const {statKor, imgPath} = country[code]
          return (
            <div
              key={code}
              data-code={code}
              className='country-box'
              onClick={onSelect}
            >
              <img src={imgPath} alt={code} />
              {statKor}
            </div>
          )
        })}
      </div>
    </div>
  )
}

export default CountrySelectorModal