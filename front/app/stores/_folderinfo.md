# Folder Information (stores)

이 폴더는 Pinia Store를 둡니다.

## 기준
- Nuxt 3
- Pinia(setup store)
- TypeScript
- Vue 3 Composition API

## 원칙
- `state`, `getters`, `actions` 분리
- `getters`는 순수 함수 유지
- 비동기/API 호출은 `actions`에서만 수행
- 외부에서는 `actions` 중심으로 사용

## 네이밍
- `actions.fetch.xxx()`
- `actions.step.xxx()`
- `getters.dto.xxx()`
- `getters.validate.xxx()`
- private helper: `_camelCase`

## 금지
- getter 내부 state 변경
- 외부에서 store private 함수 호출

## 반환
- state는 `toRefs` 기반 반환
- `getters`, `actions`는 객체 형태 반환
