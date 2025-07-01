import React from 'react'
import { Line } from 'react-chartjs-2';

/**
 * @param {{
 *   detailData: Record<string, any>,
 *   metricKey?: 'topExpDlr'|'topExpWgt'|'topImpDlr'|'topImpWgt'
 * }} props
 */
// 수출입 꺾은선 그래프 컴포넌트
const LineChart = ({ detailData, metricKey }) => {
  const records = detailData[metricKey];

  // metricKey가 없을 시
  if (!records)
    return (
      <p>지원하지 않는 metricKey: {metricKey}</p>
    );

  // 연도는 일단 첫 번째 레코드의 연도를 사용 (x축)
  const labels = records[0].items.map(item => item.year);

  //metricKey에 따라 total로 무엇을 출력할지 결정 (y축)
  const suffix = metricKey.substring(3);  
  const dataField = `total${suffix}`;

  // 각 선별 색 설정
  const colors = suffix==='ExpDlr'?
    ['rgb(56, 172, 218)','rgb(133, 192, 231)',
    'rgb(164, 198, 221)','rgba(255,206,86,1)']
  :
    ['rgb(230, 71, 124)','rgb(252, 113, 143)',
    'rgb(252, 172, 189)','rgba(255,206,86,1)']

  // 데이터셋 객체 생성
  //  label: 국가이름과 그 순위를 가진 속성
  //  data: 연도별 total을 배열 형태로 가진 속성
  //  fill: 아마 그래프 밑을 채울건지 말건지 정하는것?
  //  borderColor: 랭크별 그래프 색
  //  tension: 모르겠다.
  //  pointRadius: 뭔가 둥글게하는거같은데 모르겠다.
  const datasets = records.map((rec, idx) => {

    const data = rec.items.map(item => {
      const raw = item[dataField]
      // "1,234,567" 같은 콤마-문자열이 있을 수도 있으니
      const num = typeof raw === 'string'? Number(raw.replace(/,/g, '')) : raw
      return +num  // 숫자가 아닌 값이 섞여 있으면 NaN이 될 수 있습니다.
    })

    return {
    label: `${rec.statKor} (순위 ${rec.rank})`,
    // data: rec.items.map(item => item[dataField]),
    data,
    fill: false,
    borderColor: colors[idx % colors.length],
    tension: 0.3,
    pointRadius: 3,
  }});

  // 차트의 각종 설정을 객체로 저장
  //  responsive: 뭔지 모르겠어
  //  plugins: 뭔지 모르겠어
  //  scales: 이게 뭔데
  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      title: { display: false, text: `${metricKey} 꺾은선 차트` },
      legend: { position: 'top' }
    },
    layout: {
      padding: {
        right: 20
      }
    },
    scales: {
      x: { reverse: true, title: { display: true, text: '연도' } },
      y: { title: { display: false, text: dataField }, beginAtZero: true }
    }
  }
  return (
    <div className='line-chart-box'>
      <Line
        id={`line-chart-${metricKey}`}
        data={{labels, datasets}}
        options={options}
        redraw
      />
    </div>
  )
}

export default LineChart