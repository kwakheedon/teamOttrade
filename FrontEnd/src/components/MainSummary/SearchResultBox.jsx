import React from 'react'

// 품목명 검색 후 결과를 출력하는 페이지
const SearchResultBox = ( { hsList } ) => {
  return (
    <div>
        {hsList.map((item, index) => 
            <p key={index}>{item.korePrnm}</p>
        )}
    </div>
  )
}

export default SearchResultBox