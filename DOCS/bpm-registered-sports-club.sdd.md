# BPM Registered Sports Club SDD

## 1. Purpose
- This document defines the BPM contract specific to registered sports club applications.

## 2. Workflow Identity
- Process definition id: `registration_sports_club_process`
- Domain root entity: `reg_sports_club_apply`
- Workflow linkage column: `process_instance_id`

## 3. Domain Actions
- `save`
- `apply`
- `receipt`
- `review`
- `approve`
- `reject`

## 4. Domain Statuses
- `SAVED`
- `APPLY`
- `RECEIVED`
- `RECEIVED_REJECTED`
- `REVIEW`
- `REVIEW_REJECTED`
- `APPROVED`
- `APPROVED_REJECTED`

## 5. Current Action Execution Shape
- `save`
  - application is upserted
  - process may start
  - no BPM task completion after start because `SAVE` returns early
- `apply`
  - application is upserted with apply status
  - process is ensured
  - BPM active task is completed with action `apply`
- generic `/{applyId}/actions`
  - process is ensured
  - arbitrary action string is accepted
  - user roles and payload are sent as variables
  - audit log is inserted

## 6. Expected Business Transition Matrix
- `SAVED -> APPLY`
- `APPLY -> RECEIVED`
- `RECEIVED -> REVIEW`
- `REVIEW -> APPROVED`
- reject branches:
  - `RECEIVED_REJECTED`
  - `REVIEW_REJECTED`
  - `APPROVED_REJECTED`

## 7. Current Implementation Gap on Status Sync
- Action enum and status enum both exist.
- The code does not currently show a single authoritative mapping layer between:
  - current BPM task
  - triggered action
  - resulting application status

## 8. Target Role Matrix
- Applicant
  - `save`
  - `apply`
- Receipt operator
  - `receipt`
  - `reject`
- Reviewer
  - `review`
  - `reject`
- Approver
  - `approve`
  - `reject`
- Current state:
  - role list is gathered from `user_role`
  - role codes are passed to BPM variables
  - enforcement is not yet coded as a backend permission gate

## 9. Approval Side Effect
- Approval should eventually create a sports club entity and categories
- Existing code path for this exists as `approveToSportsClub(...)`
- It is not yet clearly connected to action execution

## 10. API Surface
- generic action endpoint exists
- dedicated endpoints for:
  - receipt
  - reject
  - review
  - approve
  are explicitly marked TODO in controller comments
