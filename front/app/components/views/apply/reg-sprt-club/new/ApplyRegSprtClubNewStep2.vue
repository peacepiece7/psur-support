<script setup lang="ts">
  import { useFormContext } from 'vee-validate'
  import FormField from '~/components/molecules/FormField.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import SelectV2 from '~/components/atoms/SelectV2.vue'
  import type { CommonCodeGroupResponse } from '~/types/models/CommonCodeGroupResponse'
  import type { ApiResponseCommonCodeGroupResponse } from '~/types/models/ApiResponseCommonCodeGroupResponse'
  import { API_BASE_URL } from '~/constants/url'
  import { useNewRegSprtClubStore } from '~/stores/newRegSprtClubStore'

  const newRegSprtClubStore = useNewRegSprtClubStore()

  // 폼 타입 정의
  type FormValues = {
    applicantName: string
    applicantTelno: string
    applicantEmail: string
    name: string
    location: string
    representativeName: string
    representativeTelno: string
    businessNo: string
    operatingSportParentCodeId: {
      title: string
      value: string
      id?: number
    } | null
    operatingSportChildCodeId: {
      title: string
      value: string
      id?: number
    } | null
  }

  // useFormContext에서 values 가져오기
  const { values, setFieldValue } = useFormContext<FormValues>()

  // 운영종목 트리 데이터
  const operatingSportTree = ref<CommonCodeGroupResponse | null>(null)
  const isLoadingOperatingSports = ref(false)

  // 운영종목 상위 코드 목록 (최상위가 아닌 상위-하위 형태)
  const operatingSportParentOptions = computed(() => {
    if (!operatingSportTree.value?.codes) return []
    return operatingSportTree.value.codes.map((code) => ({
      ...code,
      title: code.codeName || code.code || '',
      value: code.code || '',
    }))
  })

  // 선택된 상위 코드의 하위 코드 목록
  const operatingSportChildOptions = computed(() => {
    const parentCodeId = values.operatingSportParentCodeId?.value || ''
    if (!parentCodeId || !operatingSportTree.value?.codes) {
      return []
    }

    const selectedParent = operatingSportTree.value.codes.find(
      (code) => code.code === parentCodeId,
    )

    if (!selectedParent?.groupCode) return []

    // 하위 그룹 찾기
    const childGroup = operatingSportTree.value.children?.find(
      (group) => group.groupCode === selectedParent.groupCode,
    )

    if (!childGroup?.codes) return []

    return childGroup.codes.map((code) => ({
      ...code,
      title: code.codeName || code.code || '',
      value: code.code || '',
    }))
  })

  // 운영종목 트리 조회
  const fetchOperatingSportTree = async () => {
    try {
      isLoadingOperatingSports.value = true
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/OPERATING_SPORT/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 3,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data) {
        operatingSportTree.value = response.data
      }
    } catch (error) {
      console.error('Fetch operating sport tree error:', error)
    } finally {
      isLoadingOperatingSports.value = false
    }
  }

  // 운영종목 상위 코드 선택 시 하위 코드 초기화
  watch(
    () => values.operatingSportParentCodeId,
    () => {
      setFieldValue('operatingSportChildCodeId', null)
    },
  )

  // 페이지 마운트 시 데이터 로드
  onMounted(() => {
    fetchOperatingSportTree()
  })
</script>

<template>
  <section
    class="bg-grey-50 border-grey-300 min-h-[420px] rounded-3xl border p-10"
  >
    <h3 class="m-0 mb-4 text-2xl font-bold">2단계. 클럽 정보 입력</h3>

    <div class="grid max-w-[720px] gap-4">
      <div class="flex">
        <FormField class="flex-1" name="applicantName">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="신청자명"
              placeholder="신청자명을 입력하세요"
              size="md"
            />
          </template>
        </FormField>

        <FormField class="flex-1" name="applicantTelno">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="신청자 전화번호"
              placeholder="예) 02-1234-5678"
              type="tel"
              size="md"
            />
          </template>
        </FormField>
      </div>

      <div class="flex">
        <FormField class="flex-1" name="applicantEmail">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="신청자 이메일"
              placeholder="예) example@email.com"
              type="email"
              size="md"
            />
          </template>
        </FormField>

        <FormField class="flex-1" name="name">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="클럽명"
              placeholder="클럽명을 입력하세요"
              size="md"
            />
          </template>
        </FormField>
      </div>

      <div class="flex">
        <FormField class="flex-1" name="location">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="위치"
              placeholder="위치를 입력하세요"
              size="md"
            />
          </template>
        </FormField>

        <FormField class="flex-1" name="representativeName">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="대표자명"
              placeholder="대표자명을 입력하세요"
              size="md"
            />
          </template>
        </FormField>
      </div>

      <div class="flex">
        <FormField class="flex-1" name="representativeTelno">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="대표자 전화번호"
              placeholder="예) 02-1234-5678"
              type="tel"
              size="md"
            />
          </template>
        </FormField>

        <FormField class="flex-1" name="businessNo">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="사업자등록번호"
              placeholder="사업자등록번호를 입력하세요"
              size="md"
            />
          </template>
        </FormField>
      </div>

      <div class="flex">
        <FormField class="flex-1" name="operatingSportParentCodeId">
          <template #default="{ bind, field }">
            <SelectV2
              :model-value="field.value"
              v-bind="bind"
              :items="operatingSportParentOptions"
              label="운영종목 (상위)"
              placeholder="운영종목 상위를 선택하세요"
              size="md"
              :loading="isLoadingOperatingSports"
            />
          </template>
        </FormField>

        <FormField class="flex-1" name="operatingSportChildCodeId">
          <template #default="{ bind, field }">
            <SelectV2
              :model-value="field.value"
              v-bind="bind"
              :items="operatingSportChildOptions"
              label="운영종목 (하위)"
              placeholder="운영종목 하위를 선택하세요"
              size="md"
              :disabled="!values.operatingSportParentCodeId"
            />
          </template>
        </FormField>
      </div>
    </div>
  </section>
</template>

<style scoped></style>
