import React from 'react'
import NavItem from './NavItem'

//헤더의 메뉴를 나열할 컴포넌트
const HeaderNav = () => {
  return (
    <div>
      <ul className='nav-list'>
        <li><NavItem item={{
          name: "공지사항",
          path: "/notice"
        }}/></li>
        <li><NavItem item={{
          name: "커뮤니티",
          path: "/community"
        }}/></li>
        <li><NavItem item={{
          name: "QnA",
          path: "/qna"
        }}/></li>
        <li><NavItem item={{
          name: "마이페이지",
          path: "/mypage"
        }}/></li>
      </ul>
    </div>
  )
}

export default HeaderNav