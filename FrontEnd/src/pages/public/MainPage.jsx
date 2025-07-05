import React, { useRef, useState, useEffect } from 'react'; // useState, useEffect 추가
import './MainPage.css';
import MainToper from '../../components/MainSummary/MainToper';
import TotalMembersBox from '../../components/MainSummary/TotalMembersBox';
import TotalPostsBox from '../../components/MainSummary/TotalPostsBox';

const MainPage = () => {
  const scrollTargetRef = useRef(null);
  // 두 번째 섹션이 화면에 보이는지 여부를 저장할 state
  const [isBottomVisible, setIsBottomVisible] = useState(false);

  const handleScrollClick = () => {
    scrollTargetRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  // --- 👇 이 부분을 추가해주세요 ---
  useEffect(() => {
    // IntersectionObserver: 특정 요소가 화면에 나타나는지 감지
    const observer = new IntersectionObserver(
      ([entry]) => {
        // entry.isIntersecting은 '관찰 대상(scrollTargetRef)이 화면에 보이는가?'에 대한 boolean 값입니다.
        setIsBottomVisible(entry.isIntersecting);
      },
      { threshold: 0.1 } // 관찰 대상이 10% 이상 보일 때 감지
    );

    const currentRef = scrollTargetRef.current;
    if (currentRef) {
      observer.observe(currentRef); // 관찰 시작
    }

    // 컴포넌트가 사라질 때 관찰을 중단합니다 (메모리 누수 방지)
    return () => {
      if (currentRef) {
        observer.unobserve(currentRef);
      }
    };
  }, []); // 빈 배열을 전달하여 컴포넌트가 처음 렌더링될 때 한 번만 실행되도록 설정

  return (
    <div className="main-page">
      <section className="section-top">
        <MainToper />
      </section>

      {/* --- 👇 isBottomVisible이 false일 때만 버튼이 보이도록 수정 --- */}
      {!isBottomVisible && (
        <button className='scrollBtn' onClick={handleScrollClick}>
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M19 9l-7 7-7-7" stroke="#3b3b3b" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </button>
      )}

      <section className="section-bottom" ref={scrollTargetRef}>
        <TotalMembersBox />
        <TotalPostsBox />
      </section>
    </div>
  );
};

export default MainPage;