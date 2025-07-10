# Ottrade (Team: Code 한량)

## 서비스 소개
* Ottrade는 복잡하고 어려운 무역 관련 정보, 특히 HS 코드에 대한 장벽을 낮추고, 사용자 간의 자유로운 정보 공유를 촉진하기 위해 기획된 관세청 API 활용 HS 코드 정보 제공 및 무역 커뮤니티 플랫폼입니다.

* 사용자는 품목명만으로 간편하게 HS 코드를 검색하고, 관련 무역 통계, 상위 수출입 국가, AI를 활용한 시장 전망 분석 리포트까지 한 번에 확인할 수 있습니다. 또한, 커뮤니티 기능을 통해 다른 사용자들과 무역 관련 정보를 교류하고 궁금증을 해결할 수 있습니다.

## 프로젝트 기간
개발 기간: 2024년 5월 22일 ~ 2024년 7월 8일

## ⭐ 주요 기능
|기능|설명|
|---|------|
|품목명 기반 HS 코드 검색|사용자가 찾고자 하는 품목의 이름만 입력하면, 관세청 API와 연동하여 가장 관련성 높은 HS 코드 목록과 평균 세율, 신고 비율 등의 정보를 제공합니다.|
|무역 통계 데이터 시각화|HS 코드를 기반으로 국가별/연도별 수출입 통계 데이터를 조회하고, 이를 직관적인 꺾은선 및 막대그래프로 시각화하여 제공합니다. 이를 통해 사용자는 특정 품목의 무역 동향을 쉽게 파악할 수 있습니다.|
|AI 기반 시장 분석 리포트|OpenAI의 GPT 모델을 활용하여 HS 코드와 관련된 무역 통계 데이터를 심층 분석하고, 유망 진출 국가 추천, 시장 개요, 진출 전략, 잠재 리스크 등을 포함한 전문적인 리포트를 생성합니다.|
|사용자 인증 및 관리|SMS 인증을 통한 자체 회원가입/로그인 기능과 Google 소셜 로그인을 지원합니다. 사용자는 마이페이지에서 자신의 검색 이력을 확인하고 프로필 정보를 수정하거나 계정을 탈퇴할 수 있습니다.|
|커뮤니티 기능|자유게시판, 정보공유 게시판을 통해 사용자들이 자유롭게 글을 작성하고 댓글로 소통할 수 있습니다. 게시글 좋아요, HOT 게시글, 인기 검색어 순위 등의 기능을 제공하여 사용자 참여를 유도합니다.|

## ⛏ 기술스택
<table>
    <tr>
        <th>구분</th>
        <th>내용</th>
    </tr>
    <tr>
        <td>BackEnd</td>
        <td>
            <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"/>
            <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
            <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/>
            <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge"/>
            <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
            <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white"/>
            <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"/>
            <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
        </td>
    </tr>
    <tr>
        <td>FrontEnd</td>
        <td>
            <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black"/>
            <img src="https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white"/>
          <img src="https://img.shields.io/badge/Framer_Motion-0055FF?style=for-the-badge&logo=framer&logoColor=white"/>
        </td>
    </tr>
    <tr>
        <td>개발도구</td>
        <td>
          <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"/> 
          <img src="https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=Eclipse&logoColor=white"/> 
          <img src="https://img.shields.io/badge/VSCode-007ACC?style=for-the-badge&logo=VisualStudioCode&logoColor=white"/>
        </td>
    </tr>
    <tr>
        <td>서버환경</td>
        <td>
             <img src="https://img.shields.io/badge/Apache Tomcat 9.0-D22128?style=for-the-badge&logo=Apache Tomcat&logoColor=white"/> 
        </td>
    </tr>
  <tr>
        <td>라이브러리</td>
        <td>
<img src="https://img.shields.io/badge/React_Router-CA4245?style=for-the-badge&logo=react-router&logoColor=white">
<img src="https://img.shields.io/badge/Axios-007CE2?style=for-the-badge&logo=axios&logoColor=white" >
        </td>
    </tr>
    <tr>
        <td>협업도구</td>
        <td>
            <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white"/>
            <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white"/>
        </td>
    </tr>
</table>


<br>
## ⚙ 시스템 아키텍처

