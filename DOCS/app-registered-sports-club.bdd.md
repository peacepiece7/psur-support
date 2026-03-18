# App Registered Sports Club BDD

## 1. Purpose
- This document defines the user-facing behavior for the registered sports club application flow.

## 2. Actors
- applicant
- workflow operator
- reviewer
- approver
- unauthorized user

## 3. Scenario: Applicant opens the application page
- Given the applicant navigates to `/registered-sports-club/apply`
- When the page is rendered
- Then the page shows a 3-step flow
- And Step 1 displays guidance content
- Current status: implemented

## 4. Scenario: Applicant moves from Step 1 to Step 2
- Given the applicant is on Step 1
- When the applicant clicks the proceed button
- Then the page moves to Step 2
- Current status: implemented

## 5. Scenario: Applicant fills application fields
- Given the applicant is on Step 2
- When the applicant enters application data
- Then form state is kept in `vee-validate`
- Current status: implemented

## 6. Scenario: Applicant loads operating sport options
- Given the applicant is on Step 2
- When the component mounts
- Then the page requests `GET /common-codes/OPERATING_SPORT/tree`
- And parent options are built from root codes
- Current status: implemented

## 7. Scenario: Applicant changes operating sport parent
- Given a parent operating sport has already been selected
- When the applicant changes the parent selection
- Then child selection is cleared
- Current status: implemented

## 8. Scenario: Applicant saves a draft
- Given the applicant has entered Step 2 data
- When the applicant clicks the save button
- Then frontend calls `POST /reg-sports-club-applications/save`
- And `applyId` is stored when returned
- And the user sees a success alert
- Current status: implemented

## 9. Scenario: Applicant submits the application
- Given the applicant has entered Step 2 data
- When the applicant clicks the submit button
- Then frontend calls `POST /reg-sports-club-applications/apply`
- And frontend stores `applicationResponse`
- And frontend moves to Step 3
- Current status: implemented

## 10. Scenario: Applicant sees completion screen
- Given submit completed successfully
- When the page enters Step 3
- Then the page shows success message, applied timestamp, and application number
- Current status: implemented
- Caveat:
  - progress display is static, not live BPM-driven

## 11. Scenario: Applicant re-enters with `applyId`
- Given the route contains `applyId`
- When the page mounts
- Then the store captures the `applyId`
- Current status: partially implemented
- Gap:
  - full backend restore is not completed

## 12. Scenario: Applicant restores an existing draft
- Given an existing application draft exists on the server
- When the applicant reopens the application by `applyId`
- Then the form should be populated from backend detail data
- Current status: not implemented end-to-end

## 13. Scenario: Unauthorized user performs follow-up action
- Given the user lacks the required role
- When the user attempts an action such as approve or reject
- Then the system should block the operation
- Current status: not implemented as a full business rule

## 14. Scenario: Session expires before save or submit
- Given client-side form state still exists
- When backend session has expired
- And the applicant clicks save or submit
- Then the backend should reject the request
- Current status: backend likely rejects, frontend UX is not standardized
