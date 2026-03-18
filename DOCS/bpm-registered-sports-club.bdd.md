# BPM Registered Sports Club BDD

## 1. Purpose
- This document defines registered sports club workflow behavior from a business point of view.

## 2. Scenario: Applicant saves a draft
- Given the applicant has entered valid draft data
- When the applicant saves
- Then the application state becomes draft-like
- And the system persists the draft
- Current status:
  - persistence is implemented
  - BPM process may also start

## 3. Scenario: Applicant submits an application
- Given a draft or new application exists
- When the applicant submits
- Then the application is stored with apply status
- And a process instance exists
- And the workflow advances using action `apply`
- Current status: implemented

## 4. Scenario: Receipt operator receives an application
- Given an application is awaiting receipt
- And the actor has the receipt role
- When the actor performs `receipt`
- Then the workflow should move to the receipt-complete state
- And the application status should reflect receipt
- Current status:
  - generic action endpoint can be used
  - status synchronization and permission enforcement are not finalized

## 5. Scenario: Reviewer reviews an application
- Given an application is in review stage
- And the actor has the review role
- When the actor performs `review`
- Then workflow advances
- Current status: partial by generic action only

## 6. Scenario: Approver approves an application
- Given an application is in approval stage
- And the actor has the approval role
- When the actor performs `approve`
- Then workflow advances to approved
- And a sports club record should be created
- Current status:
  - target behavior is clear
  - side-effect wiring is not yet proven complete

## 7. Scenario: Unauthorized actor performs workflow action
- Given the actor does not have the business role for the current task
- When the actor calls the action endpoint
- Then the system should reject the action
- Current status: not implemented

## 8. Scenario: No active task exists for the requested action
- Given the process instance exists
- But no active task is available
- When the actor attempts the action
- Then the action fails with business error
- Current status: implemented at BPM service level
