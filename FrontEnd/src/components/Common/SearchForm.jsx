import React, { useState } from 'react'

//게시글 검색어 입력 폼
const SearchForm = () => {

  const [searchOption, setSearchOption] = useState('title');

  const handleOptionChange = (e) => {
    setSearchOption(e.target.value);
  };

  return (
    <div className='search'>
      <select
        className="searchSelect"
        value={searchOption}
        onChange={handleOptionChange}
      >
        <option value="title">제목</option>
        <option value="content">내용</option>
        <option value="title_content">제목+내용</option>
      </select>
      <input type="text" className='inputText' name="" id="" placeholder='검색어 입력'/>
      <button className='searchBtn'>돋보기</button>
    </div>
  )
}

export default SearchForm