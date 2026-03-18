# Form Builder Shared BDD

## 1. Purpose
- This document defines shared behavior for schema-driven forms and future Form.io-backed rendering.

## 2. Scenario: A schema-backed form is rendered
- Given a valid form schema exists
- When the page loads the schema
- Then the page renders fields in schema order
- Current status: target behavior only

## 3. Scenario: A schema-backed form cannot be rendered
- Given the schema is missing or invalid
- When the page tries to load the form
- Then the user sees a safe fallback or error state
- Current status: target behavior only

## 4. Scenario: A required field is left empty
- Given a field is required by schema
- When the user blurs or submits without a value
- Then the UI shows a validation error
- Current status: partially implemented in current custom forms

## 5. Scenario: A field depends on a parent field
- Given a child field depends on a parent selection
- When the user changes the parent value
- Then the child value is reset
- Current status: implemented in the current registered sports club form

## 6. Scenario: Form values are mapped to API request payload
- Given the user has entered field values
- When the user saves or submits
- Then the system maps UI values to backend DTO fields
- Current status: implemented ad hoc in store code, not generalized

## 7. Scenario: Draft data is restored with matching schema
- Given draft data exists for a form version
- When the user reopens the draft
- Then the form should restore values under the matching schema
- Current status: target behavior only
