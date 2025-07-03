import { StrictMode, useEffect } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import useAuthStore from './stores/authStore'

const Root = () => {
  const checkAuth = useAuthStore((state) => state.checkAuth);

  useEffect(() => {
    console.log("main의 checkAuth 실행")
    checkAuth();
  }, [checkAuth]);

  return <App />;
};

createRoot(document.getElementById('root')).render(
    // <StrictMode>
      <Root/>
    // </StrictMode>
)
