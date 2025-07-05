import React, { useState } from 'react'
import './GPTRecommend.css'
import GPTRecommendResult from './GPTRecommendResult'
import axios from '../../apis/authApi'

// GPT기반 추천 기능, 컴포넌트를 모아놓는 곳
const GPTRecommend = ({ hsSgn, gptSummary }) => {
  const [recommendData, setRecommendData] = useState(null)
  const [isGptLoading, setIsGptLoading] = useState(false)

  const handleClick = async () => {
    setIsGptLoading(true)
    setRecommendData(null); 
    try {
      const res = await axios.post(`/gpt/analyze/${hsSgn}`)
      if(res.status !== 200) {
        console.log('알 수 없는 오류가 발생했습니다.')
        return
      }
      const data = res.data.data
      setRecommendData({
        promisingCountry: data.promisingCountry,
        reason: data.reason,
      })
    } catch (err) {
      console.error(err)
    } finally {
      setIsGptLoading(false)
    }
  }

  return (
    // 'gpt-recommend-box'를 'gpt-recommend-container'로 변경하여 CSS와 일치시킵니다.
    <div className='gpt-recommend-container'>
      <button onClick={handleClick} className='gpt-recommend-btn' disabled={isGptLoading}>
        {isGptLoading ? '분석 중...' : 'GPT 추천'}
      </button>

      {/* isGptLoading이 true일 때만 스피너를 보여줍니다. */}
      {isGptLoading && (
        <div className="gpt-spinner-container">
          <div className="gpt-spinner"></div>
        </div>
      )}

      {/*
        로딩 상태와 관계없이 GPTRecommendResult를 렌더링합니다.
        내부 로직에 따라 결과가 있거나 없을 때 알아서 처리합니다.
      */}
      <div className='result-box'>
        <GPTRecommendResult recommend={recommendData} gptSummary={gptSummary}/>
      </div>
    </div>
  )
}

export default GPTRecommend