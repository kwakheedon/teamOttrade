import { useState } from "react";
import { Trash2, AlertTriangle } from "lucide-react";
import "./AccountDeletePage.css"; // 분리된 CSS 파일 import

// Button 컴포넌트는 별도로 정의되어 있다고 가정합니다.
// 실제 프로젝트에서는 해당 경로에서 Button 컴포넌트를 가져와야 합니다.
const Button = ({ children, onClick, disabled, variant, className }) => (
  <button onClick={onClick} disabled={disabled} className={`${className} button ${variant ? `button-${variant}` : ''}`}>
    {children}
  </button>
);

export default function AccountDeletePage({ onClose }) {
  const [confirmPhrase, setConfirmPhrase] = useState("");
  const [error, setError] = useState("");
  const [isDeleting, setIsDeleting] = useState(false);

  const isPhraseValid = confirmPhrase === "DELETE ACCOUNT";

  const handleDelete = async () => {
    if (!isPhraseValid) {
      setError("계정 삭제를 진행하려면 확인 단계를 완료해주세요.");
      return;
    }
    setIsDeleting(true);
    try {
      // TODO: 실제 회원탈퇴 API 호출
      await new Promise((resolve) => setTimeout(resolve, 2000)); // 임시 딜레이
      onClose();
    } catch (err) {
      setError("계정 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-container" onClick={(e) => e.stopPropagation()}>
        <div className="card">
          <div className="card-header">
            <h1 className="card-title">계정 탈퇴</h1>
            <p className="card-subtitle">
              이 작업은 계정과 모든 관련 데이터를 영구적으로 삭제합니다.
            </p>
          </div>

          {/* 중요 경고 영역 */}
          <div className="warning-section">
            <div className="section-title warning-title">
              <Trash2 className="icon icon-red" />
              <span>Ottrade 계정 탈퇴</span>
            </div>
            <div className="info-box warning-box">
              <ul>
                <li>• 설정 및 모니터링 데이터를 포함한 모든 관련 데이터가 삭제됩니다</li>
                <li>• 이 작업은 취소할 수 없습니다</li>
              </ul>
            </div>
          </div>

          {/* 주의사항 영역 */}
          <div className="notice-section">
            <div className="section-title notice-title">
              <AlertTriangle className="icon icon-blue" />
              <span>계정 삭제 시 주의사항</span>
            </div>
            <div className="info-box notice-box">
              <ul>
                <li>• Google 계정이 SOLAR에서 연결이 끊어집니다</li>
                <li>• 모든 배터리 모니터링 기록 및 분석은 영구적으로 삭제됩니다</li>
                <li>• 사용자 지정 설정 및 구성이 손실됩니다</li>
                <li>• 나중에 동일한 소셜 계정으로 재등록할 수 있습니다</li>
              </ul>
            </div>
          </div>

          {/* 확인 입력 영역 */}
          <div className="confirm-section">
            <label htmlFor="confirm-input" className="confirm-label">계정 삭제 확인</label>
            <p className="confirm-description">
              "DELETE ACCOUNT" 문구를 정확히 입력해 주세요
            </p>
            <input
              id="confirm-input"
              type="text"
              value={confirmPhrase}
              onChange={(e) => {
                setConfirmPhrase(e.target.value);
                setError("");
              }}
              placeholder="DELETE ACCOUNT"
              className="confirm-input"
              disabled={isDeleting}
            />
            {error && (
              <div className="error-message">
                {error}
              </div>
            )}
          </div>

          {/* 버튼 그룹 */}
          <div className="button-group">
            <Button
              onClick={handleDelete}
              disabled={!isPhraseValid || isDeleting}
              className="button-delete"
            >
              {isDeleting ? "삭제 중..." : "계정 영구 삭제"}
            </Button>
            <Button
              variant="outline"
              className="button-cancel"
              onClick={onClose}
              disabled={isDeleting}
            >
              취소
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}