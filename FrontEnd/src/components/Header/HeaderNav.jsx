import React from 'react'
import { Link } from 'react-router'

//헤더의 메뉴를 나열할 컴포넌트
const HeaderNav = () => {
  return (
    <div>
      <Link to={"/NoticePage"}>공지사항</Link>
      <Link to={"/QnAPage"}>QnA</Link>
      <Link to={"/CommunityPage"}>커뮤니티</Link>

    </div>
  )
}

export default HeaderNav