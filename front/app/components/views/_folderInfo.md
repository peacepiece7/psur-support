# Folder Information (views)

이 폴더는 페이지 단위 화면을 구성하는 View 컴포넌트를 둡니다.

## 원칙
- 도메인 단위로 하위 폴더 분리
- View는 화면 조합/이벤트 연결에 집중
- API 호출/상태관리는 Store 또는 composable로 위임

예시:
- `views/reg-rqs/**`

## 권장
- OAS(OpenAPI) 타입 기준 DTO 사용
- 공통 UI는 atoms/molecules/organisms/templates로 분리
- 파일이 커지면 섹션 컴포넌트로 분리
