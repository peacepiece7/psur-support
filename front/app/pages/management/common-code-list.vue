<script setup lang="ts">
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import type { CommonCodeResponse } from '~/types/models/CommonCodeResponse'
  import type { CommonCodeGroupSummaryResponse } from '~/types/models/CommonCodeGroupSummaryResponse'
  import type { ApiResponseCommonCodeGroupResponse } from '~/types/models/ApiResponseCommonCodeGroupResponse'
  import type { ApiResponseListCommonCodeGroupSummaryResponse } from '~/types/models/ApiResponseListCommonCodeGroupSummaryResponse'
  import SelectV2 from '~/components/atoms/SelectV2.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import NumberInput from '~/components/atoms/NumberInput.vue'
  import Button from '~/components/atoms/Button.vue'
  import Switch from '~/components/atoms/Switch.vue'
  import FormField from '~/components/molecules/FormField.vue'
  import { API_BASE_URL } from '~/constants/url'

  // 공통코드 생성 요청 타입
  type CommonCodeCreateRequest = {
    groupCode: string
    code: string
    codeName: string
    childGroupCode?: string
    sortOrder: number
    isActive: boolean
    description?: string
  }

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  // ========== 최상위 그룹 목록 섹션 ==========
  const rootGroups = ref<CommonCodeGroupSummaryResponse[]>([])
  const selectedRootGroupForEdit = ref<CommonCodeGroupSummaryResponse | null>(null)
  const isRootGroupEditMode = ref<'add' | 'edit' | null>(null)
  const isSubmittingRootGroup = ref(false)

  // 루트 그룹 폼 스키마
  const rootGroupFormSchema = yup.object({
    groupCode: yup.string().required('그룹 코드를 입력해주세요'),
    groupName: yup.string().required('그룹명을 입력해주세요'),
    description: yup.string().optional(),
    sortOrder: yup
      .number()
      .required('정렬 순서를 입력해주세요')
      .min(0, '정렬 순서는 0 이상이어야 합니다')
      .integer('정렬 순서는 정수여야 합니다'),
  })

  type RootGroupFormValues = yup.InferType<typeof rootGroupFormSchema>

  const rootGroupForm = useForm<RootGroupFormValues>({
    validationSchema: toTypedSchema(rootGroupFormSchema),
    initialValues: {
      groupCode: '',
      groupName: '',
      description: '',
      sortOrder: 0,
    },
  })

  const {
    handleSubmit: handleRootGroupSubmit,
    resetForm: resetRootGroupForm,
    setValues: setRootGroupValues,
  } = rootGroupForm

  // 최상위 그룹 목록 조회
  const fetchRootGroups = async () => {
    try {
      const response = await $fetch<ApiResponseListCommonCodeGroupSummaryResponse>(
        `${API_BASE_URL}/common-codes/groups/root`,
        {
          method: 'GET',
          credentials: 'include',
        },
      )

      if (response.resultCode === 200 && response.data) {
        rootGroups.value = response.data
      } else {
        alert(response.resultMessage || '루트 그룹 목록을 불러오는데 실패했습니다.')
      }
    } catch (error: unknown) {
      console.error('Fetch root groups error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '루트 그룹 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    }
  }

  // 최상위 그룹 추가 모드
  const startAddRootGroup = () => {
    isRootGroupEditMode.value = 'add'
    selectedRootGroupForEdit.value = null
    resetRootGroupForm()
    setRootGroupValues({
      groupCode: '',
      groupName: '',
      description: '',
      sortOrder: 0,
    })
  }

  // 최상위 그룹 수정 모드
  const startEditRootGroup = (group: CommonCodeGroupSummaryResponse) => {
    isRootGroupEditMode.value = 'edit'
    selectedRootGroupForEdit.value = group
    setRootGroupValues({
      groupCode: group.groupCode || '',
      groupName: group.groupName || '',
      description: group.description || '',
      sortOrder: group.sortOrder || 0,
    })
  }

  // 최상위 그룹 편집 취소
  const cancelRootGroupEdit = () => {
    isRootGroupEditMode.value = null
    selectedRootGroupForEdit.value = null
    resetRootGroupForm()
  }

  // 최상위 그룹 생성/수정
  const createRootGroup = async (data: RootGroupFormValues) => {
    try {
      const response = await $fetch<{ resultCode?: number; resultMessage?: string }>(
        `${API_BASE_URL}/common-codes/groups/root`,
        {
          method: 'POST',
          credentials: 'include',
          body: data,
        },
      )

      if (response.resultCode === 200) {
        return true
      } else {
        alert(response.resultMessage || '루트 그룹 생성에 실패했습니다.')
        return false
      }
    } catch (error: unknown) {
      console.error('Create root group error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '루트 그룹 생성 중 오류가 발생했습니다.'
      alert(errorMessage)
      return false
    }
  }

  // 최상위 그룹 제출
  const onRootGroupSubmit = handleRootGroupSubmit(async (formValues) => {
    if (isSubmittingRootGroup.value) return

    try {
      isSubmittingRootGroup.value = true

      if (isRootGroupEditMode.value === 'add') {
        const success = await createRootGroup(formValues)
        if (success) {
          await fetchRootGroups()
          cancelRootGroupEdit()
          alert('루트 그룹이 생성되었습니다.')
        }
      } else if (isRootGroupEditMode.value === 'edit') {
        // 수정은 삭제 후 생성으로 처리 (API에 수정 엔드포인트가 없음)
        if (!selectedRootGroupForEdit.value?.groupCode) {
          alert('수정할 수 없습니다.')
          return
        }

        // TODO: 삭제 API 호출 후 생성
        alert('수정 기능은 아직 구현되지 않았습니다.')
      }
    } catch (error) {
      console.error('Root group submit error:', error)
    } finally {
      isSubmittingRootGroup.value = false
    }
  })

  // ========== 그룹별 코드 관리 섹션 ==========
  const selectedGroupCode = ref<string | null>(null)
  const selectedCode = ref<CommonCodeResponse['code'] | null>(null)
  const selectedChildCode = ref<CommonCodeResponse['code'] | null>(null)

  const codeList = ref<CommonCodeResponse[]>([])
  const childCodeList = ref<CommonCodeResponse[]>([])

  const isLoadingCodes = ref(false)
  const isLoadingChildCodes = ref(false)

  const isCodeEditMode = ref<'add' | 'edit' | null>(null)
  const isChildCodeEditMode = ref<'add' | 'edit' | null>(null)
  const editingCode = ref<CommonCodeResponse | null>(null)
  const editingChildCode = ref<CommonCodeResponse | null>(null)
  const isSubmittingCode = ref(false)

  // 코드 폼 스키마
  const codeFormSchema = yup.object({
    groupCode: yup.string().required('그룹 코드를 입력해주세요'),
    code: yup.string().required('코드를 입력해주세요'),
    codeName: yup.string().required('코드명을 입력해주세요'),
    childGroupCode: yup.string().optional(),
    sortOrder: yup
      .number()
      .required('정렬 순서를 입력해주세요')
      .min(0, '정렬 순서는 0 이상이어야 합니다')
      .integer('정렬 순서는 정수여야 합니다'),
    isActive: yup.boolean().required('활성 상태를 선택해주세요'),
    description: yup.string().optional(),
  })

  type CodeFormValues = yup.InferType<typeof codeFormSchema>

  const codeForm = useForm<CodeFormValues>({
    validationSchema: toTypedSchema(codeFormSchema),
    initialValues: {
      groupCode: '',
      code: '',
      codeName: '',
      childGroupCode: '',
      sortOrder: 0,
      isActive: true,
      description: '',
    },
  })

  const {
    handleSubmit: handleCodeSubmit,
    resetForm: resetCodeForm,
    setValues: setCodeValues,
  } = codeForm

  // 그룹 선택 시 코드 목록 조회
  watch(selectedGroupCode, async (newGroupCode) => {
    if (newGroupCode) {
      selectedCode.value = null
      selectedChildCode.value = null
      childCodeList.value = []
      await fetchCodeList(newGroupCode)
    } else {
      codeList.value = []
      selectedCode.value = null
      selectedChildCode.value = null
      childCodeList.value = []
    }
  })

  // 코드 선택 시 하위 코드 목록 조회
  watch(selectedCode, async (newCode) => {
    if (newCode) {
      selectedChildCode.value = null
      // codeList에서 선택된 code를 찾아서 groupCode를 가져옴
      const selectedCodeItem = codeList.value.find((item) => item.code === newCode)
      if (selectedCodeItem?.groupCode) {
        await fetchChildCodeList(selectedCodeItem.groupCode)
      } else {
        childCodeList.value = []
      }
    } else {
      childCodeList.value = []
      selectedChildCode.value = null
    }
  })

  // 코드 목록 조회
  const fetchCodeList = async (groupCode: string) => {
    if (!groupCode) return

    try {
      isLoadingCodes.value = true
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/${groupCode}/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 3,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data?.codes) {
        codeList.value = response.data.codes
      } else {
        alert(response.resultMessage || '코드 목록을 불러오는데 실패했습니다.')
      }
    } catch (error: unknown) {
      console.error('Fetch code list error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '코드 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoadingCodes.value = false
    }
  }

  // 하위 코드 목록 조회
  const fetchChildCodeList = async (code: string) => {
    if (!code) return

    try {
      isLoadingChildCodes.value = true
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/${code}/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 3,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data?.codes) {
        childCodeList.value = response.data.codes
      } else {
        alert(response.resultMessage || '하위 코드 목록을 불러오는데 실패했습니다.')
      }
    } catch (error: unknown) {
      console.error('Fetch child code list error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '하위 코드 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoadingChildCodes.value = false
    }
  }

  // 코드 추가 모드
  const startAddCode = () => {
    if (!selectedGroupCode.value) return

    isCodeEditMode.value = 'add'
    isChildCodeEditMode.value = null
    editingCode.value = null
    editingChildCode.value = null
    resetCodeForm()
    setCodeValues({
      groupCode: selectedGroupCode.value,
      code: '',
      codeName: '',
      childGroupCode: '',
      sortOrder: 0,
      isActive: true,
      description: '',
    })
  }

  // 코드 선택 핸들러
  const handleCodeSelect = (code: CommonCodeResponse) => {
    selectedCode.value = code.code || null
    // 선택 시 자동으로 수정 모드로 진입하지 않음 (명시적으로 수정 버튼 클릭 필요)
  }

  // 코드 수정 모드
  const startEditCode = (code: CommonCodeResponse) => {
    selectedCode.value = code.code || null
    isCodeEditMode.value = 'edit'
    isChildCodeEditMode.value = null
    editingCode.value = code
    editingChildCode.value = null
    resetCodeForm()
    setCodeValues({
      groupCode: selectedGroupCode.value || '',
      code: code.code || '',
      codeName: code.codeName || '',
      childGroupCode: code.groupCode || '',
      sortOrder: code.sortOrder || 0,
      isActive: true,
      description: code.description || '',
    })
  }

  // 하위 코드 추가 모드
  const startAddChildCode = () => {
    if (!selectedCode.value) return

    // codeList에서 선택된 code를 찾아서 groupCode를 가져옴
    const selectedCodeItem = codeList.value.find((item) => item.code === selectedCode.value)
    if (!selectedCodeItem?.groupCode) {
      alert('하위 코드를 추가할 수 없습니다. (groupCode가 없습니다)')
      return
    }

    isChildCodeEditMode.value = 'add'
    isCodeEditMode.value = null
    editingCode.value = null
    editingChildCode.value = null
    resetCodeForm()

    setCodeValues({
      groupCode: selectedCodeItem.groupCode,
      code: '',
      codeName: '',
      childGroupCode: '',
      sortOrder: 0,
      isActive: true,
      description: '',
    })
  }

  // 하위 코드 선택 핸들러
  const handleChildCodeSelect = (code: CommonCodeResponse) => {
    selectedChildCode.value = code.code || null
    // 선택 시 자동으로 수정 모드로 진입하지 않음 (명시적으로 수정 버튼 클릭 필요)
  }

  // 하위 코드 수정 모드
  const startEditChildCode = (code: CommonCodeResponse) => {
    if (!selectedCode.value) return

    selectedChildCode.value = code.code || null

    // codeList에서 선택된 code를 찾아서 groupCode를 가져옴
    const selectedCodeItem = codeList.value.find((item) => item.code === selectedCode.value)
    if (!selectedCodeItem?.groupCode) {
      alert('하위 코드를 수정할 수 없습니다. (groupCode가 없습니다)')
      return
    }

    isChildCodeEditMode.value = 'edit'
    isCodeEditMode.value = null
    editingCode.value = null
    editingChildCode.value = code
    resetCodeForm()

    setCodeValues({
      groupCode: selectedCodeItem.groupCode,
      code: code.code || '',
      codeName: code.codeName || '',
      childGroupCode: code.groupCode || '',
      sortOrder: code.sortOrder || 0,
      isActive: true,
      description: code.description || '',
    })
  }

  // 코드 편집 취소
  const cancelCodeEdit = () => {
    isCodeEditMode.value = null
    isChildCodeEditMode.value = null
    editingCode.value = null
    editingChildCode.value = null
    resetCodeForm()
  }

  // 공통코드 생성
  const createCommonCode = async (data: CommonCodeCreateRequest) => {
    try {
      const response = await $fetch<{ resultCode?: number; resultMessage?: string }>(
        `${API_BASE_URL}/common-codes`,
        {
          method: 'POST',
          credentials: 'include',
          body: data,
        },
      )

      if (response.resultCode === 200) {
        return true
      } else {
        alert(response.resultMessage || '공통코드 생성에 실패했습니다.')
        return false
      }
    } catch (error: unknown) {
      console.error('Create common code error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '공통코드 생성 중 오류가 발생했습니다.'
      alert(errorMessage)
      return false
    }
  }

  // 공통코드 삭제
  const deleteCommonCode = async (groupCode: string, code: string) => {
    if (!confirm('정말 삭제하시겠습니까?')) {
      return false
    }

    try {
      const response = await $fetch<{ resultCode?: number; resultMessage?: string }>(
        `${API_BASE_URL}/common-codes/${groupCode}/codes/${code}`,
        {
          method: 'DELETE',
          credentials: 'include',
        },
      )

      if (response.resultCode === 200) {
        return true
      } else {
        alert(response.resultMessage || '공통코드 삭제에 실패했습니다.')
        return false
      }
    } catch (error: unknown) {
      console.error('Delete common code error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '공통코드 삭제 중 오류가 발생했습니다.'
      alert(errorMessage)
      return false
    }
  }

  // 코드 제출
  const onCodeSubmit = handleCodeSubmit(async (formValues) => {
    if (isSubmittingCode.value) return

    try {
      isSubmittingCode.value = true

      if (isCodeEditMode.value === 'add') {
        const requestData: CommonCodeCreateRequest = {
          groupCode: formValues.groupCode,
          code: formValues.code,
          codeName: formValues.codeName,
          sortOrder: formValues.sortOrder,
          isActive: formValues.isActive,
          childGroupCode: formValues.childGroupCode || undefined,
          description: formValues.description || undefined,
        }

        const success = await createCommonCode(requestData)
        if (success) {
          await fetchCodeList(selectedGroupCode.value || '')
          cancelCodeEdit()
          alert('공통코드가 생성되었습니다.')
        }
      } else if (isCodeEditMode.value === 'edit' && editingCode.value) {
        // 수정은 삭제 후 생성으로 처리
        const oldGroupCode = selectedGroupCode.value
        const oldCode = editingCode.value.code

        if (!oldGroupCode || !oldCode) {
          alert('수정할 수 없습니다.')
          return
        }

        const deleteSuccess = await deleteCommonCode(oldGroupCode, oldCode)
        if (!deleteSuccess) {
          return
        }

        const requestData: CommonCodeCreateRequest = {
          groupCode: formValues.groupCode,
          code: formValues.code,
          codeName: formValues.codeName,
          sortOrder: formValues.sortOrder,
          isActive: formValues.isActive,
          childGroupCode: formValues.childGroupCode || undefined,
          description: formValues.description || undefined,
        }

        const createSuccess = await createCommonCode(requestData)
        if (createSuccess) {
          await fetchCodeList(selectedGroupCode.value || '')
          cancelCodeEdit()
          alert('공통코드가 수정되었습니다.')
        }
      } else if (isChildCodeEditMode.value === 'add') {
        if (!selectedCode.value) {
          alert('코드를 선택해주세요.')
          return
        }

        const selectedCodeItem = codeList.value.find((item) => item.code === selectedCode.value)
        if (!selectedCodeItem?.groupCode) {
          alert('하위 코드를 추가할 수 없습니다.')
          return
        }

        const requestData: CommonCodeCreateRequest = {
          groupCode: formValues.groupCode,
          code: formValues.code,
          codeName: formValues.codeName,
          sortOrder: formValues.sortOrder,
          isActive: formValues.isActive,
          childGroupCode: formValues.childGroupCode || undefined,
          description: formValues.description || undefined,
        }

        const success = await createCommonCode(requestData)
        if (success) {
          await fetchChildCodeList(selectedCodeItem.groupCode)
          cancelCodeEdit()
          alert('하위 코드가 생성되었습니다.')
        }
      } else if (isChildCodeEditMode.value === 'edit' && editingChildCode.value) {
        if (!selectedCode.value) {
          alert('수정할 수 없습니다.')
          return
        }

        const selectedCodeItem = codeList.value.find((item) => item.code === selectedCode.value)
        if (!selectedCodeItem?.groupCode) {
          alert('수정할 수 없습니다.')
          return
        }

        // 수정은 삭제 후 생성으로 처리
        const oldGroupCode = selectedCodeItem.groupCode
        const oldCode = editingChildCode.value.code

        if (!oldCode) {
          alert('수정할 수 없습니다.')
          return
        }

        const deleteSuccess = await deleteCommonCode(oldGroupCode, oldCode)
        if (!deleteSuccess) {
          return
        }

        const requestData: CommonCodeCreateRequest = {
          groupCode: formValues.groupCode,
          code: formValues.code,
          codeName: formValues.codeName,
          sortOrder: formValues.sortOrder,
          isActive: formValues.isActive,
          childGroupCode: formValues.childGroupCode || undefined,
          description: formValues.description || undefined,
        }

        const createSuccess = await createCommonCode(requestData)
        if (createSuccess) {
          await fetchChildCodeList(selectedCodeItem.groupCode)
          cancelCodeEdit()
          alert('하위 코드가 수정되었습니다.')
        }
      }
    } catch (error) {
      console.error('Code submit error:', error)
    } finally {
      isSubmittingCode.value = false
    }
  })

  // 코드 삭제
  const handleDeleteCode = async (code: CommonCodeResponse) => {
    if (!selectedGroupCode.value || !code.code) {
      alert('삭제할 수 없습니다.')
      return
    }

    const success = await deleteCommonCode(selectedGroupCode.value, code.code)
    if (success) {
      await fetchCodeList(selectedGroupCode.value)
      if (editingCode.value?.code === code.code) {
        cancelCodeEdit()
      }
      alert('공통코드가 삭제되었습니다.')
    }
  }

  // 하위 코드 삭제
  const handleDeleteChildCode = async (code: CommonCodeResponse) => {
    if (!selectedCode.value || !code.code) {
      alert('삭제할 수 없습니다.')
      return
    }

    // codeList에서 선택된 code를 찾아서 groupCode를 가져옴
    const selectedCodeItem = codeList.value.find((item) => item.code === selectedCode.value)
    if (!selectedCodeItem?.groupCode) {
      alert('삭제할 수 없습니다.')
      return
    }

    const success = await deleteCommonCode(selectedCodeItem.groupCode, code.code)
    if (success) {
      await fetchChildCodeList(selectedCodeItem.groupCode)
      if (editingChildCode.value?.code === code.code) {
        cancelCodeEdit()
      }
      alert('하위 코드가 삭제되었습니다.')
    }
  }

  // 페이지 마운트 시 초기 데이터 로드
  onMounted(async () => {
    await fetchRootGroups()
  })
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="공통코드 관리"
      image-src="/main_banner.png"
      image-alt="공통코드 관리 페이지 헤더 이미지"
      type="infoPage"
    />

    <div class="bg-neutral-fill-50 min-h-screen p-8">
      <div class="mx-auto max-w-[1400px]">
        <!-- 최상위 그룹 목록 섹션 -->
        <div class="mb-6">
          <h2 class="text-neutral-text-title mb-4 text-xl font-bold">
            최상위 그룹 목록
          </h2>
          <div class="grid gap-6 lg:grid-cols-3">
            <!-- 최상위 그룹 카드 리스트 -->
            <div class="border-neutral-stroke-300 rounded-lg border bg-white p-6">
              <div class="mb-4 flex items-center justify-between">
                <h3 class="text-neutral-text-title text-base font-bold">최상위 그룹 목록</h3>
                <Button
                  color="primary"
                  size="sm"
                  @click="startAddRootGroup"
                >
                  추가
                </Button>
              </div>
              <div class="space-y-2">
                <div
                  v-for="group in rootGroups"
                  :key="group.id"
                  class="border-neutral-stroke-200 rounded border p-3 hover:bg-neutral-fill-50 cursor-pointer"
                  :class="{
                    'bg-primary-50 border-primary-300':
                      isRootGroupEditMode === 'edit' &&
                      selectedRootGroupForEdit?.id === group.id,
                  }"
                  @click="startEditRootGroup(group)"
                >
                  <div>
                    <div class="text-neutral-text-title font-semibold">
                      {{ group.groupName }}
                    </div>
                    <div class="text-neutral-text-caption text-xs">
                      {{ group.groupCode }}
                    </div>
                  </div>
                </div>
                <div
                  v-if="rootGroups.length === 0"
                  class="text-neutral-text-caption py-8 text-center text-sm"
                >
                  최상위 그룹 목록이 없습니다.
                </div>
              </div>
            </div>

            <!-- 최상위 그룹 편집 영역 -->
            <div
              v-if="isRootGroupEditMode"
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-2"
            >
              <h3 class="text-neutral-text-title mb-4 text-base font-bold">
                {{ isRootGroupEditMode === 'add' ? '최상위 그룹 추가' : '최상위 그룹 수정' }}
              </h3>
              <form @submit.prevent="onRootGroupSubmit">
                <div class="space-y-4">
                  <FormField name="groupCode">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="그룹 코드"
                        placeholder="그룹 코드를 입력하세요"
                        size="md"
                        :readonly="isRootGroupEditMode === 'edit'"
                      />
                    </template>
                  </FormField>

                  <FormField name="groupName">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="그룹명"
                        placeholder="그룹명을 입력하세요"
                        size="md"
                      />
                    </template>
                  </FormField>

                  <FormField name="sortOrder">
                    <template #default="{ bind, field }">
                      <NumberInput
                        :model-value="field.value"
                        v-bind="bind"
                        label="정렬 순서"
                        placeholder="정렬 순서를 입력하세요"
                        size="md"
                        :min="0"
                      />
                    </template>
                  </FormField>

                  <FormField name="description">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="설명"
                        placeholder="설명을 입력하세요 (선택)"
                        size="md"
                      />
                    </template>
                  </FormField>
                </div>

                <div class="mt-6 flex justify-end gap-3">
                  <Button
                    type="button"
                    color="secondary"
                    size="md"
                    @click="cancelRootGroupEdit"
                  >
                    취소
                  </Button>
                  <Button
                    type="submit"
                    color="primary"
                    size="md"
                    :disabled="isSubmittingRootGroup"
                  >
                    저장
                  </Button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <!-- 그룹별 코드 관리 섹션 -->
        <div class="mb-6">
          <h2 class="text-neutral-text-title mb-4 text-xl font-bold">
            그룹별 코드 관리
          </h2>
          <div class="grid gap-6 lg:grid-cols-3">
            <!-- 왼쪽 영역: 그룹 선택 및 코드 목록 -->
            <div class="lg:col-span-2 border-neutral-stroke-300 rounded-lg border bg-white p-6">
              <!-- 그룹 선택 -->
              <div class="mb-4">
                <label class="text-neutral-text-title mb-2 block text-sm font-semibold">
                  그룹 선택
                </label>
                <SelectV2
                  v-model="selectedGroupCode"
                  :items="rootGroups"
                  item-text="groupName"
                  item-value="groupCode"
                  placeholder="그룹을 선택하세요"
                  size="md"
                  clearable
                />
              </div>

              <!-- 코드 선택 -->
              <div v-if="selectedGroupCode" class="mb-4">
                <div class="mb-2 flex items-center justify-between">
                  <label class="text-neutral-text-title block text-sm font-semibold">
                    코드 선택
                  </label>
                  <Button
                    color="primary"
                    size="xs"
                    @click="startAddCode"
                  >
                    추가
                  </Button>
                </div>
                <div
                  v-if="isLoadingCodes"
                  class="text-neutral-text-caption py-4 text-center text-sm"
                >
                  로딩 중...
                </div>
                <div
                  v-else-if="codeList.length > 0"
                  class="space-y-1"
                >
                  <div
                    v-for="item in codeList"
                    :key="item.code"
                    class="border-neutral-stroke-200 flex items-center justify-between rounded border p-2 hover:bg-neutral-fill-50 cursor-pointer"
                    :class="{
                      'bg-primary-50 border-primary-300':
                        selectedCode === item.code ||
                        (isCodeEditMode === 'edit' && editingCode?.code === item.code),
                    }"
                    @click="handleCodeSelect(item)"
                  >
                    <div>
                      <div class="text-neutral-text-title text-sm font-semibold">
                        {{ item.codeName }}
                      </div>
                      <div class="text-neutral-text-caption text-xs">
                        {{ item.code }}
                      </div>
                    </div>
                    <div class="flex gap-2">
                      <Button
                        color="primary"
                        size="xs"
                        variant="outlined"
                        @click.stop="startEditCode(item)"
                      >
                        수정
                      </Button>
                      <Button
                        color="warning"
                        size="xs"
                        variant="outlined"
                        @click.stop="handleDeleteCode(item)"
                      >
                        삭제
                      </Button>
                    </div>
                  </div>
                </div>
                <div
                  v-else
                  class="text-neutral-text-caption py-4 text-center text-sm"
                >
                  코드 목록이 없습니다.
                </div>
              </div>

              <!-- 하위 코드 선택 -->
              <div v-if="selectedCode" class="mb-4">
                <div class="mb-2 flex items-center justify-between">
                  <label class="text-neutral-text-title block text-sm font-semibold">
                    하위 코드 선택
                  </label>
                  <Button
                    color="primary"
                    size="xs"
                    @click="startAddChildCode"
                  >
                    추가
                  </Button>
                </div>
                <div
                  v-if="isLoadingChildCodes"
                  class="text-neutral-text-caption py-4 text-center text-sm"
                >
                  로딩 중...
                </div>
                <div
                  v-else-if="childCodeList.length > 0"
                  class="space-y-1"
                >
                  <div
                    v-for="item in childCodeList"
                    :key="item.code"
                    class="border-neutral-stroke-200 flex items-center justify-between rounded border p-2 hover:bg-neutral-fill-50 cursor-pointer"
                    :class="{
                      'bg-primary-50 border-primary-300':
                        selectedChildCode === item.code ||
                        (isChildCodeEditMode === 'edit' && editingChildCode?.code === item.code),
                    }"
                    @click="handleChildCodeSelect(item)"
                  >
                    <div>
                      <div class="text-neutral-text-title text-sm font-semibold">
                        {{ item.codeName }}
                      </div>
                      <div class="text-neutral-text-caption text-xs">
                        {{ item.code }}
                      </div>
                    </div>
                    <div class="flex gap-2">
                      <Button
                        color="primary"
                        size="xs"
                        variant="outlined"
                        @click.stop="startEditChildCode(item)"
                      >
                        수정
                      </Button>
                      <Button
                        color="warning"
                        size="xs"
                        variant="outlined"
                        @click.stop="handleDeleteChildCode(item)"
                      >
                        삭제
                      </Button>
                    </div>
                  </div>
                </div>
                <div
                  v-else
                  class="text-neutral-text-caption py-4 text-center text-sm"
                >
                  하위 코드 목록이 없습니다.
                </div>
              </div>
            </div>

            <!-- 오른쪽 영역: 코드 편집 -->
            <div
              v-if="(isCodeEditMode || isChildCodeEditMode) && selectedGroupCode"
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-1"
            >
              <h3 class="text-neutral-text-title mb-4 text-base font-bold">
                {{
                  isCodeEditMode === 'add'
                    ? '코드 추가'
                    : isCodeEditMode === 'edit'
                      ? '코드 수정'
                      : isChildCodeEditMode === 'add'
                        ? '하위 코드 추가'
                        : '하위 코드 수정'
                }}
              </h3>
              <form @submit.prevent="onCodeSubmit">
                <div class="space-y-4">
                  <FormField name="groupCode">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="그룹 코드"
                        placeholder="그룹 코드를 입력하세요"
                        size="md"
                        :readonly="isChildCodeEditMode !== null"
                      />
                    </template>
                  </FormField>

                  <FormField name="code">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="코드"
                        placeholder="코드를 입력하세요"
                        size="md"
                        :readonly="isCodeEditMode === 'edit' || isChildCodeEditMode === 'edit'"
                      />
                    </template>
                  </FormField>

                  <FormField name="codeName">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="코드명"
                        placeholder="코드명을 입력하세요"
                        size="md"
                      />
                    </template>
                  </FormField>

                  <FormField name="childGroupCode">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="하위 그룹 코드"
                        placeholder="하위 그룹 코드를 입력하세요 (선택)"
                        size="md"
                      />
                    </template>
                  </FormField>

                  <FormField name="sortOrder">
                    <template #default="{ bind, field }">
                      <NumberInput
                        :model-value="field.value"
                        v-bind="bind"
                        label="정렬 순서"
                        placeholder="정렬 순서를 입력하세요"
                        size="md"
                        :min="0"
                      />
                    </template>
                  </FormField>

                  <FormField name="isActive">
                    <template #default="{ bind, field }">
                      <div>
                        <label class="text-neutral-text-title mb-2 block text-sm font-semibold">
                          활성 상태
                        </label>
                        <Switch
                          :model-value="field.value"
                          v-bind="bind"
                          label="활성"
                        />
                      </div>
                    </template>
                  </FormField>

                  <FormField name="description">
                    <template #default="{ bind, field }">
                      <TextField
                        :model-value="field.value"
                        v-bind="bind"
                        label="설명"
                        placeholder="설명을 입력하세요 (선택)"
                        size="md"
                      />
                    </template>
                  </FormField>
                </div>

                <div class="mt-6 flex justify-end gap-3">
                  <Button
                    type="button"
                    color="secondary"
                    size="md"
                    @click="cancelCodeEdit"
                  >
                    취소
                  </Button>
                  <Button
                    type="submit"
                    color="primary"
                    size="md"
                    :disabled="isSubmittingCode"
                  >
                    저장
                  </Button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
