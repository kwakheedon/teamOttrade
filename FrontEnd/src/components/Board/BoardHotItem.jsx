// 실시간 HOT 글 미리보기
import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import eye_icon from '../../assets/icons/eye_icon.png'
import './BoardItem.css'
import axios from '../../apis/authApi';

// 실시간 HOT 요약을 보여줄 컴포넌트
// -> BoardItem.jsx에서 view_count가 높은 순으로 렌더링
const BoardHotItem = () => {

    const [posts, setPosts] = useState([]);
    const navigate = useNavigate()

    const fetchPosts = async () => {
        try {
            const response = await axios.get('/board/hot')
            // console.log("hot 게시글 출력 실험: ", response) 
            // view_count가 높은 순서대로 정렬
            const sortedPosts = response.data.data;
            setPosts(sortedPosts);
        } catch(err) {
            console.error(err)
        }
    }

    useEffect(() => {
        fetchPosts()
    }, [])

    return (
        <div className='post-item'>
            <div className='post-content'>
                {posts.map((post, idx) => (
                    <div
                        key={idx}
                        className="board-list-row-link"
                        onClick={()=>navigate(`/board/${post.id}`, { state: {type: post.type} })}
                    >
                        {/* <Link to={`/board/${post.id}`} className="board-list-row-link"> */}
                        <div className="board-list-row">
                        <span className="post-title">{post.title}</span>
                        <span className="post-userId">
                            <img src={eye_icon} alt="view" />
                            {post.view_count}
                        </span>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default BoardHotItem