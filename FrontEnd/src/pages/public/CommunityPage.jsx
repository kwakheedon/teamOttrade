import React from 'react'
import { Link } from 'react-router'
import TotalMembersBox from '../../components/MainSummary/TotalMembersBox'
import TotalPostsBox from '../../components/MainSummary/TotalPostsBox'

// 커뮤니티의 전체를 보여줄 커뮤니티 메인 페이지(인기 게시글, 자유, 정보공유 등등)
const CommunityPage = () => {
  return (
    <div>
      <input type="text" />

      <h1>실시간 HOT</h1>
      {/* 실시간 HOT 미리보기 */}

      <h1>자유게시판</h1>
      <Link to="/board">더보기</Link>
      {/* 자유게시판 미리보기 */}

      <h1>정보 공유</h1>
      <Link to="/board">더보기</Link>
      {/* 정보 공유 미리보기 */}

    </div>
  )
}

export default CommunityPage