import React from 'react'
import './BoardPage.css'
import PageNav from '../../components/Common/PageNav'
import SearchForm from '../../components/Common/SearchForm'
import { useNavigate } from 'react-router';

// 자유/정보 공유 게시판의 글 목록을 보여줄 페이지
const BoardPage = () => {

  const navigate = useNavigate();

  const write = () => {
    navigate('/board/write')
  };

  return (
    <div className="board-container">
      <h1 className="board-title">자유게시판</h1>

      <div className="board-layout">
        <div className="post-list">
          
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