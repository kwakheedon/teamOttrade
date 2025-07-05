import axios from '../../apis/authApi'
import './SearchDetailPage.css'
import React, { useEffect, useState } from 'react'
import { useLocation, useParams, useSearchParams } from 'react-router-dom'
import Loading from '../../components/Common/Loading'
import LineChart from '../../components/Search/LineChart'
import BarChart from '../../components/Search/BarChart'
import GPTRecommend from '../../components/Ai/GPTRecommend'
import CountrySelectorModal from '../../components/Search/CountrySelectorModal'
import tempDataset from '../../assets/data/hs_top3_result.json'
import PreviewList2 from '../../components/Common/PreviewList2'
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

    //location
    const location = useLocation()
    const gptSummary = location.state?.gptSummary || '';

    //url매핑
    const { hsSgn } = useParams()
    const [searchParams] = useSearchParams()
    const korePrnm = searchParams.get('korePrnm') || ''
    const item = searchParams.get('item') || ''
    console.log("item 확인용: ",searchParams.get('item'))
    const koPrnm = (str = korePrnm, maxLength = 10) => { //이름 설정
        return str.length > maxLength?
        str.slice(0, maxLength) + '…'
        : str;
    }
    const params = new URLSearchParams();
    params.append('korePrnm', korePrnm); 

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
            const res = await axios.get(path, { params })
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
            const res = await axios.get(path, { params })
            setDetailData(null)
            const data = res.data
            console.log('선택한 물품의 top3+선택 국가 출력 : ',data)
            setDetailData(data)
        } catch (err) {
            console.error(err)
        }
    }

    const getMyHistoryDetail = async () => {
        try {
            const path = `/api/logs/my-history/${location.state.logId}`
            const res = await axios.get(path)
            const data = res.data.data
            console.log("히스토리에서 불러온 데이터: ", data)
            setMetricKey(initExpImp(data))
            setDetailData(data)
        } catch (err) {
            console.error(err)
        }
    }

    const initExpImp = (data) => {
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
        if(location.state) {
            getMyHistoryDetail()
        } else if(selectedCountry) {
            setDetailData(null)
            getDetailCountry()
        } else
            getDetail()
    }, [hsSgn, selectedCountry])

    // **테스트용 더미데이터 사용시
    // useEffect(() => {
    //     setDetailData(tempDataset)
    //     setMetricKey(initExpImp(tempDataset))
    // }, [])

    //국가 선택시 차트 업데이트
    const handleSelect = (e) => {
        setModalOpen(false)
        setSelectedCountry(e.currentTarget.dataset.code)
        console.log("선택한 국가:",e.currentTarget.dataset.code)
    }

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
                <div className='search-options'>
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
                            <button onClick={() => setModalOpen(true)} className='country-select-btn'>
                                국가 선택 버튼
                            </button>
                        </div>
                        <CountrySelectorModal
                            show={isModalOpen}
                            onClose={() => setModalOpen(false)}
                            onSelect={handleSelect}
                        />
                    </div>
                </div>
            </div>
            {koPrnm().length>=10? <p className='full-product-name'>{korePrnm}</p> : ""}
            <div className='search-detail-box'>
                <div className='search-detail-main'>
                    <LineChart
                        detailData={detailData}
                        metricKey={metricKey}
                    />
                    <GPTRecommend hsSgn={hsSgn} gptSummary={gptSummary}/>
                </div>
                <div>
                    <BarChart
                        detailData={detailData}
                        metricKey={metricKey}
                    />
                    <div className='toBoard'>
                        <PreviewList2
                            dataList={{
                                title: '연관 게시글',
                                path: '/community'
                            }}
                            keyword={item}
                        />
                    </div>
                </div>
            </div>
            <div className='search-detail-button-box'>
                <button className='share-btn'>Share</button>
            </div>
        </div>
    )
} 

export default SearchDetailPage