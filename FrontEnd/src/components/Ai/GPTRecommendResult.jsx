import React from 'react'
import './GPTRecommendResult.css'
import ReactMarkdown from "react-markdown"
// import './GPTRecommendResult.css'

// GPT 기반 유망국가 추천 결과를 보여주는 컴포넌트
const GPTRecommendResult = ({ recommend, gptSummary }) => {

  // gptSummary(이전 검색 이력)가 있으면 그걸 표시
  // recommend(새로 받은 추천) 데이터가 있을 때만 표시
  if (recommend) {
    return (
      <div className='gpt-recommend-result-box'>
        <ReactMarkdown>{gptSummary? gptSummary : (recommend.reason)}</ReactMarkdown>
      </div>
    )
  }

  // 데이터가 없으면 아무것도 렌더링하지 않음
  return null;
}

export default GPTRecommendResult