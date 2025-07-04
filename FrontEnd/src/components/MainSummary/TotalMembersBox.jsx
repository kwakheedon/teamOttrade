import axios from 'axios';
import React, { useEffect, useState, useRef } from 'react'
import NumberFlow from '@number-flow/react'

// 컴포넌트가 마운트될 때 API를 호출하여 데이터를 가져옴
const TotalMembersBox = () => {
  const [totalUsers, setTotalUsers] = useState(0);
  const [totalPosts, setTotalPosts] = useState(0);
  const [error, setError] = useState(null);
  const [isVisible, setIsVisible] = useState(false);
  const countRef = useRef(null);

  // 📌 통계 API 불러오기
  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await axios.get('/api/board/stats');
        const data = response.data?.data || response.data;

        if (response.data && response.data.success) {
          if (typeof data?.totalUsers !== 'undefined') {
            setTotalUsers(data.totalUsers);
            setTotalPosts(data.totalPosts);
          } else {
            console.warn("API 응답 데이터 구조가 예상과 다릅니다:", response.data);
            setError("데이터를 불러오는 데 실패했습니다: 응답 구조 오류");
          }
        } else {
          setError(response.data?.message || "데이터를 불러오는 데 실패했습니다.");
        }
      } catch (err) {
        console.error("통계 정보를 가져오는 데 실패했습니다:", err);
        setError("통계 정보를 가져오는 중 오류가 발생했습니다.");
      }
    };

    fetchStats();
  }, []);

  // 📌 화면 보이는지 감지
  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        setIsVisible(entry.isIntersecting);
      },
      {
        threshold: 0.5,
      }
    );

    if (countRef.current) {
      observer.observe(countRef.current);
    }

    return () => observer.disconnect();
  }, []);

  if (error) {
    return <div className='total-members-box'>에러: {error}</div>;
  }

  return (
    <div className='total-members-box'>
      <div className='main-introduce'>
        <h1>
          데이터 너머의 가능성,<br />
          함께 탐험해보세요.
        </h1>
      </div>
      <div className='total-members-count' ref={countRef}>
        <h2>총 회원 수 : {isVisible ? <NumberFlow value={totalUsers} /> : null}명</h2>
        <h2>총 게시글 수 : {isVisible ? <NumberFlow value={totalPosts} /> : null}개</h2>
      </div>
    </div>
  );
};

export default TotalMembersBox