import React, { useMemo } from 'react'
import { Bar } from 'react-chartjs-2'

/**
 * @param {{
 *   detailData: Record<string, any>,
 *   metricKey: 'topExpDlr'|'topExpWgt'|'topImpDlr'|'topImpWgt',
 *   year?: number
 * }} props
 */

//수출입 막대그래프 컴포넌트
const BarChart = ({ detailData, metricKey, year }) => {
  const records = detailData[metricKey]

  // metricKey가 없을 시
  if (!records)
    return (
      <p>지원하지 않는 metricKey: {metricKey}</p>
    )
  
  //연도 선택(만약 props로 연도 선택 안 할 시 최신연도로 지정)
  const years = records[0].items.map(i => i.year)
  const selYear = year && years.includes(year) ? year : years[0]
  
  
  //metricKey에 따라 total로 무엇을 출력할지 결정 (y축)
  const suffix = metricKey.substring(3);  
  const dataField = `total${suffix}`;
  
  //국가 레이블, 값 배열 생성
  const labels = records.map(r => r.statKor);
  const values = records.map(r => {
    const it = r.items.find(i => i.year === selYear) || {};

    //문자열 "1,234" 처리
    const raw = it[dataField] ?? 0;
    return typeof raw === 'string'? Number(raw.replace(/,/g, '')) : raw;
  });

  //색 선정
  const color = suffix==='ExpDlr'?
      ['rgb(19, 164, 221)','rgb(133, 192, 231)',
    'rgb(200, 210, 218)','rgba(255,206,86,1)']
    :
    ['rgb(240, 32, 101)','rgb(250, 129, 155)',
    'rgb(248, 216, 223)','rgb(54, 204, 122)']

  //차트 데이터
  const chartData = useMemo(() => ({
    labels,
    datasets: [{
      label: `${metricKey} TOP3`,
      data: values,
      backgroundColor: color,
    }]
  }), [labels, values, selYear, metricKey]);
  

  //옵션 추가
  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      title: {
        display: true,
        text: `${selYear}년 ${metricKey.replace(/([A-Z])/g,' $1')} 비교`
      },
      legend: { display: false, position: 'top' }
    },
    scales: {
      x: { title: { display: true, text: '국가' } },
      y: { title: { display: false, text: dataField }, beginAtZero: true }
    }
  };

  return (
    <div className='bar-chart-box'>
      <Bar
        id={`bar-chart-${metricKey}`}
        data={chartData}
        options={options}
        redraw
      />
    </div>
  )
}

export default BarChart