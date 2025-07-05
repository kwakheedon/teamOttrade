import React, { useRef, useEffect, useState } from 'react'
import { motion, useAnimation } from 'framer-motion'
import PreviewList from '../Common/PreviewList'

const TotalPostsBox = () => {
  const sectionRef = useRef(null)
  const controls = useAnimation()
  const [isVisible, setIsVisible] = useState(false)

  // IntersectionObserver: 화면에 보일 때 애니메이션 실행
  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        setIsVisible(entry.isIntersecting)
        if (entry.isIntersecting) {
          controls.start({
            opacity: 1,
            y: 0,
            transition: { duration: 0.6, ease: 'easeOut' }
          })
        }
      },
      {
        threshold: 0.4,
      }
    )

    if (sectionRef.current) {
      observer.observe(sectionRef.current)
    }

    return () => observer.disconnect()
  }, [controls])

  return (
    <motion.div
      className='total-posts-box'
      ref={sectionRef}
      initial={{ opacity: 0, y: 30 }}
      animate={controls}
    >
      <section className="community-section">
        <motion.div
          className='community-title'
          initial={{ opacity: 0, y: 20 }}
          animate={isVisible ? { opacity: 1, y: 0 } : { opacity: 0, y: 20 }}
          transition={{ duration: 0.5 }}
        >
          <h2>커뮤니티</h2>
          <h1>정보를 모두 공유하세요!</h1>
        </motion.div>

        <motion.div
          className="community-content"
          initial={{ opacity: 0, y: 20 }}
          animate={isVisible ? { opacity: 1, y: 0 } : { opacity: 0, y: 20 }}
          transition={{ duration: 0.6, delay: 0.2 }}
        >
          <div className="community-section">
            <PreviewList
              type={'free'}
              dataList={{
                title: "자유게시판",
                path: "/board"
              }}
            />
          </div>
          <div className="community-section">
            <PreviewList
              type={'info'}
              dataList={{
                title: "정보 공유",
                path: "/infoShare"
              }}
            />
          </div>
        </motion.div>
      </section>
    </motion.div>
  )
}

export default TotalPostsBox
