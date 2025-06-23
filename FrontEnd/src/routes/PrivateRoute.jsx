import React from 'react'

//오직 보호 기능만 명시하는 라우터
const PrivateRoute = () => {

}

//예시
/*
const PrivateRoute = ({ children }) => {
  const { isLoggedIn } = useAuthStore();
  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }
  return children;
};
*/

export default PrivateRoute