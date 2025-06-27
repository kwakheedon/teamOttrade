import React from 'react'
import './BoardForm.css'

// 글쓰기 기능을 담당하는 폼 컴포넌트
const BoardForm = () => {
  return (
    <div className="form-wrapper">
      <label className="form-label"><h1>제목</h1></label>
        <textarea
          className="form-textarea"
          placeholder="내용을 작성하세요."
        ></textarea>
    </div>
  );
};

export default BoardForm