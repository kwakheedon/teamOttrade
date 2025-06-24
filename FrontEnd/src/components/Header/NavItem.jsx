import React from 'react'
import { Link } from 'react-router'

//HeaderNav에 들어갈 메뉴 요소를 정의하는 컴포넌트
const NavItem = ({item}) => {
  return (
    <div>
      <Link to={item.path}>{item.name}</Link>
    </div>
  )
}

export default NavItem