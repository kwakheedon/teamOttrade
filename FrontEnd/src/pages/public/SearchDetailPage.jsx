import axios from '../../apis/authApi'
import './SearchDetailPage.css'
import React, { useEffect, useState } from 'react'
import { useParams, useSearchParams } from 'react-router'
import Loading from '../../components/Common/Loading'
import LineChart from '../../components/Search/LineChart'
import BarChart from '../../components/Search/BarChart'
import PreviewList from '../../components/Common/PreviewList'
import GPTRecommend from '../../components/Ai/GPTRecommend'
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
import useSearchStore from '../../stores/searchStore'
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
    //state
    const [detailData, setDetailData] = useState(null)
    const [metricKey, setMetricKey] = useState(null)
    const [isModalOpen, setModalOpen] = useState(false)
    const [selectedCountry, setSelectedCountry] = useState(null);

    //전역state
    const { hsSgn } = useParams()
    const [searchParams] = useSearchParams()
    const korePrnm = searchParams.get('korePrnm') || ''
    // const { hsSgn, korePrnm } = useSearchStore()
    const koPrnm = (str = korePrnm, maxLength = 10) => { //이름 설정
        return str.length > maxLength?
        str.slice(0, maxLength) + '…'
        : str;
    }

    //수출입 데이터 존재 여부 확인
    const hasExp = (detailData?.topExpDlr?.length ?? 0) > 0
    const hasImp = (detailData?.topImpDlr?.length ?? 0) > 0

    
    const disableToggle = metricKey === 'topExpDlr'?
        !hasImp
        : metricKey === 'topImpDlr'?
            !hasExp
            : true

    const getDetail = async () => {
        try {
            const path = `/top3/${hsSgn}`
            console.log("경로 확인용 ----",path)
            const res = await axios.get(path)
            const data = res.data
            console.log('선택한 물품의 top3 출력 : ',data)
            setMetricKey(initExpImp(data))
            setDetailData(data)
        } catch (err) {
            console.error(err)
        }
    }

    const getDetailCountry = async () => {
        try {
            const path = `/top3/${hsSgn}?cntyCd=${selectedCountry}`
            const res = await axios.get(path)
            setDetailData(null)
            const data = res.data
            console.log('선택한 물품의 top3+선택 국가 출력 : ',data)
            setDetailData(data)
        } catch (err) {
            console.error(err)
        }
    }

    const initExpImp = (data) => {
        // console.log("init 진행")
        const exp = Array.isArray(data?.topExpDlr) && data.topExpDlr.length > 0;
        const imp = Array.isArray(data?.topImpDlr) && data.topImpDlr.length > 0;
        if (exp) return 'topExpDlr';
        if (imp) return 'topImpDlr';
        return null;
    }

    const handleToggle = () => {
        if (metricKey === 'topExpDlr' && hasImp) {
            setMetricKey('topImpDlr');
        } else if (metricKey === 'topImpDlr' && hasExp) {
            setMetricKey('topExpDlr');
        }
    }

    useEffect(() => {
        if(selectedCountry) {
            setDetailData(null)
            getDetailCountry()
        }
        else
            getDetail()
    }, [hsSgn, selectedCountry])

    //국가 선택시 차트 업데이트
    const handleSelect = (e) => {
        setModalOpen(false)
        setSelectedCountry(e.currentTarget.dataset.code)
        console.log("선택한 국가:",e.currentTarget.dataset.code)
    }

    //임시 테스트용 함수
    // const tempDetail = () => {
    //     setDetailData(tempResult)
    // }
    // useEffect(() => { tempDetail() }, [hsSgn])

    //로딩중일 때 보일 창
    if(!detailData) {
        return (
            <Loading/>
        )
    }

    return (
        <div className='search-detail-page-box'>
            <div className='search-header'>
                <h1>{koPrnm()}</h1>
                <div className='imp-exp-box'>
                    수입 / 수출
                    <div>
                        <button
                            className="toggle-switch"
                            onClick={handleToggle}
                            disabled={disableToggle}
                        >
                            <div className={`toggle-button ${metricKey==="topExpDlr"? 'on' : 'off'}`}></div>
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
            {koPrnm().length>=10? <p className='full-product-name'>{korePrnm}</p> : ""}
            <div className='search-detail-box'>
                <div className='search-detail-main'>
                    <LineChart
                        detailData={detailData}
                        metricKey={metricKey}
                    />
                    <GPTRecommend hsSgn={hsSgn}/>
                </div>
                <div>
                    <BarChart
                        detailData={detailData}
                        metricKey={metricKey}
                    />
                    <div className='toBoard'>
                        {/* <PreviewList
                            dataList={{
                                title: '연관 게시글',
                                path: '/community'
                            }}
                        /> */}
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