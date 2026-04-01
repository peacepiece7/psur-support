# Frontend Guide

## Main Paths
- `front/app/pages/registered-sports-club/apply.vue`
- `front/app/pages/registered-sports-club/list.vue`
- `front/app/pages/registered-sports-club/detail-view.vue`
- `front/app/stores/newRegSprtClubStore.ts`

## Current Flow
- `apply.vue`는 3단계 stepper UI를 가진다.
- 저장은 `POST /reg-sports-club-applications/save`
- 신청은 `POST /reg-sports-club-applications/apply`
- 목록은 `GET /reg-sports-club-applications`
- 상세는 `GET /reg-sports-club-applications/{applyId}`
- 상세 화면에서 role과 상태 코드 기준으로 `receipt`, `review`, `approve`, `reject` 액션 UI를 노출한다.
- `reject` 버튼 권한은 BPM 조건과 동일하게 `RECEIPT_MANAGER`, `REVIEWER`, `APPROVER`, `ADMIN_SYSTEM_MANAGER`를 따른다.

## Current Constraints
- form validation은 아직 약하다.
- store가 UI orchestration과 request shaping을 함께 가진다.
- alert 기반 에러 처리가 많다.
- 화면용 `NewRegSprtClubReq/Res` 모델과 실제 백엔드 upsert DTO는 서로 다르다.
- 현재 저장/신청 요청은 `operatingSportCodeIds`를 보내지 않고 legacy parent/child id 위주로 보낸다.

## Editing Rule
- 새 프론트 작업은 페이지, store, API contract 중 어디를 바꾸는지 먼저 적는다.
- 상태 코드는 화면 문자열이 아니라 서버 계약 기준으로 본다.
