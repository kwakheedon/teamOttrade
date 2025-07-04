import React from 'react'
import Loading from '../Common/Loading'
import ReactMarkdown from "react-markdown"

//GPT 기반 유망국가 추천 결과를 보여주는 컴포넌트
const GPTRecommendResult = ({ recommend, gptSummary }) => {
  if(gptSummary) {
    return (
      <div>
        {gptSummary}
      </div>
    )
  }

  if(!recommend)
    return (
      <Loading/>
    )

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

export default GPTRecommendResult