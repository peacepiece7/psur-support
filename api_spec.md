# API 명세

## 기본 정보

- Base URL: `http://localhost:9090`
- Swagger UI: `http://localhost:9090/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:9090/v3/api-docs`

## 인증

- 방식: 세션 인증(`JSESSIONID`)
- 비인증 예외 경로:
  - `/auth/login`
  - `/auth/register`
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
  - `/error`
  - `/actuator/**`

## 주요 엔드포인트

### Auth

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/logout`
- `POST /auth/password/reset`

### Users

- `GET /users`
- `GET /users/me`
- `GET /users/me/detail`
- `PUT /users/me`

### Roles / User Roles

- `GET /roles`
- `GET /user-roles`
- `POST /user-roles`
- `PUT /user-roles`
- `PUT /user-roles/bulk`
- `DELETE /user-roles`

### Common Codes

- `GET /common-codes/{groupCode}/tree`
- `GET /common-codes/groups`
- `GET /common-codes/groups/root`
- `POST /common-codes`
- `PUT /common-codes`
- `DELETE /common-codes/{groupCode}/codes/{code}`

### Sports Clubs

- `POST /sports-clubs`
- `GET /sports-clubs/{id}`
- `GET /sports-clubs`
- `PUT /sports-clubs/{id}`
- `DELETE /sports-clubs/{id}`

### Registered Sports Club Applications

- `POST /reg-sports-club-applications/save`
- `POST /reg-sports-club-applications/apply`
- `POST /reg-sports-club-applications/{applyId}/actions`
- `GET /reg-sports-club-applications/{applyId}`
- `GET /reg-sports-club-applications`

## 운영 종목 연동

- 일반 스포츠클럽: `categoryIds` -> `sports_club_category`
- 등록 신청: `operatingSportCodeIds` -> `reg_sports_club_application_category`
- `operatingSportCodeIds`가 없으면 `operatingSportParentCodeId`, `operatingSportChildCodeId` 사용

## Camunda 연동 메모

- Base URL: `http://localhost:8080`
- Process Definition ID: `registration_sports_club_process`
- 시작: `POST /v2/process-instances`
- 태스크 조회: `POST /v2/user-tasks/search`
- 태스크 완료: `POST /v2/user-tasks/{userTaskKey}/completion`
