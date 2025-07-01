import React from 'react'
import PreviewList from '../Common/PreviewList'

//총 게시글 수  출력
const TotalPostsBox = () => {
  return (
    <div className='total-posts-box'>
      <section className="community-section">
        <div className='community-title'>
          <h2>커뮤니티</h2>
          <h1>정보를 모두 공유하세요!</h1>
        </div>

        <div className="community-content">
          <PreviewList
            dataList={{
              title: "자유게시판",
              path: "/board"
            }}
          />
          <PreviewList
            dataList={{
              title: "정보 공유",
              path: "/infoShare"
            }}
          />
        </div>
      </section>
    </div>
  )
}

export default TotalPostsBox