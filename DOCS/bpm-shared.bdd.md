# BPM Shared BDD

## 1. Purpose
- This document defines shared BPM behavior as business-facing workflow scenarios.

## 2. Scenario: A process instance is started when workflow begins
- Given a domain object has no process instance id
- When workflow logic requires a process
- Then the system starts a Camunda process instance
- And stores the returned process instance key
- Current status: implemented

## 3. Scenario: A workflow action completes the current active task
- Given a process instance exists
- And a CREATED user task exists for that process
- When a workflow action is triggered
- Then the system searches the active task
- And completes the task with workflow variables
- Current status: implemented

## 4. Scenario: The system retries task lookup briefly
- Given a process instance has just started or advanced
- When the next user task is not immediately searchable
- Then the system retries task lookup up to 10 times with 200ms delay
- Current status: implemented

## 5. Scenario: No active task exists
- Given no CREATED user task exists for the process instance
- When an action is triggered
- Then the action fails with business error
- Current status: implemented

## 6. Scenario: Action variables include business context
- Given a user triggers a workflow action
- When the action is sent to BPM
- Then the action name is included
- And available user roles are included when using the generic action path
- And optional payload is included when provided
- Current status: implemented

## 7. Scenario: Every action is auditable
- Given a workflow action is accepted
- When the action completes the current task
- Then an audit record is written
- And the audit record includes actor, action, task key, and payload snapshot
- Current status: implemented for generic action path

## 8. Scenario: BPM engine is unavailable
- Given Camunda cannot be reached
- When workflow start or update is attempted
- Then the action fails
- And the caller receives an error
- Current status: transport failure is not normalized into a dedicated UX path

## 9. Scenario: Unauthorized actor triggers a workflow action
- Given an actor does not have the required business role
- When the actor attempts the action
- Then the system should reject the action before or during BPM execution
- Current status: target behavior only
