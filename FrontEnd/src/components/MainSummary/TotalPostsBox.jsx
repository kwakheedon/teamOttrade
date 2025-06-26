import React from 'react'

//총 게시글 수  출력
const TotalPostsBox = () => {
  return (
    <div className='total-posts-box'>
      <section className="community-section">
        <h2 className="community-main-title">커뮤니티</h2>
        <h1 className="community-sub-title">정보를 모두 공유하세요!</h1>

        <div className="community-content">
          {/* 자유게시판 섹션 */}
          <div className="board-column">
            <div className="board-header">
              <h3>자유게시판</h3>
              <a href="/board">더보기 &gt;</a> {/* 실제 링크로 변경 */}
            </div>
          </div>

          {/* 정보 공유 섹션 */}
          <div className="board-column">
            <div className="board-header">
              <h3>정보 공유</h3>
              <a href="/board">더보기 &gt;</a> {/* 실제 링크로 변경 */}
            </div>
          </div>
        </div>

      </section>
    </div>
  )
}

export default TotalPostsBox