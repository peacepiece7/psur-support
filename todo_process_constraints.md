# TODO: Process Constraints

## One-Process-Per-User
- Limit to a single active process per user (or businessNo/CI).
- Reuse existing applyId/processTaskId if an active process already exists.
- Define completion/cleanup rule when process finishes (approve/reject).

## Stale Process Cleanup
- Detect and terminate stale process instances (no activity for N days).
- Remove or mark orphan processTaskId values in DB.
- Consider scheduled job + alerting for failures.
