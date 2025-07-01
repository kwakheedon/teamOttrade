import React, { useState } from 'react'
import axios from 'axios'
import SearchResultBox from './SearchResultBox'

const MainToper = ({ onScrollClick }) => {

    const [searchItem, setSearchItem] = useState("")
    const [hsList, setHsList] = useState()

    const startSearch = async () => {
        //const path = `http://192.168.219.86:8088/api/search-summary/${encodeURIComponent(searchItem)}`
        const path = `/api/search-summary/${searchItem}`
        try {
            const {data} = await axios.get(path)
            console.log('검색한 물품 수 : ',data)
            setHsList(data.data)
        } catch (error) {
            console.error(error)
        }
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
                {hsList && <SearchResultBox hsList={hsList}/>}
                <button className='iconBtn' onClick={startSearch}>🔍</button>
            </div>

            {/* 스크롤 버튼 */}
            <button className='scrollBtn' onClick={onScrollClick}></button>
        </div>
    )
}

export default MainToper
