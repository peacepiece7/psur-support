export const API_BASE_URL =
  process.env.NUXT_PUBLIC_API_BASE_URL || 'http://localhost:9090'

export const API_ENDPOINTS = {
  REQUEST: `${API_BASE_URL}`,
} as const
