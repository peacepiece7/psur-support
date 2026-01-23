export interface AuthResponse {
  sessionYn: 'Y' | 'N'
  user: {
    id: string
    name: string
  } | null
}

export default defineNuxtRouteMiddleware(async (to, from) => {
  // SSR에서 세션 로딩하는 부분을 완전 하드코딩으로 처리.
  // const fullPath = to.fullPath

  clearNuxtState('pageAuth')

  const { data } = await useAsyncData(
    'fakeSession',
    () => {
      return Promise.resolve({
        sessionYn: 'Y' as 'Y' | 'N', // or "N"
        user: { id: 'demo', name: 'Server User' },
      })
    },
    {
      //   headers: {
      //     'x-router-fullpath': fullPath,
      //   },
    },
  )

  const pageAuth = useState<AuthResponse>('pageAuth', () => data.value)

  // 예: 로그인 되어있는데 login 페이지로 접근하면 차단
  if (pageAuth.value?.sessionYn === 'Y' && to.name === 'login') {
    return navigateTo('/')
  }
})
