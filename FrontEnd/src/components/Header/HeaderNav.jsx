import React from 'react'
import { Link } from 'react-router'

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