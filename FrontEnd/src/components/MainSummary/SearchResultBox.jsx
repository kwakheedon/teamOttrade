import axios from 'axios'
import React from 'react'
import { useNavigate } from 'react-router-dom'
import Loading from '../../components/Common/Loading'
import useSearchStore from '../../stores/searchStore'

// 품목명 검색 후 결과를 출력하는 페이지
// MainPage.css 참조!
const SearchResultBox = ( { hsList } ) => {

  const navigate = useNavigate()
  const setSearchItem = useSearchStore((state) => state.setSearchItem)

  const searchItem = (hsSgn, korePrnm) => {
    console.log('물품 상세 선택 결과 : ',hsSgn, korePrnm)
    setSearchItem(hsSgn, korePrnm)
    // navigate(`/search/${hsSgn}?korePrnm=${encodeURIComponent(korePrnm)}`)
    navigate(`/search/${hsSgn}?korePrnm=${korePrnm}`)
  }

  if(!hsList) {
    return <Loading/>
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
                  onClick={() => searchItem(item.hsSgn, item.korePrnm)}>
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