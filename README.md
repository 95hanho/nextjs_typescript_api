# nextjs-shop Backend

Spring Boot 기반 커머스 포트폴리오 프로젝트의 백엔드 API 서버입니다.  
회원 인증, 상품 조회, 장바구니, 주문/결제, 쿠폰, 리뷰, 판매자/관리자 기능을 제공합니다.

## 프로젝트 개요

- 프로젝트명: nextjs-shop Backend
- 개발 기간: 2025.03.04 ~ 2026.05.07
- 개발 인원: 개인 프로젝트
- 역할: Backend API 설계 및 구현
- Frontend Repository: https://github.com/95hanho/nextjs-shop
- 배포 방식: WAR 빌드 후 Cafe24 Tomcat 서버 배포

## 기술 스택

- Java
- Spring Boot
- Gradle
- MyBatis
- MariaDB
- JWT
- Lombok
- Spring MVC
- Spring Boot Validation
- Spring Boot DevTools
- Cafe24 Tomcat

## 테스트 안내

배포된 프론트엔드 서비스에서 아래 계정으로 주요 기능을 확인할 수 있습니다.

- Frontend URL: https://nextjs-shop-henna.vercel.app/
- 일반 사용자: `test / aaaaaa1!`
- 판매자: `seller11 / a123159!!`
- 테스트 추천 경로: `상의 > 반소매 티셔츠`

해당 카테고리에 상품 목록, 정렬, 무한스크롤, 상품 상세, 장바구니 테스트용 데이터가 가장 많이 등록되어 있습니다.

## 주요 기능

### 사용자

- 회원가입 / 로그인 / 로그아웃
- Access Token / Refresh Token 기반 인증
- 휴대폰 인증
- 아이디 찾기 / 비밀번호 변경
- 상품 목록 및 상세 조회
- 장바구니
- 바로구매 / 장바구니 구매
- 쿠폰 적용
- 주문 내역 조회
- 리뷰 작성 / 수정 / 삭제
- 상품 Q&A

### 판매자

- 판매자 로그인
- 상품 등록 / 수정 / 조회
- 상품 옵션 관리
- 주문 관리
- 쿠폰 관리
- Q&A 답변 관리

### 관리자

- 판매자 관리
- 관리자 인증
- 서비스 운영 관련 데이터 관리

## 주요 구현 포인트

프로젝트의 주요 설계 및 구현 내용은 [주요 구현 포인트 문서](./docs/IMPLEMENTATION.md)에 정리했습니다.

- JWT 기반 인증 및 사용자 식별 구조
- BFF 연동을 고려한 인증 역할 분리
- Interceptor 기반 인증 처리
- 공통 예외 처리 구조
- 상품 목록 Cursor Pagination
- 구매 페이지 재고 선점 구조
- 주문/결제 트랜잭션 처리
- 쿠폰 적용 및 발급 구조

## 주요 트러블슈팅

프로젝트를 진행하며 발생한 주요 문제와 해결 과정은 [트러블슈팅 문서](./docs/TROUBLESHOOTING.md)에 정리했습니다.

- 로그인 ID 기반 식별 구조 개선
- 구매 진행 중 재고 초과 주문 가능성 개선
- 프론트엔드와 백엔드의 에러 처리 방식 통일

## 문서

- [주요 구현 포인트](./docs/IMPLEMENTATION.md)
- [트러블슈팅](./docs/TROUBLESHOOTING.md)

## 환경 설정

실행 환경에 따라 `application.yml`의 active profile을 변경합니다.

### Local

```yaml
spring:
  profiles:
    active: local
```

### Production

```yaml
spring:
  profiles:
    active: prod
```

로컬 개발 시에는 `local`, WAR 빌드 후 Cafe24 Tomcat 서버에 배포할 때는 `prod` profile을 사용합니다.

## 실행 방법

Spring Boot DevTools를 정상적으로 사용하려면 두 개의 터미널을 실행합니다.

### 터미널 1

```bash
.\gradlew.bat -t classes
```

### 터미널 2


```bash
.\gradlew.bat bootRun
```

## Clean

```bash
./gradlew clean
```

## 배포 방법

WAR 파일을 생성합니다.

```bash
.\gradlew.bat clean bootWar
```

빌드 결과물은 아래 경로에 생성됩니다.

```bash
build/libs/ROOT.war
```

생성된 ROOT.war 파일을 Cafe24 Tomcat 서버에 배포합니다.