![image](https://github.com/user-attachments/assets/a0d53add-2dcc-4b92-9ba0-c9e8e4c1a1ce)

## 📌 SW유스케이스
![image](https://github.com/user-attachments/assets/685a51ab-f7ab-4dc6-a20b-84efed43c8f6)

## 📌 서비스 흐름도
![image](https://github.com/user-attachments/assets/6135a3a4-238b-4f67-a59c-7f7c85ebeec4)


## 📌 ER다이어그램
![image](https://github.com/user-attachments/assets/69f80494-eaa2-4fe8-8e59-63d3b9122d40)

## 🖥 화면 구성
### 로그인/회원가입/회원괸리/회원수정/회원탈퇴
![회원가입](https://github.com/user-attachments/assets/6bbb82d2-23e2-4222-a8b3-6ec4ee7e259a)
![회원 정보 수정](https://github.com/user-attachments/assets/a6ed440a-4d04-4a73-b2f9-193122ce8a40)
![회원 탈퇴](https://github.com/user-attachments/assets/558e8576-c733-4ad8-ad63-46c0c07c178d)
![메인-로그인창](https://github.com/user-attachments/assets/d6345488-0092-4291-97b6-870f86728f23)


### 메인페이지
![메인1](https://github.com/user-attachments/assets/33530b67-8362-46ec-affd-09e45ad9708e)
![메인2](https://github.com/user-attachments/assets/f66cee61-0f08-43d8-8e11-0b5c20c28247)
![메인3](https://github.com/user-attachments/assets/12a1e009-1728-4290-8e71-dd56f61ec3ef)

### hs코드 검색
![메인-검색](https://github.com/user-attachments/assets/2cf43166-f0e9-454d-bdb2-b414beb23ef3)

### 검색 상세
![검색 상세](https://github.com/user-attachments/assets/fbaffcb3-b67e-4515-bc77-47cf911d1e16)

### 커뮤니티
![자유게시판 작성](https://github.com/user-attachments/assets/92269ab5-3a70-4ab9-8162-6198b1e4e8dc)
![자유게시판](https://github.com/user-attachments/assets/fa018f2f-ff0d-4459-ab01-1263dcbefc24)
![자유게시글 수정,삭제](https://github.com/user-attachments/assets/fbd2056c-8502-4995-beaf-61b9c625df94)

### 조회이력
![조회 이력](https://github.com/user-attachments/assets/b6ae80a6-cfff-4cb8-8f1e-e253902f6ebf)

## 👨‍👩‍👦‍👦 팀원 역할

<table>
  <tr>
    <td align="center"><img src="https://item.kakaocdn.net/do/fd49574de6581aa2a91d82ff6adb6c0115b3f4e3c2033bfd702a321ec6eda72c" width="100" height="100"/></td>
    <td align="center"><img src="https://mb.ntdtv.kr/assets/uploads/2019/01/Screen-Shot-2019-01-08-at-4.31.55-PM-e1546932545978.png" width="100" height="100"/></td>
    <td align="center"><img src="https://mblogthumb-phinf.pstatic.net/20160127_177/krazymouse_1453865104404DjQIi_PNG/%C4%AB%C4%AB%BF%C0%C7%C1%B7%BB%C1%EE_%B6%F3%C0%CC%BE%F0.png?type=w2" width="100" height="100"/></td>
    <td align="center"><img src="https://i.pinimg.com/236x/ed/bb/53/edbb53d4f6dd710431c1140551404af9.jpg" width="100" height="100"/></td>
  </tr>
  <tr>
    <td align="center"><strong>제갈태웅</strong></td>
    <td align="center"><strong>홍성재</strong></td>
    <td align="center"><strong>서명우</strong></td>
    <td align="center"><strong>곽희돈</strong></td>
  </tr>
  <tr>
    <td align="center"><b>BackEnd, PM, AI</b></td>
    <td align="center"><b>Frontend, Design</b></td>
    <td align="center"><b>Backend</b></td>
    <td align="center"><b>FrontEnd</b></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/jegal-taeung" target='_blank'>github</a></td>
    <td align="center"><a href="https://github.com/SeongjaeH123" target='_blank'>github</a></td>
    <td align="center"><a href="https://github.com/smu1212" target='_blank'>github</a></td>
    <td align="center"><a href="https://github.com/kwakheedon" target='_blank'>github</a></td>
  </tr>
</table>

## 🤾‍♂️ 트러블슈팅
![image](https://github.com/user-attachments/assets/e802d050-ef46-4355-98f5-082d3bd95162)




