# TODO List

## Process Constraints

### One-Process-Per-User
- 사용자(또는 businessNo/CI) 기준 단일 활성 프로세스만 허용
- 활성 프로세스가 이미 있으면 기존 applyId/processTaskId 재사용
- 승인/반려 등 완료 시점 정리 규칙 정의

### Stale Process Cleanup
- N일 이상 비활성 프로세스 탐지 및 종료
- DB 내 orphan processTaskId 제거/마킹
- 스케줄러 + 알림(실패 감지) 고려

## 데이터 모델링 (ERD)

### 사용자 테이블

- 컬럼 예시: `(id, username, email, telno, password, createdAt, updatedAt, deletedAt, ci)`
- 프로젝트 성격상 로그인/가입 기본 테이블로 구성

### 공통 코드 테이블

- 컬럼 예시:
  - `code`, `codeName`, `groupName`, `groupCode`, `hrnkGroupCode`, `description`, `createdAt`, `updatedAt`, `deletedAt`
- 그룹 코드의 계층 구조를 지원
- 최대 깊이는 3 단계까지 지원하는 방향

### 사용자 권한 테이블

- 권한 종류:
  - `비회원`, `일반 회원`, `행정 관리자`, `대회 관리자`, `지역장`
- 접근 범위 예시:
  - 행정 관리자 -> 상세/행정 사이트 접근 가능
  - 대회 관리자 -> 상세/대회 관리자 사이트 접근 가능
  - 지역장 -> 상세/행정 사이트 접근 가능
  - 일반 회원 -> 상세 접근 가능
  - 비회원 -> 상세 접근 가능 (비회원 상세 화면 별도)

### 스포츠클럽 권한 테이블

- 일반 스포츠클럽 권한
- 지정스포츠클럽 권한
- 등록 스포츠클럽 권한
- 우수지정스포츠클럽 권한

### 운영 종목 계층

- 예시:
  - 육상 - 달리기 - 100m 달리기
  - 육상 - 달리기 - 1000m 달리기
  - 축구 - 풋살
  - 구기 - 축구
- 계층형 구조로 모델링 필요

### 스포츠클럽 정보 테이블

- 예시 컬럼:
  - 스포츠클럽명, 위치, 소유자, 고유번호, 소유자 연락처, 스포츠클럽 권한
- 고유번호는 사업자등록번호/고유번호/스포츠클럽 번호 중 하나
- 권한은 공통 코드에 정의된 권한과 연계

## 기능 구현

### MyBatis CRUD

- MyBatis 기반 CRUD 기능 작성

### Swagger 설정

- Swagger UI 및 OpenAPI 문서 설정
- DTO 기반으로 스키마 타입 정리

### Logging

- 로그인/로그아웃, 비밀번호 찾기, 회원 정보 수정 등 기록
- 화면은 프론트에서 구성, 서버는 API 제공

### 공통 코드 조회 API

- 그룹 코드 기반 트리 조회 API 구현
- 계층 구조(depth 3까지) 지원

## Camunda BPM 연동

- Camunda BPM 프로세스 정의 및 연동 작업
- “등록 스포츠클럽 신청” 프로세스:
  - 일반 사용자 신청
  - 행정 관리자 접수/승인 또는 반려
  - Camunda BPMN과 연동하여 처리
