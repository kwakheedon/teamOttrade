import axios from 'axios'
import './SearchDetailPage.css'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router'
import LineChart from '../../components/Search/LineChart'
import BarChart from '../../components/Search/BarChart'
import PreviewList from '../../components/Common/PreviewList'
import tempResult from '../../assets/data/hs_top3_result.json'
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarController,
    BarElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js'
import CountrySelectorModal from '../../components/Search/CountrySelectorModal'
ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    BarController,
    Title,
    Tooltip,
    Legend
);

const SearchDetailPage = () => {

    const [hsSgn, setHsSgn] = useState(useParams().hsSgn)
    const [detailData, setDetailData] = useState(null)
    const [isImp, setIsImp] = useState(false)
    const [isModalOpen, setModalOpen] = useState(false)

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

    // useEffect(() => {
    //  setDetailData(null)
    //  getDetail(hsSgn)
    // }, [hsSgn])

    //국가 선택시 차트 업데이트
    const handleSelect = () => {
        
    }

    //임시 테스트용 함수
    const tempDetail = () => {
        setDetailData(tempResult)
    }
    useEffect(() => { tempDetail() }, [hsSgn])

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
                        <button className="toggle-switch" onClick={()=>setIsImp(!isImp)}>
                            <div className={`toggle-button ${isImp ? 'on' : 'off'}`}></div>
                        </button>
                    </div>
                </div>
                <div className='country-select-box'>
                    국가 선택
                    <div>
                        <button onClick={() => setModalOpen(true)}>
                            국가 선택 버튼 자리
                        </button>
                    </div>
                    <CountrySelectorModal
                        show={isModalOpen}
                        onClose={() => setModalOpen(false)}
                        onSelect={handleSelect}
                    />
                </div>
            </div>
            <div className='search-detail-box'>
                <div>
                    <div>
                        <LineChart
                            detailData={detailData}
                            metricKey={isImp? 'topImpDlr' : 'topExpDlr'}
                        />
                        <div className='gpt-recommandation-box'>
                            GTP기반 유망 국가 출력할 부분 혹시 버튼으로 하나?
                        </div>
                    </div>
                </div>
                <div>
                    <BarChart
                        detailData={detailData}
                        metricKey={isImp? 'topImpDlr' : 'topExpDlr'}
                    />
                    <div className='toBoard'>
                        <h2>연관 게시글</h2>
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
                <button className='pdf-btn'>PDF</button>
                <button className='share-btn'>Share</button>
            </div>
        </div>
    )
}

export default SearchDetailPage