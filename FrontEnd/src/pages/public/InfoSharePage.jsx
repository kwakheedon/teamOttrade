// 정보 공유 페이지
// BoardPage.css를 사용!
// 정보 공유 페이지
import React, { useEffect, useState } from 'react'
import './BoardPage.css'
import PageNav from '../../components/Common/PageNav'
import SearchForm from '../../components/Common/SearchForm'
import { useNavigate } from 'react-router'
import axios from 'axios'

const BoardPage = () => {
    const navigate = useNavigate()
    const [posts, setPosts] = useState([])
    const [filteredPosts, setFilteredPosts] = useState([])

    const write = () => {
        navigate('/board/write')
    }

    const fetchPosts = async () => {
        try {
        const response = await axios.get('/api/board?type=free')
        setPosts(response.data.data)
        setFilteredPosts(response.data.data)
        } catch (err) {
        console.error(err)
        }
    }

    useEffect(() => {
        fetchPosts()
    }, [])

    // 🔍 검색 처리 함수
    const handleSearch = (option, keyword) => {
        const lowerKeyword = keyword.toLowerCase()

        const result = posts.filter(post => {
        const title = post.title?.toLowerCase() || ''
        const content = post.content?.toLowerCase() || ''

        if (option === 'title') return title.includes(lowerKeyword)
        if (option === 'content') return content.includes(lowerKeyword)
        if (option === 'title_content') return title.includes(lowerKeyword) || content.includes(lowerKeyword)
        return false
        })

        setFilteredPosts(result)
    }

    return (
        <div className="board-container">
        <h1 className="board-title">정보 공유</h1>

        <div className="board-layout">
            <div className="post-preview">
            {filteredPosts.map((post, idx) => (
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
                <SearchForm onSearch={handleSearch} />
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
