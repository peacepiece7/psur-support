# FE / BE / BPM Contract

## Purpose
이 문서는 등록 스포츠클럽 플로우에서 프론트, 백엔드, BPM이 실제로 어떤 계약을 공유하는지 한 곳에 고정한다.

## 1. Runtime Paths
- FE page entry: `front/app/pages/registered-sports-club/apply.vue`
- FE state orchestration: `front/app/stores/newRegSprtClubStore.ts`
- BE controller: `backend/src/main/java/com/service/demo/domain/regsportsclub/controller/RegSportsClubApplicationController.java`
- BE service: `backend/src/main/java/com/service/demo/domain/regsportsclub/service/RegSportsClubApplicationService.java`
- BPM service: `backend/src/main/java/com/service/demo/domain/regsportsclub/service/BpmService.java`
- BPMN: `backend/src/main/resources/bpmn/registration_sports_club_v8.bpmn`

## 2. API Contract
### Save
- endpoint: `POST /reg-sports-club-applications/save`
- request type: `RegSportsClubApplicationUpsertRequest`
- FE caller: `newRegSprtClubStore.actions.saveApplication()`
- behavior:
  - 신청 데이터를 upsert한다.
  - 상태 기본값은 `SAVED`다.
  - process instance가 없으면 시작한다.
  - BPM user task completion은 하지 않는다.

### Apply
- endpoint: `POST /reg-sports-club-applications/apply`
- request type: `RegSportsClubApplicationUpsertRequest`
- FE caller: `newRegSprtClubStore.actions.applyApplication()`
- behavior:
  - 신청 데이터를 upsert한다.
  - 상태 기본값은 `APPLY`다.
  - process instance가 없으면 시작한다.
  - BPM completion variables에 최소 `action=apply`가 들어간다.

### Generic Action
- endpoint: `POST /reg-sports-club-applications/{applyId}/actions`
- request type: `RegSportsClubApplicationActionRequest`
- current FE caller: `front/app/pages/registered-sports-club/detail-view.vue`
- behavior:
  - 현재 사용자 roles를 조회한다.
  - BPM completion variables에 `action`, `roles`, 선택적 `payload`를 넣는다.
  - action log를 남긴다.

### Read APIs
- `GET /reg-sports-club-applications`
- `GET /reg-sports-club-applications/{applyId}`
- response type: `RegSportsClubApplicationResponse`
- current FE callers:
  - list: `front/app/pages/registered-sports-club/list.vue`
  - detail: `front/app/pages/registered-sports-club/detail-view.vue`

## 3. Variable Contract
### BPM Variables
- `action`: 문자열
- `roles`: role code 문자열 배열
- `payload`: 선택적 객체

### Shared Action Values
- `save`
- `apply`
- `receipt`
- `review`
- `approve`
- `reject`

### Shared Role Codes Used By BPMN
- `APPLICANT`
- `RECEIPT_MANAGER`
- `REVIEWER`
- `APPROVER`
- `ADMIN_SYSTEM_MANAGER`

### Operator Action Permission Contract
- `receipt`: `RECEIPT_MANAGER`, `REVIEWER`, `ADMIN_SYSTEM_MANAGER`
- `review`: `REVIEWER`, `ADMIN_SYSTEM_MANAGER`
- `approve`: `APPROVER`, `ADMIN_SYSTEM_MANAGER`
- `reject`: `RECEIPT_MANAGER`, `REVIEWER`, `APPROVER`, `ADMIN_SYSTEM_MANAGER`
- 프론트 버튼 노출은 BPM 조건보다 좁아지지 않게 유지한다.

## 4. Data Mapping Contract
### FE Form -> Upsert DTO
현재 `apply.vue`와 `newRegSprtClubStore`는 아래 값만 실제 save/apply 요청으로 보낸다.
- `applicantName`
- `applicantTelno`
- `applicantEmail`
- `clubName`
- `location`
- `representativeName`
- `representativeTelno`
- `businessNo`
- `operatingSportParentCodeId`
- `operatingSportChildCodeId`
- optional `applyId`

### Important Mismatch
`NewRegSprtClubReq/Res`는 화면용 임시 모델이며, 실제 백엔드 upsert DTO와 1:1 대응하지 않는다.

대표 차이:
- 화면 모델에는 `corpOrgName`, `corpEstablishedAt`, `region`, `facilities`, `sports`, `members`가 있다.
- 실제 save/apply 요청에는 위 값이 직접 들어가지 않는다.
- 현재 store는 화면 모델 전체를 보내지 않고 일부 필드만 골라 `RegSportsClubApplicationUpsertRequest`로 변환한다.

## 5. Category Saving Contract
- 우선 규칙: `operatingSportCodeIds`가 있으면 그 값을 저장한다.
- fallback 규칙: 없으면 `operatingSportParentCodeId`, `operatingSportChildCodeId`를 사용한다.
- 현재 FE save/apply는 `operatingSportCodeIds`를 보내지 않고 fallback 경로를 주로 탄다.
- read 응답에서는 `operatingSportCodeIds`가 category table 기준으로 다시 채워진다.

## 6. BPMN Contract
- process id: `registration_sports_club_process`
- safe assumptions:
  - user task completion은 active task 하나를 찾아 첫 번째 task key로 처리한다.
  - BPMN 조건식은 `action`과 `roles`를 직접 참조한다.
- change warning:
  - action 문자열 변경은 FE, BE enum/constant, BPMN 조건식을 함께 바꿔야 한다.
  - role code 변경은 seed data와 BPMN 조건식을 함께 바꿔야 한다.

## 7. Current Gaps
- 운영자 action UI는 상세 화면에 붙었지만 상태 코드 즉시 반영은 아직 불완전하다.
- save/apply 이후 상태명과 BPMN 실제 위치를 검증하는 자동 테스트가 없다.
- 화면용 도메인 모델과 API DTO가 분리돼 있어 신규 작업자가 혼동하기 쉽다.
