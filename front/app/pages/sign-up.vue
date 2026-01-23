<script setup lang="ts">
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import type { RegisterRequest } from '~/types/models/RegisterRequest'
  import type { ApiResponseLoginResponse } from '~/types/models/ApiResponseLoginResponse'
  import FormField from '~/components/molecules/FormField.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import Button from '~/components/atoms/Button.vue'
  import { API_BASE_URL } from '~/constants/url'

  /* =========================
   * validation schema
   * ========================= */
  const registerSchema = yup.object({
    loginId: yup
      .string()
      .required('아이디를 입력해주세요')
      .min(4, '아이디는 최소 4자 이상이어야 합니다'),
    username: yup.string().required('이름을 입력해주세요'),
    password: yup
      .string()
      .required('비밀번호를 입력해주세요')
      .min(8, '비밀번호는 최소 8자 이상이어야 합니다'),
    email: yup.string().email('올바른 이메일 형식을 입력해주세요').optional(),
    telno: yup.string().optional(),
  })

  type RegisterFormValues = yup.InferType<typeof registerSchema>

  // useForm 설정
  const { handleSubmit, values, isSubmitting } = useForm<RegisterFormValues>({
    validationSchema: toTypedSchema(registerSchema),
    initialValues: {
      loginId: '',
      username: '',
      password: '',
      email: '',
      telno: '',
    },
  })

  /* =========================
   * register handler
   * ========================= */
  const onSubmit = handleSubmit(async (formValues) => {
    try {
      const requestBody: RegisterRequest = {
        loginId: formValues.loginId,
        username: formValues.username,
        password: formValues.password,
        email: formValues.email || undefined,
        telno: formValues.telno || undefined,
      }

      const response = await $fetch<ApiResponseLoginResponse>(
        `${API_BASE_URL}/auth/register`,
        {
          method: 'POST',
          body: requestBody,
        },
      )

      if (response.resultCode === 0 || response.data) {
        // 회원가입 성공
        alert('회원가입이 완료되었습니다.')
        await navigateTo('/')
      } else {
        // 회원가입 실패
        alert(
          response.resultMessage ||
            '회원가입에 실패했습니다. 다시 시도해주세요.',
        )
      }
    } catch (error: unknown) {
      console.error('Register error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '회원가입 중 오류가 발생했습니다. 다시 시도해주세요.'
      alert(errorMessage)
    }
  })
</script>

<template>
  <div class="sign-up-page">
    <v-card class="mx-auto" style="width: 100%; max-width: 480px">
      <v-card-title
        class="border-grey-200 border-b px-6 py-4 text-xl font-bold"
      >
        회원가입
      </v-card-title>

      <v-card-text
        class="gap-4 p-6"
        style="display: flex; flex-direction: column"
      >
        <form @submit.prevent="onSubmit">
          <div class="grid gap-4">
            <!-- 아이디 -->
            <FormField name="loginId">
              <template #default="{ bind, field }">
                <TextField
                  :model-value="field.value"
                  v-bind="bind"
                  label="아이디"
                  placeholder="아이디를 입력하세요 (최소 4자)"
                  size="max"
                  autocomplete="username"
                />
              </template>
            </FormField>

            <!-- 이름 -->
            <FormField name="username">
              <template #default="{ bind, field }">
                <TextField
                  :model-value="field.value"
                  v-bind="bind"
                  label="이름"
                  placeholder="이름을 입력하세요"
                  size="max"
                  autocomplete="name"
                />
              </template>
            </FormField>

            <!-- 비밀번호 -->
            <FormField name="password">
              <template #default="{ bind, field }">
                <TextField
                  :model-value="field.value"
                  v-bind="bind"
                  label="비밀번호"
                  type="password"
                  placeholder="비밀번호를 입력하세요 (최소 8자)"
                  size="max"
                  autocomplete="new-password"
                />
              </template>
            </FormField>

            <!-- 이메일 (선택) -->
            <FormField name="email">
              <template #default="{ bind, field }">
                <TextField
                  :model-value="field.value"
                  v-bind="bind"
                  label="이메일 (선택)"
                  type="email"
                  placeholder="이메일을 입력하세요"
                  size="max"
                  autocomplete="email"
                />
              </template>
            </FormField>

            <!-- 전화번호 (선택) -->
            <FormField name="telno">
              <template #default="{ bind, field }">
                <TextField
                  :model-value="field.value"
                  v-bind="bind"
                  label="전화번호 (선택)"
                  type="tel"
                  placeholder="전화번호를 입력하세요"
                  size="max"
                  autocomplete="tel"
                />
              </template>
            </FormField>

            <!-- 제출 버튼 -->
            <Button
              type="submit"
              color="primary"
              size="lg"
              :disabled="isSubmitting"
              class="mt-2"
            >
              {{ isSubmitting ? '처리 중...' : '회원가입' }}
            </Button>
          </div>
        </form>

        <!-- 로그인 링크 -->
        <div class="mt-4 text-center">
          <NuxtLink to="/" class="text-grey-600 text-sm">
            이미 계정이 있으신가요? 로그인
          </NuxtLink>
        </div>
      </v-card-text>
    </v-card>
  </div>
</template>

<style scoped>
  .sign-up-page {
    width: 100%;
    padding: 2rem 1rem;
  }
</style>
