## 스프링 시큐리티와 OAuth 2.0으로 로그인 기능 구현하기
- 스프링 시큐리티는 막강한 인증과 인가 기능을 가진 프레임워크다.
- 보안 기능은 인터셉터, 필터 기반보다 스프링 시큐리티를 통해 구현하는 것을 적극적으로 권장하고 있다.
- 스프링 시큐리티와 OAuth 2.0을 기반으로 한 구글 로그인 기능 구현해보자.

### 스프링 시큐리티와 스프링 시큐리티 Oauth2 클라이언트
- 많은 서비스들이 로그인 기능을 직접 구현하기보단, 소셜 로그인 기능을 연동하고 있다. 왜냐하면 직접 구현 시 배보다 배꼽이 더 클 수 있다.
- 직접 구현 시 구현 리스트
  1. 로그인 시 보안
  2. 회원가입 시 이메일 혹은 전화번호 인증
  3. 비밀번호 찾기
  4. 비밀번호 변경
  5. 회원 정보 변경
  
- 스프링 부트 1.5 vs 스프링 부트 2.5
  - OAuth2 연동 방법이 2.0에서 크게 변경되었다. 그런데 설정 방법은 크게 차이가 없다. 그 이유는 spring-security-oauth2-autoconfigure 라이브러리 덕분이다.
    - 그러나 1.5 방식을 2.0에서 그대로 유지시켜주는 것 뿐, 2.0을 사용하는 것은 아니다.

- 책에서 2.0 방식을 사용하는 이유
  - 1.5 버전에서 spring-security-oauth 플젝은 유지 상태로 결정했으며 신규 기능은 추가 하지 않고 버그 수정 정도만... 신규 기능은 OAuth2 라이브러리에서만 지원하겠다고 선언
  - 스프링 부트용 라이브러리(starter) 출시
  - 기존 사용 방식은 확장 포인트가 적절히 오픈되지 않아 직접 상속 또는 오버라이딩 해야 하는데, 신규 라이브러리의 경우 이를 고려하여 설계가 됐다.
   
- 스프링 부트 2.x 버전 자료 찾을 시 유의사항
  - spring-security-oauth2-autoconfigure 라이브러리 썼는지 확인
  - application.properties 또는 application.yml 정보 차이 비교
    - 1.5 버전은 url 주소 모두 명시
    - 2.0 버전은 client 정보만 입력, 1.5 직접 입력 값이 enum으로 대체 되었다.
      - enum: CommonOAuth2Provider에서 구글, 깃허브, 페이스북, 옥타의 기본 설정 값 모두 여기서 제공, 이 외에 다른 소셜(네이버, 카카오 등)을 추가한다면 직접 다 추가 해야 한다.
      
- 구굴 로그인 기능
  - 구글 서비스 등록
     - 구글 서비스에 신규 서비스를 생성하여 발급된 인증 정보를 통해 로그인 기능과 소셜 서비스 기능 사용
       - https://console.cloud.google.com 이동 후 프로젝트 선택 클릭
       - 새 프로젝트 클릭 후 원하는 프로젝트 이름으로 만들기
       - 생성 후 프로젝트 선택 후 왼쪽 메뉴 탭에서 API 및 서비스 카테고리 이동
       - 사용자 인증 정보 클릭 후 사용자 인증 정보 만들기 클릭 후 OAuth 클라이언트 ID 클릭
       - 클라이언트 ID 생성 전 동의 화면 구성 설정이 뜨는데, 동의 화면 구성 클릭
       - 다시 클라이언트 ID 생성 화면으로 와서 웹 애플리케이션 클릭 후 '승인된 리디렉션 URI'만 URL 주소 등록 (http://localhost:8080/login/oauth2/code/google)
         - 승인된 리디렉션 URI
           - 서비스에서 파라미터로 인증 정보 전송 시 인증 성공하면 구글에서 redirect할 URL
           - 스프링 부트 2버전 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드}로 리다이렉트 URL을 지원한다.
           - 현재는 개발 단계이므로, http://localhost:8080/login/oauth2/code/google로만 등록
           - AWS 서버에 배포 시 localhost 외에 추가로 주소를 추가 해야 한다. 이건 다음 챕터에서 진행
       - 생성 클릭 후 이름 클릭하여 상세 내용으로 가면 클라이언트 ID와 클라이언트 보안 비밀 코드로 플젝에 설쟁하면 된다.
       - src/main/resources 디렉토리에 application-oauth.properties 파일 생성 후 클라이언트 ID와 클라이언트 보안 비밀 코드를 등록
         - spring.security.oauth2.client.registration.google.client-id=클라이언트 ID
         - spring.security.oauth2.client.registration.google.client-secret=클라이언트 보안 비밀
         - spring.security.oauth2.client.registration.google.scope=profile,email
           - scope=profile,email
             - 많은 예제에서 이 scope를 별도 등록하지 않는다. 기본 값이 openid, profile, email이기 때문
             - 강제로 profile, email 등록 이유는 openid라는 scope가 있으면 Open Id Provider로 인식하기 때문
             - 이렇게 되면 OpenId Provider인 서비스(구글)와 그렇지 않은 서비스 (네이버/카카오 등)로 나눠서 각각 OAuth2Service를 만들어야 한다.
             - 하나의 OAuth2Service로 사용하기 위해 openid scope를 뺴고 등록

  - 꿀팁
    - 스프링 부트에서는 application-xxx.properties로 파일 생성 시 xxx라는 이름의 profile이 생싱되어 이를 통해 관리 가능
      - 즉, profile=xxx 라는 식으로 호출 시 해당 properties 설정들을 가져올 수 있다.
      - spring.profiles.include=xxx를 application.properties에 등록 시 xxx 설정을 가져올 수 있다.             
       