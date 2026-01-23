import type {
  RequestPayload,
  RequestResponse,
} from '~~/apis/types/request.types'

export default defineEventHandler(async (event): Promise<RequestResponse> => {
  const body = await readBody<RequestPayload>(event)

  // 여기에 실제 로직 구현
  const response: RequestResponse = {
    id: Date.now().toString(),
    name: body.name,
    email: body.email,
    message: body.message,
    createdAt: new Date().toISOString(),
  }

  return response
})
