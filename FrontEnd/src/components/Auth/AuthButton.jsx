import React, { useEffect, useRef, useState } from 'react'
import './AuthButton.css'
import useAuthStore from '../../stores/authStore'
import { motion, AnimatePresence } from "framer-motion"
import AuthForm from "./AuthForm"

// 헤더의 로그인/회원가입 버튼
// zustand로 로그인 여부를 파악하고 로그인/로그아웃 전환
const AuthButton = () => {
  const { isAuthenticated } = useAuthStore();
  const [isOpen, setIsOpen] = useState(false);
  const wrapperRef = useRef(null)

  useEffect(() => {
    // 로그인폼 바깥 부분을 누르면 로그인폼이 꺼지는 로직
    const handleClickOutside = (e) => {
      if (wrapperRef.current && !wrapperRef.current.contains(e.target)) {
        setIsOpen(false)
      }
    }
    // 창이 열리면 handleClickOutside 함수를 document에 추가
    // 
    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside)
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [isOpen])

  const closeAuthForm = () => {
    setIsOpen(false) //창 닫기
  }

  return (
    <div
      className='auth-button-box'
      ref={wrapperRef}
    >
      <motion.div
        className='auth-button'
        initial={false}
        animate={{
          width: isOpen? 300 : 100,
          height: isOpen ? 350 : 40,
          borderRadius: isOpen ? 20 : 10,
          backgroundColor: "#F5F7FA",
          padding: isOpen? 20 : 10,
          cursor: isOpen? 'auto' : 'pointer'
        }}
        transition={{ 
          duration: 0.5,
          ease: "easeInOut"
        }}
        onClick={() => {
            if (!isOpen) 
              setIsOpen(true);
        }}
      >
        <AnimatePresence>
            {!isOpen? (
              <motion.span
                key="login-text"
                initial={{ opacity: 1 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                transition={{ delay: 0.3, duration: 0.3 }}
              >
                로그인
              </motion.span>
            )
            :
            (
              <motion.span
                key="login-text"
                initial={{ opacity: 1 }}
                animate={{ opacity: 0, display: 'none'}}
                exit={{ opacity: 0 }}
                // transition={{ duration: 0.3 }}
              >
                로그인
              </motion.span>
            )
            }
        </AnimatePresence>

        <AnimatePresence>
          {isOpen && (
            <motion.div
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.8 }}
              transition={{ delay: 0 }}
              style={{ width: "100%" }}
            >
              <AuthForm closeAuthForm = {closeAuthForm} isOpen = {isOpen}/>
            </motion.div>
          )}
        </AnimatePresence>
      </motion.div>
        {/* {isAuthenticated? '로그아웃' : '로그인'} */}
    </div>
  )
}

export default AuthButton