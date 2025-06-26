import React, { useState } from 'react'
import BoardItem from './BoardItem'
import dummyList from '../../assets/data/dummy.json'

//BoardItem 컴포넌트를 리스트로 출력할 컴포넌트
const BoardList = () => {
  const [posts, setPosts] = useState(dummyList)

  return (
    <div className='board-list-box'>
      {posts.map((post) => 
        <BoardItem key={post.id} post={post}/>
      )}
    </div>
  )
}

export default BoardList