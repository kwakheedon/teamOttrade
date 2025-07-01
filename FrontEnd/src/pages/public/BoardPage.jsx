import React from 'react'
import './BoardPage.css'
import PageNav from '../../components/Common/PageNav'
import SearchForm from '../../components/Common/SearchForm'
import { useNavigate } from 'react-router';
import axios from 'axios';

// 자유/정보 공유 게시판의 글 목록을 보여줄 페이지
const BoardPage = () => {

  const navigate = useNavigate();
  const [posts, setPosts] = useState([]);

  const write = () => {
    navigate('/board/write')
  };

    useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await axios.get('/api/write')  // axios 사용
        setPosts(response.data)
      } catch (error) {
        console.error('게시글을 불러오는 중 오류 발생:', error)
      }
    }

    fetchPosts()
  }, [])

  return (
    <div className="board-container">
      <h1 className="board-title">자유게시판/정보 공유/QnA</h1>
  
      <div className="board-layout">
        <div className="post-list">

          {/* 게시글 목록 렌더링 */}
          {posts.map((post, idx) => (
            <div key={idx} className="post-preview">
              <h3>{post.title}</h3>
              <p>작성자: {post.user_id}</p>
              <p>작성일: {new Date(post.created_at).toLocaleDateString()}</p>
            </div>
          ))}
          
          <button onClick={write} className="boardBtn2">게시글 작성</button>
          
          {/* 페이지 버튼 */}
          <PageNav />

          {/* 검색 폼 */}
          <SearchForm />
        </div>

        <aside className="hot-posts-section">
          <h2>실시간 HOT 게시글</h2>
        </aside>
      </div>
    </div>
  )
}

export default BoardPage