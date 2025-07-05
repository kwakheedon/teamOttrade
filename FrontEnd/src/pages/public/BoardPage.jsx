import React, { useEffect, useState } from 'react';
import './BoardPage.css';
import PageNav from '../../components/Common/PageNav';
import SearchForm from '../../components/Common/SearchForm';
import BoardHotItem from '../../components/Board/BoardHotItem'
import { Link, useNavigate } from 'react-router-dom';
import axios from '../../apis/authApi';

// 자유 게시판의 글 목록을 보여줄 페이지
const BoardPage = () => {
  const navigate = useNavigate();
  const [posts, setPosts] = useState([]);
  const [filteredPosts, setFilteredPosts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState([])
  const postsPerPage = 10;

  const write = () => {
    navigate('/board/write', {
      state: {
        category: "free"
      }
    }); 
  };

  const fetchPosts = async () => {
    try {
      const response = await axios.get('/board', {
        params: {
          type: 'free'
        }
      })
      const fetchedPosts = response.data.data.content;
      setTotalPages(response.data.data.totalPages)
      setPosts(fetchedPosts);
      setFilteredPosts(fetchedPosts);
      console.log('초기 게시글 데이터:', fetchedPosts);
    } catch (err) {
      console.error('게시글을 가져오는 중 오류 발생:', err);
    }
  }

  useEffect(() => {
    fetchPosts()
  }, [])

  const handleSearch = (option, keyword) => {
    const lowerKeyword = keyword.toLowerCase()
    console.log('검색 옵션:', option, '검색어:', keyword);

    const result = posts.filter(post => {
      const title = post.title?.toLowerCase() || ''
      const content = post.content?.toLowerCase() || ''

      if (option === 'title') return title.includes(lowerKeyword)
      if (option === 'content') return content.includes(lowerKeyword)
      if (option === 'title_content') return title.includes(lowerKeyword) || content.includes(lowerKeyword)
        return false
    })

    setFilteredPosts(result)
    setCurrentPage(1);
    console.log('검색 결과:', result);
  }

  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className="board-container">
      <h1 className="board-title">자유게시판</h1>

      <div className="board-layout">
        <div className="post-preview">
          <table className='posts-preview-box'>
            <tbody>
              {currentPosts.map((post, idx) => (
                <tr
                  className='board-list-table-row'
                  key={post.id}
                  onClick={()=>navigate(`/board/${post.id}`)}
                >
                  <td className='post-id'>{post.id}</td>
                  <td className='post-title'>{post.title}</td>
                  <td className='post-comments'>
                    <i className="comment-icon">💬</i> {post.comments ? post.comments : 0}
                  </td>
                  <td className="post-date">
                    작성일: {new Date(post.created_at).toLocaleDateString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="board-bottom">
              <div className="board-bottom-top">
                <button onClick={write} className="boardBtn2">게시글 작성</button>
                <div className='board-bottom-top2'>
                  <PageNav
                    postsPerPage={postsPerPage}
                    totalPosts={filteredPosts.length}
                    paginate={paginate}
                    currentPage={currentPage}
                  />
                </div>
              </div>
              <div className="board-bottom-bottom">
                <SearchForm onSearch={handleSearch} />
              </div>
          </div>
        </div>

        <aside className="hot-posts-section">
          <h2>실시간 HOT 게시글</h2>
          <BoardHotItem />
        </aside>
      </div>
    </div>
  );
};

export default BoardPage;