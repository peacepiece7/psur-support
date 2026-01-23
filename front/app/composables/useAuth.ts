import type { ApiResponseLoginResponse } from '~/types/models/ApiResponseLoginResponse'
import { API_BASE_URL } from '~/constants/url'

export function useAuth() {
  const auth = useAuthStore()

  const loginNative = async (id: string, pw: string, skipNavigate = false) => {
    try {
      const response = await $fetch<ApiResponseLoginResponse>(
        `${API_BASE_URL}/auth/login`,
        {
          method: 'POST',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
          body: {
            loginId: id,
            password: pw,
          },
        },
      )

      if (response.data) {
        const userData = response.data

        if (!userData.loginId || !userData.username) {
          throw new Error('로그인 응답에 사용자 데이터가 없습니다.')
        }

        auth.setUser({
          id: userData.loginId,
          name: userData.username,
        })
        if (!skipNavigate) {
          await navigateTo('/')
        }
      } else {
        throw new Error('로그인 응답에 사용자 데이터가 없습니다.')
      }
    } catch (error) {
      console.error('로그인 실패:', error)
      throw error
    }
  }

  const loginKakao = async (skipNavigate = false) => {
    alert('카카오 로그인 성공!')
    auth.setUser({ id: 'kakao123', name: 'Kakao User' })
    if (!skipNavigate) {
      await navigateTo('/')
    }
  }

  const loginNaver = async (skipNavigate = false) => {
    alert('네이버 로그인 성공!')
    auth.setUser({ id: 'naver123', name: 'Naver User' })
    if (!skipNavigate) {
      await navigateTo('/')
    }
  }

  const loginNice = async (skipNavigate = false) => {
    alert('본인인증(NICE) 성공!')
    auth.setUser({ id: 'nice123', name: 'Nice User' })
    if (!skipNavigate) {
      await navigateTo('/')
    }
  }

  return {
    loginNative,
    loginKakao,
    loginNaver,
    loginNice,
  }
}
