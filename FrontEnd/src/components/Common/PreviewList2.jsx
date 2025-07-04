import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import './PreviewList.css'

//연관 게시글 미리보기 컴포넌트
const PreviewList2 = ({ dataList, keyword }) => {
    const [posts, setPosts] = useState([])

    const fetchPosts = async () => {
    // keyword가 없으면 함수를 실행하지 않습니다.
    if (!keyword) return;

    try {
        const response = await axios.get('/api/board/search', {
        // ✅ 모든 파라미터를 이 객체 안에서 관리합니다.
        params: {
            keyword, // `keyword: keyword`와 동일
            page: 0,
            size: 5,
            sort: 'desc',
        },
        });
        console.log(response.data);
        
        setPosts(response.data.data.content);
    } catch (err) {
        console.error('연관 게시글 불러오기 실패:', err);
    }
    };

    useEffect(() => {
        fetchPosts()
    }, [keyword]) // keyword가 바뀌면 다시 fetch

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
                    </Link>
                </div>
                ))}
            </div>
        </div>
    )
}

export default PreviewList2