import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import './PreviewList.css'

//게시판 미리보기 컴포넌트
//더보기 버튼은 컴포넌트 따로 나눌지 안 나눌지 고민 해봐야할듯
const PreviewList = ({ dataList, type }) => {
  const [posts, setPosts] = useState([])

  const fetchPosts = async () => {
    try {
      const response = await axios.get('/api/board', {
        params: {
          type,
          size: 5
        }
      })
      // console.log("[PreviewList] 데이터 체크: ", response.data.data.content)
      setPosts(response.data.data.content)
    } catch (err) {
      console.error(err)
    }
  }

  useEffect(() => {
    fetchPosts()
  }, [])

  return (
    <div className="board-column">
      <div className="board-header">
        <h2>{dataList.title}</h2>
        <Link to={dataList.path}>더보기 &gt;</Link>
      </div>

      <div className="board-list">
        <div style={{ borderRadius: '10px', overflow: 'hidden' }}>
          {posts.map((post, idx) => (
            <div className="board-list-row" key={idx}>
              <Link to={`/board/${post.id}`} className="board-list-row-link">
                <span className="post-title">{post.title}</span>
                <span className="post-userId">{post.user_id}</span>
                <span className="post-comments">💬 {post.comments ? post.comments : 0} </span>
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

export default PreviewList