import React, { useRef } from 'react'
import './MainPage.css'
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

      <section className="section-bottom" ref={scrollTargetRef}>
        <TotalMembersBox />
        <TotalPostsBox />
      </section>
    </div>
  )
}

export default MainPage