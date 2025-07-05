    import React, { useState, useEffect } from 'react'
    import axios from 'axios'
    import SearchResultBox from './SearchResultBox'
    import search from "../../assets/icons/search.png"
    import SplitText from "./SplitText";
    const MainToper = ({ onScrollClick }) => {
    const [searchItem, setSearchItem] = useState("")
    const [hsList, setHsList] = useState()
    const [suggestions, setSuggestions] = useState([])

    useEffect(() => {
        const fetchSuggestions = async () => {
        if (searchItem.trim() === "") {
            setSuggestions([])
            return
        }
        try {
            const { data } = await axios.get(`/api/suggest?keyword=${encodeURIComponent(searchItem)}`)
            setSuggestions(data.data)
        } catch {
            setSuggestions([])
        }
        }
        const debounce = setTimeout(fetchSuggestions, 300)
        return () => clearTimeout(debounce)
    }, [searchItem])

    const startSearch = async (item = searchItem) => {
        try {
        const { data } = await axios.get(`/api/search-summary/${encodeURIComponent(item)}`)
        setHsList(data.data)
        setSuggestions([])
        } catch (error) {
        console.error(error)
        }
    }

    const handleSuggestionClick = (word) => {
        setSearchItem(word)
        startSearch(word)
    }

    // ▶️ onSubmit 으로 폼 제출 핸들링
    const handleSubmit = (e) => {
        e.preventDefault()
        startSearch()
    }
    const handleAnimationComplete = () => {
  console.log('All letters have animated!');
};

    return (
        <div className='main-box'>
        <h1 className="main-text">
             <SplitText
  text="품목만 입력하면<br />
            수출 전략이 완성됩니다"
            fontSize="80px"
  className="font-semibold text-center"
  onLetterAnimationComplete={handleAnimationComplete}
/>
        </h1>
       

        {/* form 태그로 감싸고 onSubmit 사용 */}
        <form className='main-input-box' onSubmit={handleSubmit}>
            <input
            className="input-item"
            type="text"
            value={searchItem}
            onChange={e => setSearchItem(e.target.value)}
            />

            {suggestions.length > 0 && (
            <ul className="autocomplete-list">
                {suggestions.map((item, idx) => (
                <li
                    key={idx}
                    className="autocomplete-item"
                    onClick={() => handleSuggestionClick(item)}
                >
                    {item}
                </li>
                ))}
            </ul>
            )}

            {/* 검색 결과 */}
            {hsList && <SearchResultBox hsList={hsList} item={searchItem}/>}

            {/* 버튼을 submit 타입으로 변경 */}
            <button className='iconBtn' type="submit">
            <img src={search} alt="검색" />
            </button>
        </form>

        <button className='scrollBtn' onClick={onScrollClick}></button>
        </div>
    )
    }

    export default MainToper
