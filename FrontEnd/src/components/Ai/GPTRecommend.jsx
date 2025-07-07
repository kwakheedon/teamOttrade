// src/components/GPTRecommend.jsx
import React, { useEffect, useState } from 'react'
import './GPTRecommend.css'
import GPTRecommendResult from './GPTRecommendResult'
import axios from '../../apis/authApi'
import GradientButton from './GradientButton'

const GPTRecommend = ({ hsSgn, gptSummary }) => {
  const [recommendData, setRecommendData] = useState(gptSummary)
  const [isGptLoading, setIsGptLoading] = useState(false)
  const [forbidden, setForbidden] = useState(false)

  const handleClick = async () => {
    setIsGptLoading(true)
    setRecommendData('')
    try {
      const res = await axios.post(`/gpt/analyze/${hsSgn}`)
      if (res.status !== 200) {
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

  useEffect(()=> {
    if(recommendData!=='') setForbidden(true)
    else setForbidden(false)
  }, [recommendData])

  return (
    <div className="gpt-recommend-container">
      <GradientButton
        onClick={handleClick}
        className={`gpt-recommend-btn`}
        disabled={isGptLoading || forbidden}
        forbidden={forbidden}
        loadingDuration={2000} // loadingDuration 을 충분히 크게 잡아서 API 콜 완료까지 스피너 유지 가능
      >
        {isGptLoading
          ? <span className="loading-text">분석 중...</span>
          : 'GPT 추천'
        }
      </GradientButton>

      <div className="result-box">
        <GPTRecommendResult
          recommend={recommendData}
          gptSummary={gptSummary}
        />
      </div>
    </div>
  )
}

export default GPTRecommend
