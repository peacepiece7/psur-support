export default defineNuxtPlugin((nuxtApp) => {
  // 클라이언트 사이드에서만 localStorage에서 사용자 정보 불러오기
  const authStore = useAuthStore()

  // @TODO: 이거 임시 코드임 hydraion 에러 안나게 임시 처리 나중에 api 연동하면 삭제!!
  nuxtApp.hook('app:mounted', async () => {
    setTimeout(() => {
      authStore.loadFromLocalStorage()
    }, 300)
  })
})
