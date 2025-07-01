import React from 'react'
import { Link } from 'react-router'

//게시판 미리보기 컴포넌트
//더보기 버튼은 컴포넌트 따로 나눌지 안 나눌지 고민 해봐야할듯
const PreviewList = ({ dataList }) => {
  return (
    <div className="board-column">
      <div className="board-header">
        <h3>{dataList.title}</h3>
        <Link to={dataList.path}>더보기 &gt;</Link>
      </div>
    </div>
  )
}

export default PreviewList