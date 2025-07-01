import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router'
import './BoardItem.css'
import axios from 'axios';
// import './BoardPage.css'


//게시글 요약을 보여줄 컴포넌트
const BoardItem = () => {

  const [posts, setPosts] = useState([]);

  const fetchPosts = async () => {
    try {
      // const response = await axios.get('/api/board?type=free', {
      //   headers: { Accept: 'application/xml' }, // XML로 받을 걸 명시
      //   responseType: 'text', // 중요: XML을 문자열로 받도록 설정
      // })
      const response = await axios.get('/api/board?type=free')
      console.log(response.data.data)

      setPosts(response.data.data)

      // XML 문자열을 파싱해서 DOM으로 변환
      const parser = new DOMParser()
      const xmlDoc = parser.parseFromString(response.data, 'application/xml')

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
          <div className="board-list-row">
            <span className="post-title">{post.title}</span>
            <span className="post-userId">{post.user_id}</span>
            <span className="post-comments">
              <i className="comment-icon">💬</i> {post.comments}
            </span>
          </div>
        </div>
      ))}
    </div>
  )
}

export default BoardItem