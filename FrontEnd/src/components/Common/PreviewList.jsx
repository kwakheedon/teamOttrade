import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router'

//게시판 미리보기 컴포넌트
//더보기 버튼은 컴포넌트 따로 나눌지 안 나눌지 고민 해봐야할듯
const PreviewList = ({ dataList }) => {

  const [posts, setPosts] = useState([])

  const write = () => {
    navigate('/board/write')
  }

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
    <div className="board-column">
      <div className="board-header">
        <h3>{dataList.title}</h3>
        <Link to={dataList.path}>더보기 &gt;</Link>
        <div>
          {posts.map((post, idx) => (
            <div key={idx}>
              <div className="board-list-row">
                <span className="post-id">{post.id}</span>
                <span className="post-title">{post.title}</span>
                <span className="post-userId">작성자: {post.user_id}</span>
                <span className="post-comments">
                  <i className="comment-icon">💬</i> {post.comments}
                </span>
                <span className="post-date">
                  작성일: {new Date(post.created_at).toLocaleDateString()}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>

      
    </div>
  )
}

export default PreviewList