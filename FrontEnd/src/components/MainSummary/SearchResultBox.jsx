import axios from 'axios'
import React from 'react'
import { useNavigate } from 'react-router'

// 품목명 검색 후 결과를 출력하는 페이지
const SearchResultBox = ( { hsList } ) => {

  const navigate = useNavigate()

  const searchItem = async (hsSgn) => {
    console.log('물품 상세 선택 결과 : ',hsSgn)
    navigate(`/search/${hsSgn}`)
  }

  if(!hsList) {
    return <p>로딩중....</p>
  }

  return (
    <div className='search-result-box'>
      <table>
        <tbody>
          <tr className='search-result-header'>
            <td>순번</td>
            <td>HS코드</td>
            <td>신고 비율</td>
            <td>HS 품목 해설</td>
            <td>평균 세율</td>
          </tr>
          {hsList.map((item, index) => 
            <tr className='search-result-body' key={index}>
              <td>{index}</td>
              <td>{item.hsSgn}</td>
              <td>{item.rate}</td>
              <td>{item.korePrnm}</td>
              <td>{item.avgTxrt}%</td>
              <td>
                <button
                  className='search-detail-button'
                  onClick={() => searchItem(item.hsSgn)}>
                </button>
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}

export default SearchResultBox