import React, { useState } from 'react'
 import { Bar } from 'react-chartjs-2'

//수출입 막대그래프 컴포넌트
const BarChart = () => {

  const [datas, setDatas] = useState()
  const setBar = () => {
    setDatas(
      [{
        label: "수출량",
        data: detailData.topExpWgt.map((info) => info.expWgt),
        backgroundColor: "#1E90FF",
        borderWidth: 1
      },
      {
        label: "수입량",
        data: detailData.topExpWgt.map((info) => info.expWgt),
        backgroundColor: "#1E90FF",
        borderWidth: 1
      }]
    )
  } 

  return (
    <div className='bar-chart-box'>
      <p>수출 TOP3</p>
      <Bar data={datas[0]}
      />
    </div>
  )
}

export default BarChart