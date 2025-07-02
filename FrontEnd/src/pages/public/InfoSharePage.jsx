// 정보 공유 페이지
// BoardPage.css를 사용!

import React, { useEffect, useState } from 'react'
import './BoardPage.css'
import PageNav from '../../components/Common/PageNav'
import SearchForm from '../../components/Common/SearchForm'
import BoardHotItem from '../../components/Board/BoardHotItem'
import { Link, useNavigate } from 'react-router'
import axios from 'axios'

const InfoSharePage = () => {
    const navigate = useNavigate()
    const [posts, setPosts] = useState([])
    const [filteredPosts, setFilteredPosts] = useState([])
    const [currentPage, setCurrentPage] = useState(1);
    const postsPerPage = 10;

    const write = () => {
        navigate('/board/write')
    }

    const fetchPosts = async () => {
        try {
            const response = await axios.get('/api/board?type=free')
            // response.data.data.content가 실제 게시글 배열이라고 가정하고 수정합니다.
            const fetchedPosts = response.data.data.content;
            setPosts(fetchedPosts);
            setFilteredPosts(fetchedPosts);
            console.log('초기 게시글 데이터:', fetchedPosts); // 데이터 확인용
        } catch (err) {
            console.error('게시글을 가져오는 중 오류 발생:', err);
        }
    }

    useEffect(() => {
        fetchPosts()
    }, [])

    const handleSearch = (option, keyword) => {
        const lowerKeyword = keyword.toLowerCase()
        console.log('검색 옵션:', option, '검색어:', keyword); // 검색 입력 확인용

        const result = posts.filter(post => {
            const title = post.title?.toLowerCase() || ''
            const content = post.content?.toLowerCase() || ''

            if (option === 'title') return title.includes(lowerKeyword)
            if (option === 'content') return content.includes(lowerKeyword)
            if (option === 'title_content') return title.includes(lowerKeyword) || content.includes(lowerKeyword)
            return false
        })

        setFilteredPosts(result)
        console.log('검색 결과:', result); // 검색 결과 확인용
    }

    const indexOfLastPost = currentPage * postsPerPage;
    const indexOfFirstPost = indexOfLastPost - postsPerPage;
    const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    return (
        <div className="board-container">
            <h1 className="board-title">정보 공유</h1>

            <div className="board-layout">
                <div className="post-preview">
                    {/* filteredPosts가 배열인지 확인하여 map 함수를 안전하게 호출합니다. */}
                    {Array.isArray(filteredPosts) && filteredPosts.map((post, idx) => (
                        <div key={idx}>
                            <Link to={`/board/${post.id}`} className="board-list-row-link">
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
                            </Link>
                        </div>
                    ))}

                    <div className="board-bottom2">
                        <PageNav 
                            postsPerPage={postsPerPage}
                            totalPosts={filteredPosts.length}
                            paginate={paginate}
                            currentPage={currentPage}
                        />
                        <SearchForm onSearch={handleSearch} />
                    </div>
                </div>

                <aside className="hot-posts-section">
                    <h2>실시간 HOT 게시글</h2>
                    <BoardHotItem />
                </aside>
            </div>
        </div>
    )
}

export default InfoSharePage