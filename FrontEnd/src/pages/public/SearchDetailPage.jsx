import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router'
import LineChart from '../../components/Search/LineChart'
import BarChart from '../../components/Search/BarChart'

const SearchDetailPage = () => {

    const [hsSgn, setHsSgn] = useState(useParams().hsSgn)
    const [detailData, setDetailData] = useState()

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

    useEffect(() => { getDetail(hsSgn) }, [hsSgn])

    if(!detailData) {
        return (
            <div>
                로딩중...
            </div>
        )
    }

    return (
        <div className='search-detail-page-box'>
            <div>
                <h1>물품명</h1>
                <div>
                    수출 / 수입
                    <div>
                        <button>토글버튼자리</button>
                    </div>
                </div>
                <div>
                    국가 선택
                    <div>
                        <button>국가 선택 버튼 자리</button>
                    </div>
                </div>
            </div>
            <div>
                <LineChart/>
                <div>
                    GTP기반 유망 국가 출력할 부분 혹시 버튼으로 하나?
                </div>
            </div>
            <div>
                {/* 차트 잘못 만들었음,
                차트 컴포넌트는 차트 관련된것 이외에 다른 건 가지면 안됨
                아마 default export를 허용하면 안된다는 것 같은데
                아마? */}
                {/* <BarChart detailData={detailData}/> */}
                <div>
                    연관 게시글 뜨는 부분
                </div>
            </div>
        </div>
    )
}

export default SearchDetailPage