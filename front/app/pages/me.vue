<script setup lang="ts">
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import type { UserResponse } from '~/types/models/UserResponse'
  import type { UpdateProfileRequest } from '~/types/models/UpdateProfileRequest'
  import type { ApiResponseUserResponse } from '~/types/models/ApiResponseUserResponse'
  import FormField from '~/components/molecules/FormField.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import Button from '~/components/atoms/Button.vue'
  import { API_BASE_URL } from '~/constants/url'

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  // 로그인하지 않은 경우 홈으로 리다이렉트
  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  /* =========================
   * validation schema
   * ========================= */
  const profileSchema = yup.object({
    username: yup.string().required('이름을 입력해주세요'),
    email: yup.string().email('올바른 이메일 형식을 입력해주세요').optional(),
    telno: yup.string().optional(),
  })

  type ProfileFormValues = yup.InferType<typeof profileSchema>

  // 사용자 정보 상태
  const userInfo = ref<UserResponse | null>(null)
  const isLoading = ref(false)
  const isSubmitting = ref(false)

  /* =========================
   * 내 정보 조회
   * ========================= */
  const fetchMyProfile = async () => {
    try {
      isLoading.value = true
      const response = await $fetch<ApiResponseUserResponse>(
        `${API_BASE_URL}/users/me`,
        {
          method: 'GET',
          credentials: 'include',
        },
      )

      if (response.resultCode === 200 && response.data) {
        userInfo.value = response.data
      } else {
        alert(response.resultMessage || '내 정보를 불러오는데 실패했습니다.')
      }
    } catch (error: unknown) {
      console.error('Fetch profile error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '내 정보를 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoading.value = false
    }
  }

  // useForm 설정
  const { handleSubmit, values, resetForm } = useForm<ProfileFormValues>({
    validationSchema: toTypedSchema(profileSchema),
    initialValues: {
      username: '',
      email: '',
      telno: '',
    },
  })

  // 사용자 정보가 로드되면 폼에 반영
  watch(
    () => userInfo.value,
    (user) => {
      if (user) {
        resetForm({
          values: {
            username: user.username || '',
            email: user.email || '',
            telno: user.telno || '',
          },
        })
      }
    },
    { immediate: true },
  )

  /* =========================
   * 내 정보 수정
   * ========================= */
  const onSubmit = handleSubmit(async (formValues) => {
    try {
      isSubmitting.value = true
      const requestBody: UpdateProfileRequest = {
        username: formValues.username,
        email: formValues.email || undefined,
        telno: formValues.telno || undefined,
      }

      const response = await $fetch<ApiResponseUserResponse>(
        `${API_BASE_URL}/users/me`,
        {
          method: 'PUT',
          body: requestBody,
          credentials: 'include',
        },
      )

      if (response.resultCode === 200 && response.data) {
        userInfo.value = response.data
        alert('내 정보가 수정되었습니다.')
      } else {
        alert(
          response.resultMessage ||
            '내 정보 수정에 실패했습니다. 다시 시도해주세요.',
        )
      }
    } catch (error: unknown) {
      console.error('Update profile error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '내 정보 수정 중 오류가 발생했습니다. 다시 시도해주세요.'
      alert(errorMessage)
    } finally {
      isSubmitting.value = false
    }
  })

  // 페이지 로드 시 내 정보 조회
  onMounted(() => {
    fetchMyProfile()
  })
</script>

<template>
   <div class="relative min-h-screen w-full">
 <PageHeaderSection
      title="마이페이지"
      image-src="/main_banner.png"
      image-alt="마이페이지 헤더 이미지"
      type="infoPage"
    />
 <div class="me-page bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-2xl">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">마이페이지</h1>

      <v-card v-if="isLoading" class="p-8 text-center">
        <p>내 정보를 불러오는 중...</p>
      </v-card>

      <v-card v-else class="p-8">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">내 정보</h2>

        <form @submit.prevent="onSubmit">
          <div class="grid gap-4">
            <!-- 아이디 (읽기 전용) -->
            <div class="grid gap-1">
              <label class="text-grey-900 text-sm leading-[1.2] font-bold">
                아이디
              </label>
              <TextField
                :model-value="userInfo?.loginId || ''"
                label=""
                placeholder=""
                size="max"
                readonly
                disabled
              />
            </div>

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

            <!-- 이메일 -->
            <FormField name="email">
              <template #default="{ bind, field }">
                <TextField
                  :model-value="field.value"
                  v-bind="bind"
                  label="이메일"
                  type="email"
                  placeholder="이메일을 입력하세요"
                  size="max"
                  autocomplete="email"
                />
              </template>
            </FormField>

            <!-- 전화번호 -->
            <FormField name="telno">
              <template #default="{ bind, field }">
                <TextField
                  :model-value="field.value"
                  v-bind="bind"
                  label="전화번호"
                  type="tel"
                  placeholder="전화번호를 입력하세요"
                  size="max"
                  autocomplete="tel"
                />
              </template>
            </FormField>

            <!-- 수정 버튼 -->
            <Button
              type="submit"
              color="primary"
              size="lg"
              :disabled="isSubmitting"
              class="mt-4"
            >
              {{ isSubmitting ? '수정 중...' : '수정하기' }}
            </Button>
          </div>
        </form>
      </v-card>
    </div>
  </div>
</div>
</template>

<style scoped>
  .me-page {
    min-height: 100vh;
  }
</style>
