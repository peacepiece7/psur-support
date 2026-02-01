# Camunda 8 API Notes (Local Swagger + Official Docs)

## Local Swagger (localhost:8080)
- Operate v1: `/v3/api-docs/Operate-v1`
- Tasklist v1: `/v3/api-docs/Tasklist-v1`
- Orchestration Cluster API v2: `/v3/api-docs/Orchestration%20Cluster%20API`

## Current Integration (Registration Sports Club)
Base URL (local): `http://localhost:8080`

### Start process instance
- Endpoint: `POST /v2/process-instances`
- Request (by BPMN process ID):
  - `processDefinitionId`: `registration_sports_club_process`
  - `variables`: `{}` (root scope variables)
- Response fields used:
  - `processInstanceKey` -> persisted as `reg_sports_club_apply.process_task_id`

### Find active user task for a process instance
- Endpoint: `POST /v2/user-tasks/search`
- Request filter:
  - `processInstanceKey`: `{processTaskId}`
  - `state`: `CREATED`
- Response fields used:
  - `items[].userTaskKey`

### Complete user task with action
- Endpoint: `POST /v2/user-tasks/{userTaskKey}/completion`
- Request body:
  - `variables`: `{ "action": "apply" }`

## Official Camunda 8 Docs (references)
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
