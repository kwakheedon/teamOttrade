import React, { useState } from 'react'
import './GPTRecommend.css'
import GPTRecommendButton from './GPTRecommendButton'
import GPTRecommendResult from './GPTRecommendResult'
import axios from '../../apis/authApi'

//GPT기반 추천 기능, 컴포넌트를 모아놓는 곳
const GPTRecommend = ({ hsSgn }) => {

  const [recommendData, setRecommendData] = useState(null)

  const handleClick = async () => {
    console.log("GPT기반 추천중...")
    try {
      const res = await axios.post(`gpt/analyze/${hsSgn}`)
      console.log(res)
      if(res.status!==200) {
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
    }
  }

  return (
    <div className='gpt-recommend-box'>
      <button onClick={handleClick}>GPT 추천</button>
      <GPTRecommendResult recommend={recommendData}/>
    </div>
  )
}

export default GPTRecommend