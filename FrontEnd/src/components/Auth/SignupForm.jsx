import React, { useState, useEffect } from 'react';
import SocialLogin from './SocialLogin';
import './SignUpForm.css';
import { useNavigate } from 'react-router';

const SignUpForm = () => {
  const [remainingTime, setRemainingTime] = useState(0);
  const [isCounting, setIsCounting] = useState(false);

  // 화살표 버튼 누르면 메인 페이지로 이동
  const navigate = useNavigate();
  const goBack = () => {
    navigate('/')
  }

  const handleSendVerification = () => {
    setRemainingTime(180);
    setIsCounting(true);
  };

  useEffect(() => {
    let timerId;
    if (isCounting && remainingTime > 0) {
      timerId = setInterval(() => {
        setRemainingTime(prevTime => prevTime - 1);
      }, 1000);
    } else if (remainingTime === 0 && isCounting) {
      setIsCounting(false);
      clearInterval(timerId);
    }

    return () => clearInterval(timerId);
  }, [isCounting, remainingTime]);

  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <div className="signup-container">
      <button className="back-button" onClick={goBack}>&larr;</button>

      <h2 className="signup-title">회원 가입</h2>

      <div className="input-group">
        <input
          type="text"
          className="signup-input"
          placeholder='전화번호'
        />
        <button className="send-button" onClick={handleSendVerification}>
          인증 번호 발송
        </button>
        {isCounting && (
          <p className="timer-text">남은 시간 : {formatTime(remainingTime)}</p>
        )}
        <input
          type="text"
          className="signup-input"
          placeholder='인증번호'
        />
      </div>

      <div className="input-group">
        <input
          type="password"
          className="signup-input"
          placeholder='비밀번호'
        />
        <input
          type="text"
          className="signup-input"
          placeholder='닉네임'
        />
      </div>

      <SocialLogin/>
    </div>
  );
};

export default SignUpForm;