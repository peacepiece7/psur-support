# Operator Action UI

## Goal
- 신청 상세 화면에서 운영자 role과 현재 상태에 맞는 action 버튼을 실행할 수 있게 한다.

## Current Step
- 상세 화면 action 호출 후 상태 코드 동기화와 승인 후속 처리 연결 완료

## Current State
- `users/me/detail`로 현재 사용자 role code를 조회한다.
- 상태 코드와 role code 기준으로 `receipt`, `review`, `approve`, `reject` 버튼을 제한한다.
- `/reg-sports-club-applications/{applyId}/actions` 호출과 optional note payload 전송을 구현했다.
- 백엔드는 action 호출 후 상태 코드를 즉시 갱신하고 `approve` 시 sports club 생성과 category 복사를 함께 처리한다.
- 프론트 상세 화면은 action 성공 후 최신 상세를 다시 조회한다.
- lint: `npx eslint app/pages/registered-sports-club/detail-view.vue` 통과
- test: `bash ./gradlew test --tests com.service.demo.domain.regsportsclub.service.RegSportsClubApplicationServiceTest --tests com.service.demo.domain.regsportsclub.service.ActionServiceTest` 통과

## Verification
- verdict: APPROVED
- attempts: 0

## Next Owner
- Coder

## Next Step
- FE form model과 BE DTO 간 mapping 문서화 또는 타입 재정리
