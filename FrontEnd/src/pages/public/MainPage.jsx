import React from 'react'
import { Link } from 'react-router'

const Main = () => {
  return (
    <div>
        <h1>품목만 입력하면</h1>
        <h1>수출 전략이 완성됩니다</h1>
        <input type="text" />
        <br />
        <Link to={"/"}>보내기</Link>
    </div>
  )
}

export default Main