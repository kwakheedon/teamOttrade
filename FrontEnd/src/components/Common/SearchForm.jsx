import React from 'react'

//게시글 검색어 입력 폼
const SearchForm = () => {
  return (
    <div className='search'>
      <p className='searchSelect'>제목</p>
      <input type="text" className='inputText' name="" id="" placeholder='검색어 입력'/>
      <button className='searchBtn'>돋보기</button>
    </div>
  )
}

export default SearchForm