import React, { useRef, useState, useEffect, useCallback } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import './GradientButton.css';

// Hook to get previous value for delay logic
function usePrevious(value) {
  const ref = useRef(value);
  useEffect(() => {
    ref.current = value;
  }, [value]);
  return ref.current;
}

/**
 * AI 트렌드에 어울리는 네온 글로우 스타일의 그라디언트 버튼 컴포넌트
 * - proximity hover 감지
 * - 클릭 시 부드러운 축소 효과
 * - 로딩 중에는 스피너만 표시
 * - 로딩 완료 후 width 애니메이션 및 텍스트 딜레이 페이드인
 */
export default function GradientButton({
  children,
  onClick,
  className = '',
  proximity = 60,
  loadingDuration = 600,
  disabled = false,
  forbidden = false,
  ...props
}) {
    const btnRef = useRef(null);
    const [isProximityHover, setIsProximityHover] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    // 이전 로딩 상태를 추적해, 로딩 완료 직후를 감지
    const prevLoading = usePrevious(isLoading);
    const justLoaded = prevLoading && !isLoading;

    // proximity hover
    useEffect(() => {
        const handleMouseMove = (e) => {
        if (disabled || !btnRef.current) {
            setIsProximityHover(false);
            return;
        }
        const rect = btnRef.current.getBoundingClientRect();
        const dx = Math.max(rect.left - e.clientX, 0, e.clientX - rect.right);
        const dy = Math.max(rect.top - e.clientY, 0, e.clientY - rect.bottom);
        setIsProximityHover(Math.hypot(dx, dy) < proximity);
        };
        window.addEventListener('mousemove', handleMouseMove);
        return () => window.removeEventListener('mousemove', handleMouseMove);
    }, [proximity, disabled]);

    // click handler
    const handleClick = useCallback((e) => {
        if (disabled) return;
        setIsLoading(true);
        onClick?.(e);
        setTimeout(() => setIsLoading(false), loadingDuration);
    }, [onClick, loadingDuration, disabled]);

    // animated properties (disabled 시 기본값 유지)
    const animateProps = {
        scale: (!disabled && isProximityHover) ? 1.05 : 1,
        width: isLoading ? '10%' : '20%',
        height: '2.5rem'
    };

    return (
        <motion.button
            ref={btnRef}
            className={`gradient-button ${disabled ? 'disabled-button' : ''} ${forbidden? 'forbbiden-button' : ''} ${className}`}
            onClick={handleClick}
            disabled={disabled}
            initial={false}
            animate={animateProps}
            whileTap={!disabled ? { scale: 0.95 } : {}}
            transition={{
                type: 'spring',
                stiffness: 300,
                damping: 25,
                mass: 0.5
            }}
            {...props}
        >
        <AnimatePresence exitBeforeEnter>
            {isLoading ? (
            <motion.span
                key="spinner"
                className="spinner-ring"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                transition={{ duration: 0.1 }}
            />
            ) : (
            <motion.span
                key="text"
                className="button-text"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                transition={{
                duration: justLoaded ? 0.1 : 0,
                delay: justLoaded ? 0.2 : 0
                }}
            >
                {children}
            </motion.span>
            )}
        </AnimatePresence>
    </motion.button>
  );
}