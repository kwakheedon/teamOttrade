import React from 'react'
import './BoardForm.css'

// 글쓰기 기능을 담당하는 폼 컴포넌트
const BoardForm = ({ savePost, setTitle, setContent }) => {
  return (
    <div className="form-wrapper">
      <label className="form-label">
        <input
          type="text"
          placeholder='제목을 입력하세요'
          onChange={e => setTitle(e.target.value)}
        />
      </label>
        <textarea
          className="form-textarea"
          placeholder="내용을 작성하세요."
          onChange={e => setContent(e.target.value)}
        ></textarea>
      <div className="write-button-wrap">
        <button className="boardBtn" onClick={() => savePost()}>게시</button>
      </div>
    </div>
  );
};

export default BoardForm