import React from 'react'
import { Link } from 'react-router'

// 커뮤니티의 전체를 보여줄 커뮤니티 메인 페이지(인기 게시글, 자유, 정보공유 등등)
const CommunityPage = () => {
  return (
    <div>
      <h1>함께 탐험해보세요.</h1>
      
      {/* 회원 수, 게시글 수 호출 */}
      <h2>총 회원 수:</h2>
      <h2>종 게시글 수:</h2>

      <h2>커뮤니티</h2>
      <h1>정보를 모두와 공유하세요!</h1>

      <h2>자유게시판</h2>
      <Link to="/board">더보기</Link>
      {/* 자유게시판 미리보기 호출 */}

      <h2>정보 공유</h2>
      <Link to="/board">더보기</Link>
      {/* 정보공유 페이지 미리보기 호출 */}

    </div>
  )
}

export default CommunityPage