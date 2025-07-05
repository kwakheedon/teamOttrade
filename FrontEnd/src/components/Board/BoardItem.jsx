import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import './BoardItem.css'
import eye_icon from '../../assets/icons/eye_icon.png'
import axios from '../../apis/authApi';

//게시글 요약을 보여줄 컴포넌트
const BoardItem = ({ type }) => {

  const [posts, setPosts] = useState([]);

  const fetchPosts = async () => {
    try {
      const response = await axios.get('/board', {
        params: {
          type,
          size: 5
        }
      })
      setPosts(response.data.data.content)
      console.log("BoardItem 체크: ", response.data.data.content)
    } catch(err) {
      console.error(err)
    }
  }

  useEffect(() => {
    fetchPosts()
  }, [])

  return (
    <div className='post-item'>
      {posts.map((post, idx) => (
        <div key={idx}>
          <Link to={`/board/${post.id}`} className="board-list-row-link">
            <div className="board-list-row">
              <span className="post-title">{post.title}</span>
              <span className="post-userId">{post.user_id}</span>
              <span className="post-comments">
                <img src={eye_icon} alt="view" style={{ width: '20px', opacity: '0.3'}}/>
                {post.view_count} 
              </span>
            </div>
          </Link>
        </div>
      ))}
    </div>
  )
}

export default BoardItem