import React, { useEffect, useState } from 'react'
import CommentForm from '../../components/Board/CommentForm'
import './BoardDetailPage.css'
import axios from 'axios'
import { useNavigate, useParams } from 'react-router'

// 정보 공유의 세부 내용을 보여줄 페이지
const BoardDetailPage = () => {
    const [post, setPost] = useState(null);
    const { id } = useParams();

    const navigate = useNavigate();

    const goBack = () => {
        navigate('/infoShare');
    }

    const fetchPostDetail = async () => {
        try {
            // /board/detail/{boardId} 참조
            const response = await axios.get(`/api/board/detail/${id}`); 

            setPost(response.data.data); 
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        if (id) {
        fetchPostDetail();
        }
    }, [id]);

    // post가 null일 경우 로딩 상태 또는 기본값 표시
    if (!post) {
        return <div>err</div>;
    }

    return (
        <div className="board-detail-page">
            <h1 className="board-category">정보 공유</h1>
            
            <button className='back-btn' onClick={goBack}>목록으로</button>

            <div className="board-title-area">
                <h2 className="board-post-title">{post.title}</h2>
                <div className="board-meta">
                <span>{post.user_id}</span>
                <span> | {post.createdAt}</span>
                </div>
            </div>

            <div className="board-content">
                <p>{post.content}</p>
            </div>

            <div className="bottom-line"></div>
        </div>
        
    )
}

export default BoardDetailPage