import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import './BoardItem.css'
import axios from 'axios';

//게시글 요약을 보여줄 컴포넌트
const BoardItem = () => {

  const [posts, setPosts] = useState([]);

  const fetchPosts = async () => {
    try {
      const response = await axios.get('/api/board?type=free')
  
      setPosts(response.data.data.content)
      console.log("fetchPosts 실행확인: ",posts)

    } catch(err) {
      console.error(err)
    }
  }

  useEffect(() => {
    fetchPosts()
  }, [])

  return (
    <div className='post-item'>
      {posts.slice(0, 5).map((post, idx) => (
        <div key={idx}>
          <Link to={`/board/${post.id}`} className="board-list-row-link">
            <div className="board-list-row">
              <span className="post-title">{post.title}</span>
              <span className="post-userId">{post.user_id}</span>
              <span className="post-comments">
                <i className="comment-icon">💬</i> {post.comments}
              </span>
            </div>
          </Link>
        </div>
      ))}
    </div>
  )
}

export default BoardItem