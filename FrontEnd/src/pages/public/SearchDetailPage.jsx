import axios from 'axios'
import './SearchDetailPage.css'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router'
import LineChart from '../../components/Search/LineChart'
import BarChart from '../../components/Search/BarChart'
import PreviewList from '../../components/Common/PreviewList'

const SearchDetailPage = () => {

    const [hsSgn, setHsSgn] = useState(useParams().hsSgn)
    const [detailData, setDetailData] = useState(1)

    const getDetail = async (hsSgn) => {
        try {
            const path = `/api/top3/${hsSgn}`
            const res = await axios.get(path)
            const data = res.data
            console.log('선택한 물품의 top3 출력 : ',data)
            setDetailData(data)
        } catch (err) {
            console.error(err)
        }
    }

    // useEffect(() => { getDetail(hsSgn) }, [hsSgn])

    if(!detailData) {
        return (
            <div>
                로딩중...
            </div>
        )
    }

    return (
        <div className='search-detail-page-box'>
            <div className='search-header'>
                <h1>물품명</h1>
                <div className='imp-exp-box'>
                    수출 / 수입
                    <div>
                        <button>토글버튼자리</button>
                    </div>
                </div>
                <div className='country-select-box'>
                    국가 선택
                    <div>
                        <button>국가 선택 버튼 자리</button>
                    </div>
                </div>
            </div>
            <div className='search-detail-box'>
                <div>
                    <div>
                        <LineChart/>
                        <div className='gpt-recommandation-box'>
                            GTP기반 유망 국가 출력할 부분 혹시 버튼으로 하나?
                        </div>
                    </div>
                </div>
                <div>
                    {/* 차트 잘못 만들었음,
                    차트 컴포넌트는 차트 관련된것 이외에 다른 건 가지면 안됨
                    아마 default export를 허용하면 안된다는 것 같은데
                    아마? */}
                    {/* <BarChart detailData={detailData}/> */}
                    수출/입 TOP3
                    <div className='bar-chart-box'>
                        예비용
                    </div>
                    <div>
                        연관 게시글
                        <PreviewList
                            dataList={{
                                name: '연관 게시글',
                                path: '/community'
                            }}
                        />
                    </div>
                </div>
            </div>
            <div className='search-detail-button-box'>
                <button>PDF</button>
                <button>Share</button>
            </div>
        </div>
    )
}

export default SearchDetailPage