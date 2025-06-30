★POST /api/auth/signup API에 요청

AuthController
1 signup(@RequestBody AuthDto.SignUpRequest request) 메소드가 HTTP 요청을 받음
2 @RequestBody 어노테이션이 클라이언트가 보낸 JSON을 AuthDto.SignUpRequest 객체로 변환
3 핵심 로직 처리를 위해 authService.signup(request)를 호출하며 SignUpRequest 객체를 그대로 전달합니다.

AuthService
4 signup(AuthDto.SignUpRequest request) 메소드가 실행됩니다. @Transactional이 붙어있어 이 메소드 내의 모든 DB 작업은 하나의 트랜잭션으로 묶여짐
5 userRepository.existsByEmail(request.getEmail())을 호출하여 이메일 중복 여부를 확인합니다. 만약 중복이라면 CustomException을 발생시킵니다.
6 userRepository.existsByPhone(request.getPhone())을 호출하여 휴대폰 번호 중복 여부를 확인합니다. 만약 중복이라면 CustomException을 발생시킵니다.
7 request.toEntity(passwordEncoder)를 호출하여 DTO를 User 엔티티로 변환합니다.

AuthDto.SignUpRequest
8 toEntity(PasswordEncoder passwordEncoder) 메소드실행
9 passwordEncoder.encode(this.password)를 통해 사용자의 평문 비밀번호를 암호화.
10 User.builder()를 사용하여 이메일, 암호화된 비밀번호, 닉네임, 전화번호, 기본 역할(Role.USER)을 가진 User 엔티티 객체를 생성하고 AuthService로 반환합니다.

AuthService
11 생성된 User 엔티티를 userRepository.save(user) 메소드에 전달하여 데이터베이스에 저장

UserRepository
12 Spring Data JPA가 save 메소드에 해당하는 INSERT SQL 쿼리를 실행하여 users 테이블에 새로운 레코드를 추가합니다.


AuthService의 signup 메소드가 성공적으로 종료됩니다.
AuthController는 ResponseEntity.ok(ApiResponse.success("...", null))를 통해 HTTP 200 OK 상태 코드와 성공 메시지를 클라이언트에게 응답합니다.
※ 예외 발생 시: AuthService에서 CustomException이 발생하면, GlobalExceptionHandler가 이를 감지하여 해당 예외에 맞는 HTTP 상태 코드(e.g., 409 Conflict)와 에러 메시지를 클라이언트에게 응답합니다.

-------------------
★일반 로그인 (POST /api/auth/login)
사용자의 이메일과 비밀번호를 검증하고, 성공 시 Access Token과 Refresh Token을 발급처리

AuthController
1 login(@RequestBody AuthDto.LoginRequest request) 메소드요청
2 authService.login(request)를 호출.

AuthService
3 login(AuthDto.LoginRequest request) 메소드가 @Transactional과 함께 실행
4 userRepository.findByEmail(request.getEmail())을 호출하여 해당 이메일을 가진 사용자가 있는지 확인하고. 없으면 CustomException(ErrorCode.LOGIN_FAIL)을 발생시킴
5 사용자가 있다면, passwordEncoder.matches(request.getPassword(), user.getPassword())를 호출하여 클라이언트가 보낸 평문 비밀번호와 DB에 저장된 암호화된 비밀번호 비교하고. 일치하지 않으면 CustomException(ErrorCode.LOGIN_FAIL)을 발생시킴.
6 모두 통과되면 토큰 발급을 위해  자기 자신의 issueTokens(user) 메소드를 호출
  
  AuthService의 issueTokens(user) 메소드
7 jwtUtil.generateAccessToken(user.getId(), user.getRole())을 호출하여 Access Token을 생성
8 jwtUtil.generateRefreshToken(user.getId())를 호출하여 Refresh Token을 생성
9 user.updateRefreshToken(refreshToken)을 호출하여 방금 생성한 Refresh Token을 User 엔티티에 저장. @Transactional 덕분에 메소드가 끝나면 이 변경사항이 DB에 자동으로 반영
10 생성된 두 토큰을 AuthDto.TokenResponse 객체에 담아 반환
11 AuthService의 login 메소드가 TokenResponse를 AuthController로 반환합니다.

