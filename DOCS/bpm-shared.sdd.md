# BPM Shared SDD

## 1. Purpose
- This document defines the shared BPM integration contract used by the application.
- It covers process instance lifecycle, task completion, workflow variables, and action auditing.

## 2. BPM Runtime
- Engine target: Camunda Orchestration REST
- Base URL: `camunda.orchestration.base-url`
- Process definition id: `camunda.orchestration.process-definition-id`

## 3. Backend Services
- `BpmService`
  - talks to external Camunda endpoints
- `ActionService`
  - ensures process start and completes workflow actions
- `RegSportsClubApplicationService`
  - resolves user roles and writes action logs

## 4. External Camunda API Usage
- Start process
  - `POST /v2/process-instances`
- Search active tasks
  - `POST /v2/user-tasks/search`
- Complete user task
  - `POST /v2/user-tasks/{userTaskKey}/completion`

## 5. Process Start Contract
- If a domain object has no `processInstanceId`, `ActionService.ensureProcessStarted(...)` starts one.
- The new process instance id is written back to DB.
- Current implementation note:
  - `save` path also calls `ensureProcessStarted`
  - therefore draft save can start a BPM instance before submit

## 6. Task Completion Contract
- Active task search uses:
  - `processInstanceKey`
  - `state = CREATED`
- Search retries:
  - max retries: `10`
  - delay: `200ms`
- On success:
  - first returned task is used
  - completion request sends variables
- On failure:
  - throws `ApiException("Active user task not found")`

## 7. Variable Contract
- Minimal variable:
  - `action`
- Additional variables currently supported:
  - `roles`
  - `payload`

## 8. Audit Log Contract
- After generic `handleAction(applyId, actionKey, payload)`:
  - insert into `application_action_log`
- Stored fields:
  - `applyId`
  - `processCode`
  - `actionKey`
  - `taskKey`
  - `payloadJson`
  - `actorId`
  - `actorRole`
  - `executedAt`

## 9. Shared BPM Risks
- role list is passed to BPM as variables but not yet enforced as a backend authorization gate
- app status update rules are not coupled explicitly to BPM task transitions
- workflow state and UI step state are not synchronized
- process start policy for drafts is ambiguous

## 10. Shared BPM Design Debt
- no stable task-to-role matrix in code
- no stale/orphan process reconciliation job
- no consistent recovery flow when Camunda is unavailable
