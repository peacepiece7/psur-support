import { defineStore } from 'pinia'

export interface User {
  id: string
  name: string
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isLoggedIn: false,
    user: null as User | null,
    token: null as string | null,
  }),

  actions: {
    setUser(user: User) {
      this.user = user
      this.isLoggedIn = true
      if (typeof window !== 'undefined') {
        localStorage.setItem('auth-user', JSON.stringify(user))
      }
    },

    logout() {
      this.isLoggedIn = false
      this.user = null
      this.token = null
      if (typeof window !== 'undefined') {
        localStorage.removeItem('auth-user')
      }
    },

    loadFromLocalStorage() {
      if (typeof window === 'undefined') return

      const saved = localStorage.getItem('auth-user')
      if (saved) {
        try {
          this.user = JSON.parse(saved)
          this.isLoggedIn = true
        } catch (e) {
          console.error('Failed to parse auth-user from localStorage', e)
        }
      }
    },
  },
})
