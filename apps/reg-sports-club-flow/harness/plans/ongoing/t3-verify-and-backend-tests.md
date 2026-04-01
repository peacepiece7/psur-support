# Front Verify And Backend Tests

## Goal
- 프론트 typecheck 실행환경을 복구하고, save/apply/action 서버 테스트를 추가한다.

## Current Step
- 프론트 `verify`와 백엔드 `gradlew test` 통과

## Current State
- `front/package.json`에 `lint:check`, `typecheck`, `verify` 스크립트를 추가했다.
- `vue-tsc`를 dev dependency로 추가해 `nuxt typecheck`가 동작한다.
- 백엔드에 `ActionServiceTest`, `RegSportsClubApplicationServiceTest`를 추가했다.
- 검증 결과:
  - `npm run verify`: 통과
  - `bash gradlew test`: 통과

## Verification
- verdict: APPROVED
- attempts: 0

## Next Owner
- Coder

## Next Step
- FE form model과 BE DTO 간 mapping 문서화 또는 타입 재정리를 진행한다.
