import React from 'react'

//글쓰기 기능을 담당하는 폼 컴포넌트
const BoardForm = () => {
  return (
    <div>
      <h1>제목</h1>
      <input type="text" placeholder='내용을 작성하세요.'/>
    </div>
  )
}

export default BoardForm