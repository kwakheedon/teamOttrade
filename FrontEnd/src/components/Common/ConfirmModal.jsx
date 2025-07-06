// src/components/ConfirmModal.jsx
import React from 'react'
import ReactDOM from 'react-dom'
import { motion, AnimatePresence } from 'framer-motion'
import './ConfirmModal.css'

const backdropVariants = {
  hidden: { opacity: 0 },
  visible: { opacity: 1 }
}

const modalVariants = {
  hidden: { scale: 0.8, opacity: 0 },
  visible: { 
    scale: 1, 
    opacity: 1,
    transition: { duration: 0.25, ease: 'easeOut' }
  }
}

export default function ConfirmModal({ 
  isOpen, 
  message = '정말 진행하시겠습니까?', 
  onConfirm, 
  onCancel 
}) {
    const modal = (
        <AnimatePresence>
            {isOpen && (
                <>
                {/* 백드롭 */}
                <motion.div
                    className="modal-backdrop"
                    variants={backdropVariants}
                    initial="hidden"
                    animate="visible"
                    exit="hidden"
                    onClick={onCancel}
                />

                {/* 모달 내용 */}
                <motion.div
                    className="modal-content"
                    variants={modalVariants}
                    initial="hidden"
                    animate="visible"
                    exit="hidden"
                >
                    <p className="modal-message">{message}</p>
                    <div className="modal-buttons">
                    <button className="btn btn-cancel" onClick={onCancel}>
                        취소
                    </button>
                    <button className="btn btn-confirm" onClick={onConfirm}>
                        확인
                    </button>
                    </div>
                </motion.div>
                </>
            )}
        </AnimatePresence>
    )
  return ReactDOM.createPortal(modal, document.body)
}
