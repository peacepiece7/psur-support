# Folder Information

다음 규칙을 반드시 지켜서 Pinia Store를 생성해줘.

[환경]

- Nuxt 3
- Pinia (setup store)
- TypeScript
- Vue 3 Composition API

[설계 철학]

- 대형 Form / 신청서 중심 Store
- DTO는 UI 바인딩 안정성을 최우선으로 설계
- Pinia의 자유 + Vuex의 규율을 결합한 구조

[구조 규칙]

1. Store 내부는 반드시 다음 3개 영역으로 나눈다.

   - state
   - getters (PURE)
   - actions (PUBLIC API)

2. getters

   - 반드시 side-effect 없는 순수 함수만 작성
   - state를 읽기만 해야 함
   - getter는 객체 형태로 묶는다
     예: getters.dto.req(), getters.validate.hasError()
   - getter 내부 로직은 private helper 함수 호출만 허용

3. actions

   - 외부(UI/페이지)에서 호출 가능한 유일한 API
   - 비동기(API 호출)는 actions에서만 수행
   - actions는 객체 네임스페이스 허용
     예: actions.fetch.detail(), actions.step.next()
   - 직접 state를 수정하지 말고 private 함수 호출

4. private 함수

   - store 내부에서만 사용
   - 반드시 `_camelCase` 네이밍
   - 역할:
     - state 변경 (mutation 대체)
     - API 호출
     - getter 보조 로직
   - 외부 export 절대 금지

5. DTO / state

   - DTO 및 초기 state는 반드시 factory 함수로 생성
   - 상수 DTO 정의 금지
   - class DTO 사용 금지
   - Form DTO는 reactive로 관리
   - 선택 상태 / index / nullable 값은 ref 사용

6. 서버 응답(res)과 UI 요청(req)은 분리

   - state에는 res DTO만 유지
   - req DTO는 getter에서 변환 (\_toReq 패턴)

7. reset 전략
   - initialize action 제공
   - reset 시 factory 함수 기반으로 state 전체 초기화
   - step/index 등 UI 상태도 함께 초기화

[네이밍 규칙]

- actions.fetch.xxx()
- actions.step.xxx()
- getters.dto.xxx()
- getters.validate.xxx()
- private helpers: \_toReq, \_applyDetail, \_resetState 등

[반환 형태]

- state는 toRefs로 반환
- getters, actions는 객체 그대로 반환
- store 외부에서는 actions만 사용하도록 설계

[금지 사항]

- getter에서 state 변경
- ref 객체에 v-model 직접 바인딩
- Object.assign(ref, data)
- 외부에서 private 함수 호출

[출력 요구]

- 실제 실무에서 바로 사용할 수 있는 완성 코드
- mock API 함수 포함 가능
- 타입 안정성 유지