----------------------------
★JWT 인증이 필요한 API 호출 (GET /api/users/me)
클라이언트가 Access Token을 사용하여 자신의 정보를 요청하고, 서버는 토큰을 검증하여 인증된 사용자 정보를 반환

로그인 시 발급받은 Access Token을 HTTP 헤더에 담아 GET /api/users/me API에 요청.
Authorization: Bearer eyJhbGciOiJIUzI1Ni...

JwtAuthenticationFilter (가장 먼저 동작)
1 doFilterInternal() 메소드가 모든 요청을 가로챕니다.
2 jwtUtil.resolveToken(request)를 호출하여 Authorization 헤더에서 Bearer 접두어를 제거하고 순수 토큰 문자열을 추출
3 jwtUtil.validateToken(token)을 호출하여 토큰의 유효성(서명, 만료 시간 등)을 검증
4 토큰이 유효하면, jwtUtil.getUserIdFromToken(token)을 호출하여 토큰에서 사용자 ID를 추출
5 customUserDetailsService.loadUserByUsername(userId)를 호출하여 DB에서 실제 사용자 정보를 조회

CustomUserDetailsService
6 loadUserByUsername(String userId) 메소드 실행
7 userRepository.findById(Long.parseLong(userId))를 통해 ID로 사용자를 조회합니다.
8 조회된 User 엔티티를 new CustomUserDetails(user)로 감싸 Spring Security가 이해할 수 있는 UserDetails 형태로 만들어 반환

JwtAuthenticationFilter
9 CustomUserDetails 객체를 받습니다.
10 new UsernamePasswordAuthenticationToken(...)을 사용하여 인증(Authentication) 객체를 생성합니다. 이 객체는 "인증된 사용자"라는 증표와 같습니다.
11 SecurityContextHolder.getContext().setAuthentication(authentication)를 통해 현재 요청의 보안 컨텍스트에 이 인증 객체를 저장합니다. 이 과정이 바로 "인증 처리"의 핵심입니다.
12 filterChain.doFilter(request, response)를 호출하여 다음 필터 또는 컨트롤러로 요청을 전달합니다.

UserController
13 getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) 메소드가 호출됩니다.
14 @AuthenticationPrincipal 어노테이션 덕분에 Spring Security가 SecurityContextHolder에 저장해 둔 CustomUserDetails 객체를 파라미터로 주입해줍니다.
15 AuthDto.UserInfoResponse.fromEntity(userDetails.getUser())를 통해 응답용 DTO를 생성합니다.
-----------------------------------------
★구글 소셜 로그인
사용자가 구글 계정으로 로그인하고, 우리 서비스에 계정이 없으면 자동 생성 후 로그인 처리, 있으면 바로 로그인 처리하여 토큰을 발급합니다.
'구글 로그인' 버튼을 클릭하면, 프론트엔드에서 http://localhost:8088/oauth2/authorization/google 주소로 이동

Spring Security
1 이 요청을 감지하고, application.properties의 설정에 따라 사용자를 구글의 인증 페이지로 리다이렉트시킵니다.
구글 (Google)

2 사용자가 구글 계정으로 로그인하고 정보 제공에 동의합니다.
3 구글은 우리 서버의 redirect-uri로 사용자를 다시 리다이렉트시키면서 **인증 코드(Authorization Code)**를 함께 보내줍니다.

Spring Security
4 리다이렉트된 요청을 받아 인증 코드를 구글에 다시 보내 Access Token과 사용자 정보를 받아옵니다.
5 받아온 사용자 정보를 가지고 CustomOAuth2UserService를 호출합니다.

