# API Spec

## Swagger / OpenAPI

- Swagger UI: `http://localhost:9090/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:9090/v3/api-docs`

## 주요 엔드포인트 요약

Auth:
- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/logout`
- `POST /auth/password/reset`

User:
- `GET /users`
- `GET /users/me`
- `GET /users/me/detail`
- `PUT /users/me`

Roles:
- `GET /roles`

User Roles:
- `GET /user-roles`
- `POST /user-roles`
- `PUT /user-roles`
- `PUT /user-roles/bulk`
- `DELETE /user-roles`

Common Codes:
- `GET /common-codes/{groupCode}/tree`

Sports Clubs:
- `POST /sports-clubs`
- `GET /sports-clubs/{id}`
- `GET /sports-clubs`
- `PUT /sports-clubs/{id}`
- `DELETE /sports-clubs/{id}`

Registered Sports Club Applications:
- `POST /reg-sports-club-applications`
- `PATCH /reg-sports-club-applications/{applyId}/status`
- `GET /reg-sports-club-applications/{applyId}`
- `GET /reg-sports-club-applications`

Operating Sports (Common Code 연동):
- Sports clubs API는 `categoryIds` (common_code.id 리스트)를 받아 `sports_club_category`에 저장
- 등록 스포츠클럽 신청은 `operatingSportCodeIds` (common_code.id 리스트)를 받아 `reg_sports_club_application_category`에 저장
- `operatingSportCodeIds`가 없으면 `operatingSportParentCodeId`/`operatingSportChildCodeId`로 fallback

## Camunda 8 API Notes (Local Swagger + Official Docs)

### Local Swagger (localhost:8080)

- Operate v1: `/v3/api-docs/Operate-v1`
- Tasklist v1: `/v3/api-docs/Tasklist-v1`
- Orchestration Cluster API v2: `/v3/api-docs/Orchestration%20Cluster%20API`

### 현재 연동 (Registration Sports Club)

Base URL (local): `http://localhost:8080`

#### Start process instance
- Endpoint: `POST /v2/process-instances`
- Request (by BPMN process ID):
  - `processDefinitionId`: `registration_sports_club_process`
  - `variables`: `{}` (root scope variables)
- Response fields used:
  - `processInstanceKey` -> persisted as `reg_sports_club_apply.process_task_id`

#### Find active user task for a process instance
- Endpoint: `POST /v2/user-tasks/search`
- Request filter:
  - `processInstanceKey`: `{processTaskId}`
  - `state`: `CREATED`
- Response fields used:
  - `items[].userTaskKey`

#### Complete user task with action
- Endpoint: `POST /v2/user-tasks/{userTaskKey}/completion`
- Request body:
  - `variables`: `{ "action": "apply" }`

### Official Camunda 8 Docs (references)

- Orchestration Cluster REST API: Create process instance
  - https://docs.camunda.io/docs/apis-tools/orchestration-cluster-api-rest/specifications/create-process-instance/
- Orchestration Cluster REST API: Search/data fetching
  - https://docs.camunda.io/docs/next/apis-tools/orchestration-cluster-api-rest/orchestration-cluster-api-rest-data-fetching/
- Orchestration Cluster REST API: Complete user task
  - https://docs.camunda.io/docs/next/apis-tools/orchestration-cluster-api-rest/specifications/complete-user-task/
- Tasklist REST API overview (context paths, auth notes)
  - https://docs.camunda.io/docs/apis-tools/tasklist-api-rest/tasklist-api-rest-overview/
- Operate API overview (context paths, search/get endpoints)
  - https://docs.camunda.io/docs/apis-tools/operate-api/overview/
- Orchestration Cluster REST API overview (context paths)
  - https://docs.camunda.io/docs/next/apis-tools/orchestration-cluster-api-rest/orchestration-cluster-api-rest-overview/
