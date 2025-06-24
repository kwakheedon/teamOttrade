import React from 'react'
import PageNav from '../../components/Common/PageNav'
import SearchForm from '../../components/Common/SearchForm'

// 자유/정보 공유 게시판의 글 목록을 보여줄 페이지
const BoardPage = () => {
  return (
    <div>
      <h1>자유게시판</h1>
      
      <button>게시글 작성</button>

      {/* 페이지 이동 버튼 호출 */}
      <PageNav/>

      {/* 검색어 입력 폼 호출 */}
      <SearchForm/>

    </div>
  )
}

export default BoardPage