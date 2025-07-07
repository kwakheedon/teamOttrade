// SearchForm.jsx
import React, { useState } from 'react'
import { Search } from 'lucide-react'

const SearchForm = ({ onSearch }) => {
  const [searchOption, setSearchOption] = useState('title')
  const [keyword, setKeyword] = useState('')

  const handleOptionChange = (e) => {
    setSearchOption(e.target.value)
  }

  const handleInputChange = (e) => {
    setKeyword(e.target.value)
  }

  const handleSearchClick = () => {
    if (onSearch) {
      onSearch(searchOption, keyword)
    }
  }

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleSearchClick()
    }
  }

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
      <input
        type="text"
        className='inputText'
        placeholder='검색어 입력'
        value={keyword}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
      />
      <button className='searchBtn' onClick={handleSearchClick}>
        <Search color='#fff' size={20} />
      </button>
    </div>
  )
}

export default SearchForm
