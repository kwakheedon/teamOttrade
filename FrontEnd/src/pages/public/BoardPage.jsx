import React, { useEffect, useState } from 'react';
import './BoardPage.css';
import eye_icon from '../../assets/icons/eye_icon.png'
import PageNav from '../../components/Common/PageNav';
import SearchForm from '../../components/Common/SearchForm';
import BoardHotItem from '../../components/Board/BoardHotItem'
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import Loading from '../../components/Common/Loading';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
import qs from 'qs';
import axios from '../../apis/authApi';

// 자유 게시판의 글 목록을 보여줄 페이지
const BoardPage = () => {
  const navigate = useNavigate();
  const [posts, setPosts] = useState([]);
  // const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1)
  const [urlParam] = useSearchParams();
  const type = urlParam.get('type');
  const postsPerPage = 10;

  const write = () => {
    navigate('/board/write', {
      state: {
        category: type
      }
    }); 
  };

  const fetchPosts = async () => {
    try {
      const response = await axios.get('/board', {
        params: {
          type
        }
      })
      const fetchedPosts = response.data.data;
      setPosts(fetchedPosts.content);
      setTotalPages(fetchedPosts.totalPages)
      console.log('초기 게시글 데이터:', fetchedPosts);
    } catch (err) {
      console.error('게시글을 가져오는 중 오류 발생:', err);
    }
  }
  useEffect(() => {
    fetchPosts()
  }, [])

  const showSelectedPage = async (value) => {
    try {
      const res = await axios.get('/board', {
        params: {
          type,
          "page": value-1,
          "size": postsPerPage,
          "sort": [ 'desc' ],
        },
        // 배열을 key=value&key=value 형태로 직렬화
        paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
      })
      const fetchedPosts = res.data.data
      console.log("선택한 페이지 게시글 정보: ", fetchedPosts)
      setPosts(fetchedPosts.content)
    } catch (err) {
      console.error("페이지 선택 오류", err)
      alert("페이지 이동 중 오류가 발생했습니다.")
    }
    console.log("선택한 페이지: ", value)
  }

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

    setPosts(result)
    console.log('검색 결과:', result);
  }

  if(!posts)
    return <Loading/>

  return (
    <div className="board-container">
      <h1 className="board-title">{type==='free'? '자유게시판' : '정보공유'}</h1>

      <div className="board-layout">
        <div className="post-preview">
          <table className='posts-preview-box'>
            <tbody>
              {posts.map((post) => (
                <tr
                  className='board-list-table-row'
                  key={post.id}
                  onClick={()=>navigate(`/board/${post.id}`, {state: {type}})}
                >
                  <td className='post-id'>{post.id}</td>
                  <td className='post-title'>{post.title}</td>
                  <td className='post-views-icon'>
                    <img src={eye_icon} alt="view" style={{ width: '20px', opacity: '0.3'}} />
                  </td>
                  <td className='post-views'>
                    {post.view_count} 
                  </td>
                  <td className="post-date">
                    작성일: {new Date(post.created_at).toLocaleDateString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="board-foot">
            <div className='board-bottom-top2'>
              <Stack spacing={2}>
                <Pagination
                  count={totalPages}
                  showFirstButton
                  showLastButton
                  onChange={(e, v) => showSelectedPage(v)}
                />
              </Stack>
            </div>
            <div className='board-bottom'>
              <div className="board-bottom-top">
                <button onClick={write} className="boardBtn2">
                  게시글 작성
                </button>
              </div>
              <div className="board-bottom-bottom">
                <SearchForm onSearch={handleSearch} />
              </div>
            </div>
          </div>
        </div>

        <aside className="hot-posts-section">
          <h2>실시간 HOT 게시글</h2>
          <BoardHotItem posts={posts}/>
        </aside>
      </div>
    </div>
  );
};

export default BoardPage;