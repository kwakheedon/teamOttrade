import React, { useRef, useEffect } from 'react';
import './NetworkBackground.css';

export default function NetworkBackground() {
  const canvasRef = useRef(null);
  const mouse = useRef({ x: null, y: null });

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    let nodes = [];
    const nodeCount = 80;
    const maxDist = 120;

    // 캔버스 사이즈 설정
    const resize = () => {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
    };
    resize();
    window.addEventListener('resize', resize);

    // 노드 초기화
    for (let i = 0; i < nodeCount; i++) {
      nodes.push({
        x: Math.random() * canvas.width,
        y: Math.random() * canvas.height,
        vx: (Math.random() - 0.5) * 0.3,
        vy: (Math.random() - 0.5) * 0.3,
      });
    }

    // 마우스 움직임 트래킹
    const handleMouseMove = (e) => {
      mouse.current.x = e.clientX;
      mouse.current.y = e.clientY;
    };
    window.addEventListener('mousemove', handleMouseMove);

    // 애니메이션 루프
    let animId;
    const animate = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      // 각 노드 업데이트 & 그리기
      nodes.forEach(n => {

        // 마우스 당기는 힘: 거리<200px 이내에만, 계수는 0.00005로 낮춤
        if (mouse.current.x !== null) {
            const dxRaw = mouse.current.x - n.x;
            const dyRaw = mouse.current.y - n.y;
            const dist = Math.hypot(dxRaw, dyRaw);
            if (dist < 100) {
                const pullStrength = 0.002;           // ← 계수 조절
                n.vx += (dxRaw / dist) * pullStrength;  // 방향 단위 벡터에 힘을 곱하기
                n.vy += (dyRaw / dist) * pullStrength;
            }else {
                const pullStrengthFar = 0.000003; // 멀리 있는 노드엔 아주 약한 힘(방향만 줌)
                n.vx += (dxRaw / dist) * pullStrengthFar;
                n.vy += (dyRaw / dist) * pullStrengthFar;
            }
        }
        
        // 자연스러운 감속
        n.vx *= 0.99;
        n.vy *= 0.99;

        // 3) 항상 약간의 랜덤 지터 추가 (멈추지 않도록)
        const jitter = 0.01; 
        n.vx += (Math.random() - 0.5) * jitter;
        n.vy += (Math.random() - 0.5) * jitter;

        // 위치 업데이트
        n.x += n.vx;
        n.y += n.vy;

        // 가장자리 바운스
        if (n.x < 0 || n.x > canvas.width) n.vx *= -1;
        if (n.y < 0 || n.y > canvas.height) n.vy *= -1;

        // 점 그리기
        ctx.beginPath();
        ctx.arc(n.x, n.y, 2, 0, Math.PI * 2);
        ctx.fillStyle = 'rgba(100,100,100,0.7)';
        ctx.fill();
      });

      // 가까운 노드들 선으로 연결
      for (let i = 0; i < nodes.length; i++) {
        for (let j = i + 1; j < nodes.length; j++) {
          const a = nodes[i];
          const b = nodes[j];
          const dist = Math.hypot(a.x - b.x, a.y - b.y);
          if (dist < maxDist) {
            const alpha = 1 - dist / maxDist;
            ctx.beginPath();
            ctx.moveTo(a.x, a.y);
            ctx.lineTo(b.x, b.y);
            ctx.strokeStyle = `rgba(150,150,150,${alpha * 0.5})`;
            ctx.lineWidth = 1;
            ctx.stroke();
          }
        }
      }

      animId = requestAnimationFrame(animate);
    };
    animate();

    return () => {
      cancelAnimationFrame(animId);
      window.removeEventListener('resize', resize);
      window.removeEventListener('mousemove', handleMouseMove);
    };
  }, []);

  return <canvas ref={canvasRef} className="network-bg" />;
}
