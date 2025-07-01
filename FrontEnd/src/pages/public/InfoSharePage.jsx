// 정보 공유 페이지
// BoardPage.css를 사용!
import React, { useEffect, useState } from 'react'
import './BoardPage.css'
import PageNav from '../../components/Common/PageNav'
import SearchForm from '../../components/Common/SearchForm'
import { useNavigate } from 'react-router';
import axios from 'axios';

// 자유 게시판의 글 목록을 보여줄 페이지
const BoardPage = () => {
    const navigate = useNavigate()
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
        <div className="board-container">
        <h1 className="board-title">정보 공유</h1>

        <div className="board-layout">
            <div className="post-preview">
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

                <div className="board-bottom2">
                    <SearchForm />
                </div>
            </div>

            <aside className="hot-posts-section">
            <h2>실시간 HOT 게시글</h2>
            </aside>
        </div>
        </div>
    )
}


export default BoardPage