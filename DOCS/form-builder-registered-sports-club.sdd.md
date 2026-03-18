# Form Builder Registered Sports Club SDD

## 1. Purpose
- This document defines the current and target form contract for the registered sports club application form.

## 2. Current Page Structure
- Page: `front/app/pages/registered-sports-club/apply.vue`
- Current steps:
  - Step 1: notice / introduction
  - Step 2: form input
  - Step 3: completion summary

## 3. Current Form State Model
- Form state is owned by `vee-validate`
- Page-level initial values:
  - `applicantName`
  - `applicantTelno`
  - `applicantEmail`
  - `name`
  - `location`
  - `representativeName`
  - `representativeTelno`
  - `businessNo`
  - `operatingSportParentCodeId`
  - `operatingSportChildCodeId`
- Store-level state additionally keeps:
  - `applyId`
  - `applicationResponse`
  - step index

## 4. Current Field Inventory
- applicant section
  - applicant name
  - applicant phone
  - applicant email
- club section
  - club name
  - location
  - representative name
  - representative phone
  - business number
- operating sport section
  - parent sport code
  - child sport code

## 5. Current Component Usage
- every input row uses `FormField`
- text fields use `TextField`
- category selectors use `SelectV2`
- completion screen uses `StatusStepper`
- shell flow uses:
  - `RequestStepper`
  - `Window`
  - `RequestActionWidget`

## 6. Current Option Loading Logic
- operating sport options are loaded from:
  - `GET /common-codes/OPERATING_SPORT/tree?depth=3&includeCodes=true`
- parent options come from root `codes`
- child options are derived from root code's linked child group
- parent change resets child selection

## 7. Current Payload Mapping
- Store method: `_formToUpsertRequest(...)`
- Mapping rules:
  - `applicantName` -> `applicantName`
  - `applicantTelno` -> `applicantTelno`
  - `applicantEmail` -> `applicantEmail`
  - `name` -> `clubName`
  - `location` -> `location`
  - `representativeName` -> `representativeName`
  - `representativeTelno` -> `representativeTelno`
  - `businessNo` -> `businessNo`
  - `operatingSportParentCodeId?.id` -> `operatingSportParentCodeId`
  - `operatingSportChildCodeId?.id` -> `operatingSportChildCodeId`
  - `applyId` comes from store if present

## 8. Current Save/Submit Integration
- save button:
  - calls store `saveApplication(...)`
  - backend endpoint `/reg-sports-club-applications/save`
- submit button:
  - calls store `applyApplication(...)`
  - backend endpoint `/reg-sports-club-applications/apply`

## 9. Current Restore Model
- Route query `applyId` is read on mount
- Store captures `applyId`
- Full data fetch/restore is not finished
- Store `fetch.detail()` still returns mock data

## 10. Current Validation State
- Backend has DTO required fields for:
  - applicantName
  - applicantTelno
  - applicantEmail
  - clubName
- Frontend page comment explicitly says current `useForm` is used without full validation enforcement
- Store still contains TODO notes for stronger validation and file-step integration

## 11. Target Form.io Shape
- schema should define:
  - step 1 notice content
  - step 2 input sections and fields
  - step 3 completion display model
- schema field metadata should include:
  - ui label
  - component type
  - dto binding
  - required rule
  - option source
  - visibility dependency
- operating sport fields should be modeled as dependent select fields

## 12. Current Gaps To Carry Forward
- no schema registry
- no renderer abstraction
- no generic field-to-DTO mapper
- no restore-from-server for real drafts
- completion step is static, not schema-driven
