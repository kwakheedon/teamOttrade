import React from 'react'
import { Link } from 'react-router'

//HeaderNav에 들어갈 메뉴 요소를 정의하는 컴포넌트
const NavItem = ({ label, path, isActive, onClick, onMouseEnter, onMouseLeave }) => (
  <div
    className={`nav-item ${isActive ? 'active' : ''}`}
    onClick={onClick}
    onMouseEnter={onMouseEnter}
    onMouseLeave={onMouseLeave}
  >
    <Link to={path}>{label}</Link>
  </div>
)

export default NavItem