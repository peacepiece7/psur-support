declare global {
  namespace NodeJS {
    interface ProcessEnv {
      NUXT_PUBLIC_API_BASE_URL?: string
      NODE_ENV: 'development' | 'production' | 'test'
    }
  }
}

export {}
