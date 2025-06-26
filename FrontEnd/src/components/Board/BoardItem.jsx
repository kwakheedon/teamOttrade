import React from 'react'
import { Link } from 'react-router'


//게시글 요약을 보여줄 컴포넌트
const BoardItem = ({post}) => {

  return (
    <div className='post-item'>
      <span>{post.id}</span>
      <div><Link>{post.title}</Link></div>
      <div>
        <span>{post.nickname}</span>
      </div>
    </div>
  )
}

export default BoardItem