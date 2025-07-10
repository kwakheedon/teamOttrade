import React from 'react'
import './Loading.css' // CSS 파일을 임포트합니다.

const Loading = () => {
  return (
    // "loading-overlay" 클래스를 적용하여 화면 전체를 덮습니다.
    <div className="loading-overlay">
      {/* "loading-spinner" 클래스로 스피너 모양을 만듭니다. */}
      <div className="loading-spinner"></div>
    </div>
  )
}

export default Loading