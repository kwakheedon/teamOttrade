import React, { useRef } from 'react'
import './MainPage.css' //
import MainToper from '../../components/MainSummary/MainToper'
import TotalMembersBox from '../../components/MainSummary/TotalMembersBox'
import TotalPostsBox from '../../components/MainSummary/TotalPostsBox'

const MainPage = () => {
  const scrollTargetRef = useRef(null);

  const handleScrollClick = () => {
    scrollTargetRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div className="main-page">
      <section className="section-top">
        <MainToper onScrollClick={handleScrollClick} />
      </section>

      {/* --- scrollBtn 내부를 채워줍니다 --- */}
      <button className='scrollBtn' onClick={handleScrollClick}>
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M19 9l-7 7-7-7" stroke="#3b3b3b" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      </button>

      <section className="section-bottom" ref={scrollTargetRef}>
        <TotalMembersBox />
        <TotalPostsBox />
      </section>
    </div>
  )
}

export default MainPage