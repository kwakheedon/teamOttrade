import React from 'react'
import './MainPage.css'

// 서비스의 기본 페이지
const MainPage = () => {
  return (
    <div className='main'>
      <h1 className="main-text">
          품목만 입력하면<br />
          수출 전략이 완성됩니다
      </h1>

      <input className="main-input" type="text" name="" id="" />

      <button className='main-iconbtn'></button>

      <div className="main-bottombar"></div>
    </div>
  )
}

export default MainPage