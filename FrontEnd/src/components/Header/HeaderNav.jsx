import React, { useState } from 'react'
import NavItem from './NavItem'

//헤더의 메뉴를 나열할 컴포넌트
const HeaderNav = () => {
  const navItems = [
    {name: "메인페이지", path: "/"},
    {name: "공지사항", path: "/notice"},
    {name: "커뮤니티", path: "/community"},
    {name: "마이페이지", path: "/mypage"}
  ]

  const [activeName, setActiveName] = useState(navItems[0].name)
  const [hoveredName, setHoveredName] = useState(null)

  // 하이라이터용 인덱스 계산: hoveredName 우선
  const highlightIndex =
    hoveredName !== null
      ? navItems.findIndex(item => item.name === hoveredName)
      : navItems.findIndex(item => item.name === activeName)

  const count = navItems.length

  return (
    <nav className="header-nav" style={{ '--nav-count': count }}>
      {navItems.map(item => (
        <NavItem
          key={item.path}
          label={item.name}
          path={item.path}
          isActive={activeName === item.name}
          onClick={() => setActiveName(item.name)}
          onMouseEnter={() => setHoveredName(item.name)}
          onMouseLeave={() => setHoveredName(null)}
        />
      ))}
      <div
        className="nav-highlighter"
        style={{ transform: `translateX(${(100 / count) * highlightIndex}%)` }}
      />
        {/* 
        <li><NavItem item={{
          name: "메인페이지",
          path: "/"
        }}/></li>
        <li><NavItem item={{
          name: "공지사항",
          path: "/notice"
        }}/></li>
        <li><NavItem item={{
          name: "커뮤니티",
          path: "/community"
        }}/></li>
        <li><NavItem item={{
          name: "마이페이지",
          path: "/mypage"
        }}/></li>
         */}
    </nav>
  )
}

export default HeaderNav