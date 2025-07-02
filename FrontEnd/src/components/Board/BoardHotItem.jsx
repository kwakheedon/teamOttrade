// 실시간 HOT 글 미리보기
import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router'
import './BoardItem.css'
import axios from 'axios';

// 실시간 HOT 요약을 보여줄 컴포넌트
// -> BoardItem.jsx에서 view_count가 높은 순으로 렌더링
const BoardHotItem = () => {

    const [posts, setPosts] = useState([]);

    const fetchPosts = async () => {
        try {
            const response = await axios.get('/api/board?type=free')

            // view_count가 높은 순서대로 정렬
            const sortedPosts = response.data.data.content.sort((a, b) => b.view_count - a.view_count);
            setPosts(sortedPosts);
            console.log("fetchPosts 실행확인: ", sortedPosts); // 정렬된 데이터 확인

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
                    <i className="comment-icon">💬</i> {/* comments 필드가 응답 바디에 없으므로 0으로 표시 또는 다른 필드 사용 고려 */}
                    {post.comments ? post.comments : 0} 
                </span>
                </div>
            </Link>
            </div>
        ))}
        </div>
    )
}

export default BoardHotItem