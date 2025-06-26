import React from 'react'
import BoardForm from '../../components/Board/BoardForm'
import { useNavigate } from 'react-router';
import './BoardWritePage.css'

// 게시글 작성 시 이용할 페이지
const BoardWrite = () => {

  const navigate = useNavigate();
  
  const write = () => {
    // 자유게시판으로 이동함
    navigate('/board')
  };

  return (
    <div>
      <h1>자유게시판/정보 공유/QnA</h1>

      {/* BoardForm.jsx(게시글 폼) 호출 */}
      <BoardForm/>

      {/* 게시 버튼 누르면 자유게시판 페이지로 */}
      <button className='boardBtn' onClick={write}>게시</button>
    </div>
  )
}

export default BoardWrite