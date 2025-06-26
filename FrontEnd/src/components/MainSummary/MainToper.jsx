import React from 'react'


const MainToper = ({ onScrollClick }) => {
    
    return (
        <div className='main-box'>
            <h1 className="main-text">
                품목만 입력하면<br />
                수출 전략이 완성됩니다
            </h1>

            <input className="input" type="text" />
            <p className='input-line'></p>

            <button className='iconBtn'></button>

            {/* 스크롤 버튼 */}
            <button className='scrollBtn' onClick={onScrollClick}></button>
        </div>
    )
}

export default MainToper
