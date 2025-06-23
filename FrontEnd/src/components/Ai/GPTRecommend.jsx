import React from 'react'
import GPTRecommendButton from './GPTRecommendButton'
import GPTRecommendResult from './GPTRecommendResult'

//GPT기반 추천 기능, 컴포넌트를 모아놓는 곳
const GPTRecommend = () => {
  return (
    <div>
        <GPTRecommendButton/>
        <GPTRecommendResult/>
    </div>
  )
}

export default GPTRecommend