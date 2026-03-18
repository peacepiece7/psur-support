# PSUR Frontend 가이드

## 개요

- Nuxt 3
- Vuetify + Tailwind
- Pinia
- Axios
- Vee-Validate
- CKEditor

## 실행

```bash
npm install
npm run dev
```

## 환경 변수

- `NUXT_PUBLIC_API_BASE_URL`
- 미지정 시 기본값: `http://localhost:9090`

예시:

```env
NUXT_PUBLIC_API_BASE_URL=http://localhost:9090
```

## 주요 스크립트

- `npm run dev`
- `npm run build`
- `npm run preview`
- `npm run lint`
- `npm run gen:oas`

## 등록 스포츠클럽 신청 API

- 저장: `/reg-sports-club-applications/save`
- 신청: `/reg-sports-club-applications/apply`
- 액션: `/reg-sports-club-applications/{applyId}/actions`

## 인코딩

- 문서/소스는 UTF-8 유지
