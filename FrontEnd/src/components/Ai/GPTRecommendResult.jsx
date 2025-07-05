import React from 'react'
import ReactMarkdown from "react-markdown"

// GPT 기반 유망국가 추천 결과를 보여주는 컴포넌트
const GPTRecommendResult = ({ recommend, gptSummary }) => {
  // gptSummary(이전 검색 이력)가 있으면 표시
  if (gptSummary) {
    return (
      <div>
        <ReactMarkdown>{gptSummary}</ReactMarkdown>
      </div>
    )
  }

  // recommend(새로 받은 추천) 데이터가 있을 때만 표시
  if (recommend) {
    return (
      <div className='gpt-recommend-result-box'>
        <div>
          <ReactMarkdown>{recommend.reason}</ReactMarkdown>
        </div>
        <div>
          <span>추천국가 : </span><ReactMarkdown>{recommend.promisingCountry}</ReactMarkdown>
        </div>
      </div>
    )
  }

  // 데이터가 없으면 아무것도 렌더링하지 않음
  return null;
}

export default GPTRecommendResult