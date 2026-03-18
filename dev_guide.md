# 개발 가이드

## 프로젝트 목표

- BPM 기반 신청/접수/검토/승인 흐름 구현
- 백엔드 스키마 기반 프론트 자동 화면 생성
- MyBatis, MySQL, Spring Boot, BPM 연동 정리

## 기술 스택

### Backend

- Spring Boot `3.3.5`
- Java `17`
- MyBatis `3.0.5`
- MySQL
- Spring Validation
- Springdoc OpenAPI
- 세션 인증(`JSESSIONID`)
- Camunda Orchestration REST 연동

### Frontend

- Nuxt 3
- Vuetify
- Tailwind
- Pinia
- Axios
- Vee-Validate
- CKEditor

## 디렉터리

- 백엔드: `backend/`
- 프론트: `front/`
- 백엔드 포트: `9090`

## 로컬 실행

### 1) MySQL 실행

```bash
docker compose -f backend/docker/mysql/docker-compose.yml up -d
```

### 2) 백엔드 실행

```bash
cd backend
./gradlew bootRun
```

### 3) 프론트 실행

```bash
cd front
npm install
npm run dev
```

## 데이터베이스

- 스키마: `backend/src/main/resources/db/schema.sql`
- 시드: `backend/src/main/resources/data.sql`

## 인증/인가

- 세션(`JSESSIONID`) 기반 로그인 상태 확인
- `AuthorizationInterceptor`에서 세션 사용자 검증
- 요청 범위 속성으로 사용자 컨텍스트 전달

## 등록 스포츠클럽 신청 API

- 저장: `POST /reg-sports-club-applications/save`
- 신청: `POST /reg-sports-club-applications/apply`
- 액션: `POST /reg-sports-club-applications/{applyId}/actions`

## Camunda 설정

- `camunda.orchestration.base-url=http://localhost:8080`
- `camunda.orchestration.process-definition-id=registration_sports_club_process`

## 인코딩 규칙

- 문서/소스는 UTF-8로 저장
- 깨짐 발생 시 파일 인코딩과 터미널 인코딩 동시 점검
