export default defineNuxtPlugin((nuxtApp) => {
  const api = $fetch.create({
    baseURL: '/api',
    async onRequest({ request, options }) {
      const headers = useRequestHeaders(['cookie', 'user-agent'])

      if (import.meta.server) {
        const event = useRequestEvent()
        const ip = event?.context.clientIp

        options.headers = {
          ...headers,
          ...options.headers,
          ['X-Client-IP']: ip || '',
        } as Headers
      }
    },
    onRequestError({ request, options, error }) {
      // 요청 에러시 함수
      if (import.meta.server) {
        console.log('request_error', error)
      }
    },
    onResponse({ request, response, options }) {
      // 응답시 함수
    },
    async onResponseError({ request, response, options }) {
      if (import.meta.server) {
        console.log('response_error', response)
      }
      // 응답 에러시 함수
      // if (response.status === 401) {
      //     await nuxtApp.runWithContext(() => navigateTo('/login'))
      //   }
    },
  })

  return { provide: { api } }
})
