import type { RequestPayload, RequestResponse } from '../types/request.types'
import { API_ENDPOINTS } from '~/constants/url'

export const requestApi = {
  create: async (payload: RequestPayload): Promise<RequestResponse> => {
    return await $fetch<RequestResponse>(API_ENDPOINTS.REQUEST, {
      method: 'POST',
      body: payload,
    })
  },
}
