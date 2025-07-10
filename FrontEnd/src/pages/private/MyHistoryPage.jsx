import React, { useEffect, useState } from 'react'
import styles from './MyHistoryPage.module.css' // 이 부분이 CSS 파일을 불러옵니다.
import axios from '../../apis/authApi'
import Loading from '../../components/Common/Loading'
import { useNavigate } from 'react-router'
import useSearchStore from '../../stores/searchStore'
import ConfirmModal from '../../components/Common/ConfirmModal'

const MyHistoryPage = () => {
    const navigate = useNavigate()
    const setSearchItem = useSearchStore((state) => state.setSearchItem)
    const [myHistory, setMyHistory] = useState(null)

    const getMyHistory = async () => {
        try {
            const res = await axios.get('api/logs/my-history')
            const data = res.data.data
            // 날짜 최신순으로 정렬
            data.sort((a, b) => new Date(b.searchedAt) - new Date(a.searchedAt));
            setMyHistory(data)
        } catch (err) {
            console.log(err)
        }
    }

    // 상세 페이지로 이동하는 함수
    const goSearchDetail = (item) => {
        setSearchItem(item.keyword, item.korePrnm)
        navigate(`/search/${item.keyword}?korePrnm=${item.korePrnm}`, {
            state: {
                logId: item.id,
                gptSummary: item.gptSummary
            }
        })
    }

    //내역을 삭제하는 함수
    const [selectedLogId, setSelectedLogId] = useState(null)
    const [modalOpen, setModalOpen] = useState(false)
    const deleteHistory = async (logId) => {
        try {
            await axios.delete(`/api/logs/my-history/${logId}`)
            setModalOpen(false)
            setSelectedLogId(null)
            await getMyHistory()
        } catch (err) {
            console.error(`내역 삭제 오류: `, err)
        }
    }

    useEffect(() => {
        getMyHistory()
    }, [])

    if (!myHistory) {
        return <Loading />
    }

    return (
        <div className={styles.tableWrapper}>
            <table className={styles.styledTable}>
                <thead>
                    <tr>
                        <th>HS코드</th>
                        <th>품목 해설</th>
                        <th>검색 날짜</th>
                        <th>기타</th>
                    </tr>
                </thead>
                <tbody>
                    {myHistory.map((item) => (
                        <tr key={item.id} className={styles.tableRow} onClick={() => goSearchDetail(item)}>
                            <td>{item.keyword}</td>
                            <td>{item.korePrnm}</td>
                            <td>{new Date(item.searchedAt).toLocaleString('ko-KR')}</td>
                            <td>
                                <button
                                    className={styles.goButton}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        goSearchDetail(item)
                                    }}
                                >
                                    이동
                                </button>
                                <button
                                    className={styles.delButton}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setSelectedLogId(item.id)
                                        setModalOpen(true)
                                    }}
                                >
                                    삭제
                                </button>
                            </td>                            
                        </tr>
                    ))}
                </tbody>
            </table>
            <ConfirmModal
                isOpen={modalOpen}
                message='정말 삭제하시겠습니까? 이 작업은 중단할 수 없습니다.'
                onConfirm={() => deleteHistory(selectedLogId)}
                onCancel={() => {
                    setModalOpen(false)
                    setSelectedLogId(null)
                }}
            />
        </div>
    )
}

export default MyHistoryPage