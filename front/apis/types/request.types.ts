export interface RequestPayload {
  name: string
  email: string
  message: string
}

export interface RequestResponse {
  id: string
  name: string
  email: string
  message: string
  createdAt: string
}
