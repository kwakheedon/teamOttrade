import React from 'react'
import './CommunityPage.css'
import { Link } from 'react-router'
import BoardItem from '../../components/Board/BoardItem'

const CommunityPage = () => {
  return (
    <div className='comPage'>

      {/* 검색창 */}
      <div className="comInput">
        <input type="text" />
      </div>

      <div className="top-section">
        {/* 실시간 HOT */}
        <section className="hot-section">
          <h2>실시간 HOT</h2>
          <BoardItem />
        </section>

        {/* 가장 많이 검색된 품목 */}
        <aside className="right-section">
          <h3>가장 많이 검색된 품목</h3>
          <BoardItem />
        </aside>
      </div>

      {/* 하단 섹션 */}
      <section className="bottom-section">
        {/* 자유게시판 */}
        <div className="board">
          <div className="board-header">
            <h2>자유게시판</h2>
            <Link to="/board">더보기 &gt;</Link>
          </div>
          <BoardItem />
        </div>

        {/* 정보공유 */}
        <div className="board">
          <div className="board-header">
            <h2>정보 공유</h2>
            <Link to="/board">더보기 &gt;</Link>
          </div>
          <BoardItem />
        </div>
      </section>

    </div>
  )
}

export default CommunityPage
