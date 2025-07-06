// src/components/Common/LikeButton.jsx
import React from 'react'
import PropTypes from 'prop-types'
import { ThumbsUp } from 'lucide-react'
import { motion } from 'framer-motion'
import './LikeButton.css'

const LikeButton = ({
  isLiked = false,
  count = null,
  onClick,
  disabled = false,
  className = '',
  customIcon: CustomIcon = null,
}) => {
  // 기본 아이콘 대신 커스텀 아이콘이 있으면 그것을 쓰고, motion 으로 감싸줍니다.
  const Icon = CustomIcon || ThumbsUp
  const MotionIcon = motion(Icon)

  return (
    <button
      type="button"
      onClick={disabled ? undefined : onClick}
      disabled={disabled}
      className={`like-button ${isLiked ? 'liked' : ''} ${className}`}
    >
      {/* 아이콘 자체에 애니메이션을 걸어서, 클릭 시 scale + color 애니메이션이 동작합니다. */}
      <MotionIcon
        size={18}
        initial={{ fill: 'none', stroke: '#4a5568', scale: 1 }}
        animate={{
          fill:    'none',
          stroke:  isLiked ? '#19D0D0' : '#4a5568',
          scale:   isLiked ? [1, 1.3, 1] : 1,
        }}
        transition={{ duration: 0.3, ease: 'easeInOut' }}
        className="like-button__icon"
      />
      {/* 좋아요 카운트에도 살짝 팝업 효과를 줄 수 있습니다. */}
      {typeof count === 'number' && (
        <motion.span
          initial={{ scale: 1 }}
          animate={{ scale: isLiked ? [1, 1.2, 1] : 1 }}
          transition={{ duration: 0.3 }}
          className="like-button__count"
        >
          {count}
        </motion.span>
      )}
    </button>
  )
}

LikeButton.propTypes = {
  isLiked:     PropTypes.bool,
  count:       PropTypes.number,
  onClick:     PropTypes.func.isRequired,
  disabled:    PropTypes.bool,
  className:   PropTypes.string,
  customIcon:  PropTypes.elementType,
}

export default LikeButton
