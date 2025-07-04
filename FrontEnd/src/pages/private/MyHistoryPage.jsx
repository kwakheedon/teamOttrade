import React, { useEffect, useState } from 'react'
import './MyHistoryPage.css'
import axios from '../../apis/authApi'
import Loading from '../../components/Common/Loading'
import { useNavigate } from 'react-router'
import useSearchStore from '../../stores/searchStore'

const MyHistoryPage = () => {
    const navigate = useNavigate()
    const setSearchItem = useSearchStore((state) => state.setSearchItem)

    const [myHistory, setMyHistory] = useState(null)

    const getMyHistory = async () => {
        try {
            const res = await axios.get('api/logs/my-history')
            if(res.status!==200)
                console.log("알수없는 오류 발생")
            const data = res.data.data
            setMyHistory(data)
            console.log("history 배열:", myHistory)
        } catch (err) {
            console.log(err)
        }
    }

    const goSearchDetail = (idx) => {
        setSearchItem(myHistory[idx].keyword, myHistory[idx].korePrnm)
        navigate(`/search/${myHistory[idx].keyword}?korePrnm=${myHistory[idx].korePrnm}`,{
            state: {
                logId: myHistory[idx].id,
                gptSummary: myHistory[idx].gptSummary
            }
        })
    }

    useEffect(() => {
        getMyHistory()
    }, [])

    if(!myHistory) {
        return <Loading/>
    }

    return (
        <div className='my-history-box'>
            <table>
                <thead>
                    <tr>
                        <th>기록 ID</th>
                        <th>HS코드</th>
                        <th>품목 해설</th>
                        <th>검색 날짜</th>
                    </tr>
                </thead>
                <tbody>
                    {myHistory.map((item, idx) => (
                        <tr key={item.id}>
                            <td>{item.id}</td>
                            <td>{item.keyword}</td>
                            <td>{item.korePrnm}</td>
                            <td>{item.searchedAt}</td>
                            <td>
                                <button
                                    onClick={() => goSearchDetail(idx)}
                                >
                                    이동
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}

export default MyHistoryPage