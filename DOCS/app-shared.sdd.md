# App Shared SDD

## 1. Purpose
- This document defines the shared application contract used across frontend and backend.
- It covers runtime configuration, authentication, API response conventions, shared data rules, and generated client rules.

## 2. Runtime Stack
- Backend
  - Spring Boot `3.3.5`
  - Java `17`
  - MyBatis `3.0.5`
  - MySQL
  - springdoc OpenAPI
- Frontend
  - Nuxt `3`
  - Vuetify
  - Pinia
  - vee-validate
  - OpenAPI generated TypeScript client
- External
  - Camunda Orchestration REST

## 3. Runtime Configuration
- Backend port: `9090`
- Frontend API base URL: `process.env.NUXT_PUBLIC_API_BASE_URL || 'http://localhost:9090'`
- Camunda base URL: `http://localhost:8080`
- Camunda process definition id: `registration_sports_club_process`

## 4. Authentication Contract
- Authentication model is session-based.
- Login endpoint stores `SessionConst.USER_ID` into `HttpSession`.
- Logout invalidates the session.
- Protected endpoints are guarded by `AuthorizationInterceptor`.
- Excluded paths:
  - `/auth/login`
  - `/auth/register`
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
  - `/error`
  - `/actuator/**`
- Current frontend auth state is inconsistent:
  - backend is session-based
  - frontend `authStore` also keeps user data in `localStorage`
  - `auth.global.ts` currently uses a fake SSR session bootstrap

## 5. API Response Contract
- Every backend controller returns `ApiResponse<T>`.
- Structure:
  - `resultCode: int`
  - `resultMessage: string`
  - `data: T | null`
- Success contract:
  - `resultCode = 200`
  - `resultMessage = "OK"`
- Failure contract:
  - `ApiResponse.fail(code, message)`
  - frontend commonly checks `resultCode !== 200`

## 6. Data Access Flow
- Canonical backend flow:
  - DB schema
  - MyBatis mapper XML
  - mapper interface
  - service
  - controller
  - OpenAPI docs
- Canonical frontend flow:
  - generated OpenAPI spec
  - generated services/models
  - `app/types/models` mirror
  - page/store usage

## 7. OpenAPI Generation Contract
- Generation script: `front/scripts/generate-open-api-sepc.cjs`
- Source endpoint: `http://127.0.0.1:9090/v3/api-docs`
- Output root: `front/__generated__`
- Models are copied from `__generated__/default/models` to `front/app/types/models`
- Generated code must not be manually edited.

## 8. Shared Identifier Rules
- `id`: generic DB row id
- `userId`: authenticated user id
- `roleId`: role table id
- `applyId`: application header id
- `applicationId`: application detail id
- `approvedClubId`: created sports club id after approval

## 9. Shared Common Code Rule
- Business state and category data rely on `common_code` and `common_code_group`
- Shared code usages already present:
  - application status
  - club role
  - operating sport tree

## 10. Shared Risks
- frontend auth bootstrap is fake and not bound to backend session truth
- generated OpenAPI service is only partially used; many pages call `$fetch` directly
- frontend success/failure UX is not centralized

## 11. Source References
- `backend/src/main/resources/application.properties`
- `backend/src/main/java/com/service/demo/config/WebConfig.java`
- `backend/src/main/java/com/service/demo/common/api/ApiResponse.java`
- `front/app/constants/url.ts`
- `front/scripts/generate-open-api-sepc.cjs`
- `front/__generated__/default/core/OpenAPI.ts`
- `front/app/middleware/auth.global.ts`
- `front/app/stores/authStore.ts`
