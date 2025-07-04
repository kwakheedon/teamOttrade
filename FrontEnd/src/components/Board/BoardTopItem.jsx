import axios from '../../apis/authApi';
import React, { useEffect, useState } from 'react'
import './BoardItem.css'
import './BoardTopItem.css'

// 가장 많이 검색된 물품 요약을 보여줄 컴포넌트
function BoardTopItem() {
    const [posts, setPosts] = useState([]);

    const fetchPosts = async () => {
        try {
            const response = await axios.get('/api/logs/top-keywords')
        
            setPosts(response.data.data)
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
        {posts.slice(0, 10).map((post, idx) => (
            <div key={idx}>
                <div className="board-list-row2">
                    <span className="post-number">{idx + 1}</span>
                    <span className="post-title">{post.keyword}</span>
                    <span>{post.searchCount}</span>
                </div>
            </div>
        ))}
        </div>
    )
}

export default BoardTopItem