import type { User } from '~/stores/authStore'

export function useAuth() {
  const auth = useAuthStore()

  const loginNative = async (id: string, pw: string, skipNavigate = false) => {
    alert('네이티브 로그인 성공!')
    auth.setUser({ id, name: 'Native User' })
    if (!skipNavigate) {
      await navigateTo('/')
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
