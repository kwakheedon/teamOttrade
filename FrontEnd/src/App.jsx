import React from 'react';
import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import Header from './components/Header/Header';
import { PublicRoutes } from './routes/PublicRoutes';
import { PrivateRoutes } from './routes/PrivateRoutes';
import PrivateRoute from './routes/PrivateRoute';
import { AnimatePresence, motion } from 'framer-motion';

// "서서히 떠오르는" 느낌의 페이지 전환 애니메이션 효과 정의
const pageVariants = {
  initial: { opacity: 0, y: 40 },
  in: { opacity: 1, y: 0 },
  out: { opacity: 0, y: -40 }
};

// 애니메이션 전환 속성 정의
const pageTransition = {
  type: "tween",
  ease: "easeInOut",
  duration: 0.3
};

// const excludedPaths = ['/login', '/signup'];

/**
 * 애니메이션 효과가 적용된 라우트를 관리하는 컴포넌트
 */
const AnimatedRoutes = () => {
  const location = useLocation();

  // const isExcluded = excludedPaths.some(path =>
  //   matchPath({ path, end: true }, location.pathname)
  // );

  // if (isExcluded) {
  //   // 애니메이션 없이 일반 Routes 렌더링
  //   return (
  //     <Routes location={location}>
  //       {PublicRoutes}
  //       <Route element={<PrivateRoute/>}>
  //         {PrivateRoutes}
  //       </Route>
  //     </Routes>
  //   );
  // }

  return (
    <AnimatePresence mode="wait">
      <motion.div
        key={location.pathname}
        initial="initial"
        animate="in"
        exit="out"
        variants={pageVariants}
        transition={pageTransition}
        style={{
          // 애니메이션을 위해 position: absolute는 유지합니다.
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%'
        }}
      >
        <Routes location={location}>
          {PublicRoutes}
          <Route element={<PrivateRoute/>} >
            {PrivateRoutes}
          </Route>
        </Routes>
      </motion.div>
    </AnimatePresence>
  );
};

const App = () => {
  return (
    <BrowserRouter>
      {/* 헤더는 그대로 유지됩니다. */}
      <Header/>

      {/*
        메인 콘텐츠 영역입니다.
        헤더가 고정되어 있더라도 콘텐츠가 가려지지 않도록
        헤더의 높이만큼 상단에 여백(padding)을 줍니다.
        (헤더 높이를 70px로 가정)
      */}
      <main style={{ paddingTop: '70px' }}>
        {/*
          이 div는 애니메이션이 적용될 페이지들의 기준점이 됩니다.
          position: relative을 설정하여 이 내부에 있는
          position: absolute 요소가 이 div를 기준으로 위치하게 합니다.
        */}
        <div style={{ position: 'relative', height: 'calc(100vh - 70px)' }}>
          <AnimatedRoutes />
        </div>
      </main>
    </BrowserRouter>
  );
};

export default App;