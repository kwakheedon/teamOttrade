import React from 'react'
import BoardForm from '../../components/Board/BoardForm'
import { useNavigate } from 'react-router';
import './BoardWritePage.css'

// 게시글 작성 시 이용할 페이지
const BoardWrite = () => {
  const navigate = useNavigate();

  const write = () => {
    navigate('/board');
  };

  return (
    <div className="write-container">
      <h1 className="write-title">자유게시판/정보 공유/QnA</h1>

      <BoardForm />

      <div className="write-button-wrap">
        <button className="boardBtn" onClick={write}>게시</button>
      </div>
    </div>
  );
};

export default BoardWrite