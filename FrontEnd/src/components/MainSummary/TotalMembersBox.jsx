import React from 'react'

//전체 회원 수를 출력할 컴포넌트
const TotalMembersBox = () => {
  return (
    <div className='total-members-box'>
      <div className='main-introduce'>
        <h1>
          데이터 너머의 가능성,<br />
          함께 탐험해보세요.
        </h1>
      </div>
      <div className='total-members-count'>
        <h2>총 회원 수 : n명</h2>
        <h2>총 게시글 수 : n개</h2>
      </div>
    </div>
  )
}

export default TotalMembersBox