CustomOAuth2UserService
6 loadUser(OAuth2UserRequest userRequest) 메소드가 @Transactional과 함께 실행됩니다.
7 구글로부터 받은 사용자 정보를 GoogleUserInfo 객체로 파싱하여 표준화합니다.
8 userRepository.findByEmail(email)로 DB에 이미 가입된 사용자인지 확인합니다.
 사용자가 있는 경우: .map(...) 블록이 실행됩니다. 기존 User 엔티티의 닉네임 등 정보를 최신으로 업데이트하고 그 User를 반환합니다.
 사용자가 없는 경우: .orElseGet(...) 블록이 실행됩니다. User.builder()로 새 User를 생성하고 userRepository.save()로 DB에 저장한 뒤 그 User를 반환합니다.
9 최종적으로 얻은 User 엔티티를 new CustomUserDetails(user, attributes)로 감싸 반환합니다.

OAuth2LoginSuccessHandler
10 CustomOAuth2UserService가 성공적으로 CustomUserDetails를 반환하면, Spring Security는 이어서 onAuthenticationSuccess(...) 메소드를 호출합니다.
11 authentication.getPrincipal()을 통해 방금 생성된 CustomUserDetails를 가져옵니다.
12 토큰 발급 로직을 직접 수행하지 않고, authService.issueTokens(oAuth2User.getUser())를 호출하여 AuthService에 토큰 발급을 위임합니다.

AuthService의 issueTokens(user) 메소드
13 (위의 '일반 로그인' 4번과 동일) Access/Refresh Token을 생성하고, Refresh Token을 DB에 업데이트한 후, TokenResponse를 반환합니다.

OAuth2LoginSuccessHandler
14 AuthService로부터 TokenResponse를 받습니다.
15 UriComponentsBuilder를 사용하여 프론트엔드 주소(http://localhost:3000/auth/callback)에 Access Token과 Refresh Token을 쿼리 파라미터로 붙인 최종 URL을 만듭니다.
16 getRedirectStrategy().sendRedirect(...)를 통해 사용자의 브라우저를 이 최종 URL로 리다이렉트시킵니다.
 클라이언트 (Frontend)
17 리다이렉트된 페이지(auth/callback)에서 URL의 쿼리 파라미터로부터 토큰들을 추출하여 저장하고, 사용자에게 로그인 완료 화면을 보여줍니다.

★토큰 재발급 (POST /api/auth/reissue)
만료된 Access Token 대신, 유효한 Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.

보관하고 있던 Refresh Token을 JSON에 담아 POST /api/auth/reissue API에 요청합니다.
AuthController
1 reissue(@RequestBody AuthDto.ReissueRequest request) 메소드가 요청을 받습니다.
2 authService.reissueToken(request)을 호출합니다.
3 AuthService

4 reissueToken(AuthDto.ReissueRequest request) 메소드가 @Transactional과 함께 실행됩니다.
5 jwtUtil.validateToken(refreshToken)으로 받은 Refresh Token이 유효한지 검증합니다. 유효하지 않으면 CustomException 발생.
6 userRepository.findByRefreshToken(refreshToken)으로 DB에서 해당 Refresh Token을 가진 사용자를 찾습니다. 없으면 CustomException 발생 (탈취되었거나 이미 로그아웃 처리된 토큰일 수 있음).
7 사용자를 찾으면, 다시 issueTokens(user) 메소드를 호출합니다.
8 AuthService의 issueTokens(user) 메소드

9 (위와 동일) 새로운 Access Token과 새로운 Refresh Token을 모두 발급합니다. (보안을 위해 Refresh Token도 재발급하는 것이 좋음 - Refresh Token Rotation)
10 새로 발급된 Refresh Token을 DB에 업데이트합니다.
11 두 개의 새 토큰을 TokenResponse에 담아 반환합니다.

12 TokenResponse가 AuthController로 반환되고, 클라이언트에게 새로운 토큰들이 응답으로 전달됩니다.
13 클라이언트는 기존의 Access Token과 Refresh Token을 모두 새로 받은 토큰으로 교체해야 합니다.