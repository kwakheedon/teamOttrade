import React from 'react'
import BoardForm from '../../components/Board/BoardForm'
import { useLocation, useNavigate } from 'react-router-dom';
import './BoardWritePage.css'

// 게시글 작성 시 이용할 페이지
const BoardWrite = () => {
  const location = useLocation()
  const navigate = useNavigate()

  console.log("location: ",location)
  const savePost = () => {
    navigate('/board', {replace: true});
  };

  return (
    <div className="write-container">
      <h1 className="write-title">{location.state.title}</h1>

      <BoardForm />

      <div className="write-button-wrap">
        <button className="boardBtn" onClick={savePost}>게시</button>
      </div>
    </div>
  );
};

export default BoardWrite