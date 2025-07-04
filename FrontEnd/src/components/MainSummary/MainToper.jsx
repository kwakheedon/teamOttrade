import React, { useState, useEffect } from 'react'
import axios from 'axios'
import SearchResultBox from './SearchResultBox'
import search from "../../assets/icons/search.png"

const MainToper = ({ onScrollClick }) => {
    const [searchItem, setSearchItem] = useState("")
    const [hsList, setHsList] = useState()
    const [suggestions, setSuggestions] = useState([]) // 자동 완성 추천어 목록

    // 자동 완성 요청
    useEffect(() => {
        const fetchSuggestions = async () => {
        if (searchItem.trim() === "") {
            setSuggestions([])
            return
        }

        try {
            const { data } = await axios.get(`/api/suggest?keyword=${encodeURIComponent(searchItem)}`)
            setSuggestions(data.data) // 서버에서 추천어 리스트 받기
        } catch (err) {
            console.error("자동 완성 실패", err)
            setSuggestions([])
        }
        }

        const debounce = setTimeout(fetchSuggestions, 300) // 300ms 디바운싱
        return () => clearTimeout(debounce)
    }, [searchItem])

    // 검색 실행
    const startSearch = async (item = searchItem) => {
        try {
        const { data } = await axios.get(`/api/search-summary/${encodeURIComponent(item)}`)
        console.log('검색한 물품 수 : ', data)
        setHsList(data.data)
        setSuggestions([]) // 자동완성 닫기
        } catch (error) {
        console.error(error)
        }
    }

    // 자동완성 클릭 시 검색어 세팅 + 검색 실행
    const handleSuggestionClick = (word) => {
        setSearchItem(word)
        startSearch(word)
    }

    return (
        <div className='main-box'>
        <h1 className="main-text">
            품목만 입력하면<br />
            수출 전략이 완성됩니다
        </h1>

        <div className='main-input-box'>
            <input
            className="input-item"
            type="text"
            value={searchItem}
            onChange={e => setSearchItem(e.target.value)}
            />

            {/* 🔽 자동 완성 결과 */}
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

            <button className='iconBtn' onClick={() => startSearch()}>
                <img src={search} />
            </button>
        </div>

        <button className='scrollBtn' onClick={onScrollClick}></button>
        </div>
    )
}

export default MainToper
