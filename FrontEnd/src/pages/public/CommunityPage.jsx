// CommunityPage.jsx
import React from 'react'
import './CommunityPage.css'
import { Link } from 'react-router-dom'
import BoardItem from '../../components/Board/BoardItem'
import BoardHotItem from '../../components/Board/BoardHotItem'
import BoardTopItem from '../../components/Board/BoardTopItem'

// 댓글 총 개수를 재귀적으로 계산하는 함수
const countTotalComments = (comments) => {
  return comments.reduce((total, comment) => {
    const childCount = Array.isArray(comment.children)
      ? countTotalComments(comment.children)
      : 0
    return total + 1 + childCount
  }, 0)
}

const CommunityPage = () => {
  return (
    <div className='comPage'>

      <div className="main-content">
        <div className="left-column">
          <section className="hot-section">
            <h2>실시간 HOT</h2>
            <BoardHotItem />
          </section>

          <section className="bottom-section">
            <div className="board">
              <div className="board-header">
                <h2>자유게시판</h2>
                <Link to="/board">더보기 &gt;</Link>
              </div>
              <BoardItem type={'free'} />
            </div>

            <div className="board">
              <div className="board-header">
                <h2>정보 공유</h2>
                <Link to="/infoShare">더보기 &gt;</Link>
              </div>
              <BoardItem type={'info'}/>
            </div>
          </section>
        </div>

        <aside className="right-section">
          <h3>가장 많이 검색된 품목</h3>
          <BoardTopItem />
        </aside>
      </div>
    </div>
  )
}

export default CommunityPage