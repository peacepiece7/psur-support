# App Shared BDD

## 1. Purpose
- This document defines shared user-visible behavior and acceptance criteria across the application.

## 2. BDD Writing Rule
- Each scenario must be written from business behavior first.
- Each scenario must include:
  - actor
  - precondition
  - trigger
  - expected API interaction
  - expected UI result
  - expected failure result
  - current implementation status

## 3. Scenario: User logs in
- Given valid credentials exist
- When the user submits login
- Then backend authenticates the user
- And backend writes `USER_ID` into session
- And response returns user info
- Current status:
  - backend implemented
  - frontend auth bootstrap still fake

## 4. Scenario: User logs out
- Given a valid session exists
- When the user triggers logout
- Then the session is invalidated
- And subsequent protected requests should fail auth
- Current status: backend implemented

## 5. Scenario: User accesses protected page
- Given no valid session exists
- When the user accesses a protected page
- Then the system should prevent access
- Current status:
  - backend protection exists
  - frontend redirect behavior is not authoritative yet

## 6. Scenario: API call succeeds
- Given the request is valid
- When frontend sends a business API request
- Then backend returns `ApiResponse`
- And frontend treats `resultCode = 200` as success
- Current status: implemented broadly

## 7. Scenario: Business API returns handled failure
- Given the request violates a business rule
- When backend raises a handled business error
- Then backend returns a failure response
- And frontend shows backend message or fallback text
- Current status: implemented inconsistently

## 8. Scenario: Transport or server failure occurs
- Given the server is unavailable or the request fails unexpectedly
- When frontend calls the API
- Then frontend catches the error
- And the user sees a generic failure message
- Current status: implemented with ad hoc alerts

## 9. Scenario: Common code-driven option data is used
- Given a form field depends on common code values
- When the page loads option data
- Then the page fetches the code tree from backend
- And renders options without hardcoded business values
- Current status: implemented for operating sports

## 10. Automation Targets
- login success
- logout success
- protected endpoint access without session
- success response parsing
- business failure parsing
- server failure fallback handling
