import React from 'react'

// 품목명 검색 후 결과를 출력하는 페이지
const SearchResultBox = ( { hsList } ) => {
  return (
    <div>
      <table>
        <tbody>
          <tr className='search-result-header'>
            <td>순번</td>
            <td>HS코드</td>
            <td>신고 비율</td>
            <td>HS 품목 해설</td>
            <td>기본 세율</td>
          </tr>
          {hsList.map((item, index) => 
            <tr className='search-result-body' key={index}>
              <td>{index}</td>
              <td>{item.hsSgn}</td>
              <td>{item.rate}</td>
              <td>{item.korePrnm}</td>
              <td>{0}</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}

export default SearchResultBox