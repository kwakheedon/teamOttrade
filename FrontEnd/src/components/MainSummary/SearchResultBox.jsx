import React from 'react';
import { useNavigate } from 'react-router-dom';
import useSearchStore from '../../stores/searchStore';
import styled, { createGlobalStyle } from 'styled-components';
import { motion } from 'framer-motion';
import Loading from '../../components/Common/Loading';

// --- 스크롤바 스타일 (전역) ---
const GlobalScrollbarStyle = createGlobalStyle`
  ::-webkit-scrollbar {
    width: 8px;
  }
  ::-webkit-scrollbar-track {
    background: #f0f8ff;
    border-radius: 10px;
  }
  ::-webkit-scrollbar-thumb {
    background: #bde0fe;
    border-radius: 10px;
  }
  ::-webkit-scrollbar-thumb:hover {
    background: #a2d2ff;
  }
`;


// --- 스타일 컴포넌트 정의 ---

const TableWrapper = styled(motion.div)`
  width: 100%;
  max-width: 800px;
  margin-top: 2rem;
  background: linear-gradient(145deg, #f0f8ff, #e6f7ff);
  border-radius: 20px;
  box-shadow: 0 8px 25px rgba(0, 119, 255, 0.1);
  overflow: hidden;
  max-height: 45vh;
  overflow-y: auto;
  display: flex;
`;

const StyledTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  text-align: center;
  
  th {
    padding: 16px 12px;
    background-color: #dcf2ff;
    color: #005a9e;
    font-weight: 600;
    font-size: 0.95rem;
    position: sticky;
    top: 0;
    z-index: 1;
  }
  
  @media (max-width: 768px) {
    thead {
      display: none;
    }
  }
`;

const MotionRow = styled(motion.tr)`
  cursor: pointer;
  
  td {
    padding: 16px 12px;
    border-bottom: 1px solid #e0f4ff;
    color: #34495e;
    font-size: 0.9rem;
    background-color: #ffffff;
  }
  
  &:last-child td {
    border-bottom: none;
  }
  
  @media (max-width: 768px) {
    display: block;
    background-color: #ffffff;
    border-radius: 15px;
    margin: 12px;
    box-shadow: 0 4px 10px rgba(0, 119, 255, 0.08);
    overflow: hidden;
    
    td {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      border-bottom: 1px solid #e0f4ff;
      text-align: right;
      
      &::before {
        content: attr(data-label);
        font-weight: 600;
        color: #0077ff;
      }

      &:last-child {
        border-bottom: none;
      }
    }
  }
`;

// --- 애니메이션 설정 ---

// 컨테이너: 전체 테이블의 등장 애니메이션
const containerVariants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.07, // 각 행이 0.07초 간격으로 나타남
    },
  },
};

// 행(Row): 각 행의 등장 및 인터랙션 애니메이션
const rowVariants = {
  hidden: { opacity: 0, x: -30 }, // 왼쪽 밖에서 투명한 상태로 시작
  visible: {
    opacity: 1,
    x: 0, // 제자리로 이동하며 나타남
    transition: { type: 'spring', stiffness: 260, damping: 20 },
  },
};

// --- 컴포넌트 ---

const SearchResultBox = ({ hsList, item }) => {
  const navigate = useNavigate();
  const setSearchItem = useSearchStore((state) => state.setSearchItem);

  const searchItem = (hsSgn, korePrnm) => {
    setSearchItem(hsSgn, korePrnm);
    navigate(`/search/${hsSgn}?korePrnm=${korePrnm}&item=${item}`);
  };

  if (!hsList) {
    return <Loading />;
  }

  return (
    <>
      <GlobalScrollbarStyle />
      <TableWrapper
        variants={containerVariants}
        initial="hidden"
        animate="visible"
      >
        <StyledTable>
          <thead>
            <tr>
              <th>순번</th>
              <th>HS코드</th>
              <th>신고 비율</th>
              <th>HS 품목 해설</th>
              <th>평균 세율</th>
            </tr>
          </thead>
          <tbody>
            {hsList.map((data, index) => (
              <MotionRow
                key={index}
                onClick={() => searchItem(data.hsSgn, data.korePrnm)}
                variants={rowVariants}
                // 마우스 올렸을 때 효과 (리프트 업)
                whileHover={{ scale: 1.02, backgroundColor: 'rgba(220, 242, 255, 0.7)' }}
                // 클릭했을 때 효과 (탭)
                whileTap={{ scale: 0.98 }}
                // 모든 인터랙션에 자연스러운 스프링 효과 적용
                transition={{ type: 'spring', stiffness: 400, damping: 15 }}
              >
                <td data-label="순번">{index + 1}</td>
                <td data-label="HS코드">{data.hsSgn}</td>
                <td data-label="신고 비율">{data.rate}%</td>
                <td data-label="HS 품목 해설">{data.korePrnm}</td>
                <td data-label="평균 세율">{data.avgTxrt}%</td>
              </MotionRow>
            ))}
          </tbody>
        </StyledTable>
      </TableWrapper>
    </>
  );
};

export default SearchResultBox;