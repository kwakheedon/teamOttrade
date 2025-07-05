import React from 'react'

// 글쓰기 기능을 담당하는 폼 컴포넌트
const BoardForm = ({ savePost, setTitle, setContent }) => {
  return (
    <div className="form-wrapper">
      <h3>글 작성</h3>
      <label className="form-label" htmlFor='title'>
        <input
          type="text"
          name='title'
          placeholder='제목을 입력하세요'
          className='title-input'
          onChange={e => setTitle(e.target.value)}
        />
      </label>
      <hr />
        <textarea
          className="form-textarea"
          style={{
            overflowY: 'scroll'
          }}
          placeholder="내용을 작성하세요."
          onChange={e => setContent(e.target.value)}
        ></textarea>
    </div>
  );
};

export default BoardForm