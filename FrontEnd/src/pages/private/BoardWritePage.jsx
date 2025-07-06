import React, { useEffect, useState } from 'react'
import BoardForm from '../../components/Board/BoardForm'
import { useLocation, useNavigate } from 'react-router-dom';
import './BoardWritePage.css'
import '../../components/Board/BoardForm.css'
import axios from '../../apis/authApi';

// 게시글 작성 시 이용할 페이지
const BoardWrite = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const [title, setTitle] = useState("")
  const [content, setContent] = useState("")

  useEffect(() => {
    if(!location.state) {
      alert('게시판을 선택해주세요')
      navigate('/community', {replace: true})
    }
  }, [])

  const savePost = async () => {
    // console.log("제목:",title)
    // console.log("내용:",content)
    try {
      const path = '/board/write'
      const item = {
        title,
        content,
        type: location.state.category,
      }
      const res = await axios.post(path, item)
      console.log(res)
    } catch (err) {
      console.log(err)
    }

    setTitle(""); setContent("");
    navigate(`/board?type=${location.state.category}`, {replace: true});
  };

  return (
  <div className="write-container">
    <h1 className="write-title">
      {
        location.state.category==="free"?
          "자유게시판"
          : location.state.category==="info" &&
            "정보 공유"
      }
    </h1>
    <div className='write-content-box'>
      <BoardForm
        setTitle={setTitle}
        setContent={setContent}
      />
        <div className='content-save-or-canceled-box'>
          <button onClick={()=>navigate(-1)}>
            취소
          </button>
          <button onClick={savePost}>
            게시
          </button>
      </div>
    </div>

  </div>
  );
};

export default BoardWrite