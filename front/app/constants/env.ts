export const ENV = {
  NODE_ENV: process.env.NODE_ENV || 'development',
  API_BASE_URL: process.env.NUXT_PUBLIC_API_BASE_URL || '',
} as const
