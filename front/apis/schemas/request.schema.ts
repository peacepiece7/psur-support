import * as yup from 'yup'

export const requestSchema = yup.object({
  name: yup.string().required('이름을 입력해주세요'),
  email: yup
    .string()
    .email('올바른 이메일을 입력해주세요')
    .required('이메일을 입력해주세요'),
  message: yup.string().required('메시지를 입력해주세요'),
})

export type RequestSchema = yup.InferType<typeof requestSchema>
