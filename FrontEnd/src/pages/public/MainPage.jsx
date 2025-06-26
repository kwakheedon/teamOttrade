import React from 'react'
import './MainPage.css'
import MainToper from '../../components/MainSummary/MainToper'
import TotalMembersBox from '../../components/MainSummary/TotalMembersBox'
import TotalPostsBox from '../../components/MainSummary/TotalPostsBox'

// 서비스의 기본 페이지
const MainPage = () => {
  return (
    <div>
      <MainToper/>
      <TotalMembersBox/>
      <TotalPostsBox/>
    </div>
  )
}

export default MainPage