# App Registered Sports Club SDD

## 1. Purpose
- This document defines the application contract for registered sports club applications.

## 2. Business Scope
- Save a draft application
- Submit an application
- View one application
- View application list
- Trigger later workflow actions
- Convert approved application into a `sports_club` record

## 3. Core Tables
- `reg_sports_club_apply`
- `reg_sports_club_application`
- `reg_sports_club_application_category`
- `application_action_log`
- `sports_club`
- `sports_club_category`

## 4. Entity Relationships
- one `reg_sports_club_apply` has one active `reg_sports_club_application`
- one `reg_sports_club_application` has many category rows
- one `reg_sports_club_apply` has many action logs
- one `reg_sports_club_application` may reference one approved sports club

## 5. Status Contract
- status code group: `REG_SPORTS_CLUB_APPLY_STATUS`
- values in code:
  - `SAVED`
  - `APPLY`
  - `RECEIVED`
  - `RECEIVED_REJECTED`
  - `REVIEW`
  - `REVIEW_REJECTED`
  - `APPROVED`
  - `APPROVED_REJECTED`

## 6. Action Contract
- `save`
- `apply`
- `receipt`
- `review`
- `approve`
- `reject`

## 7. Request Contract
- DTO: `RegSportsClubApplicationUpsertRequest`
- fields:
  - `applyId`
  - `statusCodeId`
  - `statusCode`
  - `applicantName`
  - `applicantTelno`
  - `applicantEmail`
  - `clubName`
  - `location`
  - `representativeName`
  - `representativeTelno`
  - `businessNo`
  - `clubRoleCodeId`
  - `operatingSportParentCodeId`
  - `operatingSportChildCodeId`
  - `operatingSportCodeIds`

## 8. Response Contract
- DTO: `RegSportsClubApplicationResponse`
- fields:
  - `applyId`
  - `applicationId`
  - `code`
  - `codeName`
  - `appliedAt`
  - applicant fields
  - club fields
  - operating sport fields
  - `operatingSportCodeIds`
  - `approvedClubId`

## 9. API Contract
- `POST /reg-sports-club-applications/save`
- `POST /reg-sports-club-applications/apply`
- `POST /reg-sports-club-applications/{applyId}/actions`
- `GET /reg-sports-club-applications/{applyId}`
- `GET /reg-sports-club-applications`

## 10. Persistence Rules
- create path inserts apply header, detail row, and categories
- update path updates apply header, detail row, and replaces categories
- category fallback:
  - prefer `operatingSportCodeIds`
  - fallback to parent/child ids
- club role fallback:
  - lookup `CLUB_ROLE / REG_CLUB`

## 11. Approval Intent
- Service contains `approveToSportsClub(applyId)` logic.
- Intended side effects:
  - create `sports_club`
  - create `sports_club_category`
  - set `approvedClubId`
- Current risk:
  - method exists but is not visibly wired into action flow

## 12. Current Gaps
- no explicit duplicate prevention by `businessNo`
- no single-application enforcement
- no finalized stale/orphan process cleanup
- no dedicated action endpoints for receipt/review/approve/reject
- no finalized status-to-BPM synchronization contract

## 13. Frontend Mapping
- Current page: `front/app/pages/registered-sports-club/apply.vue`
- Current store: `front/app/stores/newRegSprtClubStore.ts`
- Mapping:
  - `name` -> `clubName`
  - `operatingSportParentCodeId?.id` -> `operatingSportParentCodeId`
  - `operatingSportChildCodeId?.id` -> `operatingSportChildCodeId`
  - `applyId` comes from store state
