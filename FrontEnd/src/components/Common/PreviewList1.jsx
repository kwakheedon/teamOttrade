import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import './PreviewList.css'

//정보공유 미리보기 컴포넌트
//더보기 버튼은 컴포넌트 따로 나눌지 안 나눌지 고민 해봐야할듯
const PreviewList1 = ({ dataList }) => {
    const [posts, setPosts] = useState([])

    const fetchPosts = async () => {
        try {
        const response = await axios.get('/api/board?type=infoShare')
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
            {posts.slice(0, 5).map((post, idx) => (
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
    )
}

export default PreviewList1