# Form Builder Registered Sports Club BDD

## 1. Purpose
- This document defines behavior for the registered sports club application form as a schema-driven experience.

## 2. Scenario: User enters Step 2 form data
- Given the user is on Step 2
- When the user types into form fields
- Then the page stores those values in form state
- And the values are available for save or submit
- Current status: implemented

## 3. Scenario: User loads operating sport options
- Given the user opens Step 2
- When the page mounts
- Then the page fetches the operating sport code tree
- And renders parent options
- Current status: implemented

## 4. Scenario: User selects a parent operating sport
- Given parent options are loaded
- When the user selects one parent code
- Then the child list is recalculated from the linked child group
- Current status: implemented

## 5. Scenario: User changes the selected parent sport
- Given both parent and child values were selected
- When the parent value changes
- Then the child value is reset to null
- Current status: implemented

## 6. Scenario: User saves a draft from Step 2
- Given the user has entered current form values
- When the user clicks save
- Then the form values are mapped to an upsert request
- And `/reg-sports-club-applications/save` is called
- And `applyId` is stored when returned
- Current status: implemented

## 7. Scenario: User submits from Step 2
- Given the user has entered current form values
- When the user clicks submit
- Then the form values are mapped to an upsert request
- And `/reg-sports-club-applications/apply` is called
- And completion data is stored in the page store
- And the page moves to Step 3
- Current status: implemented

## 8. Scenario: User sees completion summary
- Given the submit request has succeeded
- When Step 3 is shown
- Then the page displays:
  - success banner
  - applied timestamp
  - application id if available
  - summary values captured from form state
- Current status: implemented

## 9. Scenario: User reopens a saved draft by `applyId`
- Given the URL contains `applyId`
- When the page is opened
- Then the system should restore the saved server data into the form
- Current status:
  - only `applyId` capture is implemented
  - full restore is not implemented

## 10. Scenario: Required validation blocks invalid submission
- Given required fields are missing
- When the user clicks submit
- Then the page should show validation errors and stop submit
- Current status:
  - target behavior
  - frontend currently lacks full enforced validation on this page

## 11. Scenario: Schema-driven replacement of the current form
- Given a future Form.io schema is introduced
- When the registered sports club form page loads
- Then the same business flow should work without page-local field hardcoding
- And save/submit should still call the same backend contract
- Current status: target behavior only
