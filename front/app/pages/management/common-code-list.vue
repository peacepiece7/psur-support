<script setup lang="ts">
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import type { CommonCodeResponse } from '~/types/models/CommonCodeResponse'
  import type { CommonCodeGroupSummaryResponse } from '~/types/models/CommonCodeGroupSummaryResponse'
  import type { CommonCodeCreateRequest } from '~/types/models/CommonCodeCreateRequest'
  import SelectV2 from '~/components/atoms/SelectV2.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import NumberInput from '~/components/atoms/NumberInput.vue'
  import Button from '~/components/atoms/Button.vue'
  import Switch from '~/components/atoms/Switch.vue'
  import FormField from '~/components/molecules/FormField.vue'
  import { useCommonCodeStore } from '~/stores/useCommonCodeStore'

  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)
  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  const store = useCommonCodeStore()

  // ========== 루트 그룹 폼 (vee-validate) ==========
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

  const startAddRootGroup = () => {
    store.actions.startAddRootGroup()
    resetRootGroupForm()
    setRootGroupValues({
      groupCode: '',
      groupName: '',
      description: '',
      sortOrder: 0,
    })
  }

  const startEditRootGroup = (group: CommonCodeGroupSummaryResponse) => {
    store.actions.startEditRootGroup(group)
    setRootGroupValues({
      groupCode: group.groupCode || '',
      groupName: group.groupName || '',
      description: group.description || '',
      sortOrder: group.sortOrder || 0,
    })
  }

  const cancelRootGroupEdit = () => {
    store.actions.cancelRootGroupEdit()
    resetRootGroupForm()
  }

  const onRootGroupSubmit = handleRootGroupSubmit(async (formValues) => {
    if (store.isSubmittingRootGroup) return
    try {
      store.actions.setSubmittingRootGroup(true)
      if (store.isRootGroupEditMode === 'add') {
        const success = await store.actions.createRootGroup({
          groupCode: formValues.groupCode,
          groupName: formValues.groupName,
          description: formValues.description,
          sortOrder: formValues.sortOrder,
        })
        if (success) {
          await store.actions.fetchRootGroups()
          cancelRootGroupEdit()
          alert('루트 그룹이 생성되었습니다.')
        }
      } else if (store.isRootGroupEditMode === 'edit') {
        if (!store.selectedRootGroupForEdit?.groupCode) {
          alert('수정할 수 없습니다.')
          return
        }
        alert('수정 기능은 아직 구현되지 않았습니다.')
      }
    } catch (error) {
      console.error('Root group submit error:', error)
    } finally {
      store.actions.setSubmittingRootGroup(false)
    }
  })

  // ========== 코드 폼 (vee-validate) ==========
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

  watch(
    () => store.selectedGroupCode,
    (newGroup) => {
      store.actions.onSelectedGroupCodeChange(newGroup ?? null)
    },
    { immediate: true },
  )
  watch(
    () => store.selectedCode,
    (newCode) => {
      store.actions.onSelectedCodeChange(newCode ?? null)
    },
    { immediate: true },
  )

  const startAddCode = () => {
    store.actions.startAddCode()
    if (!store.getters.selectedGroupCodeValue()) return
    resetCodeForm()
    setCodeValues({
      groupCode: store.getters.selectedGroupCodeValue() ?? '',
      code: '',
      codeName: '',
      childGroupCode: '',
      sortOrder: 0,
      isActive: true,
      description: '',
    })
  }

  const handleCodeSelect = (code: CommonCodeResponse) => {
    store.actions.setSelectedCode(code.code || null)
  }

  const startEditCode = (code: CommonCodeResponse) => {
    store.actions.startEditCode(code)
    resetCodeForm()
    setCodeValues({
      groupCode: store.selectedGroupCode?.groupCode ?? '',
      code: code.code || '',
      codeName: code.codeName || '',
      childGroupCode: code.groupCode || '',
      sortOrder: code.sortOrder || 0,
      isActive: true,
      description: code.description || '',
    })
  }

  const startAddChildCode = () => {
    store.actions.startAddChildCode()
    const item = store.getters.selectedCodeItem()
    if (!item?.groupCode) return
    resetCodeForm()
    setCodeValues({
      groupCode: item.groupCode,
      code: '',
      codeName: '',
      childGroupCode: '',
      sortOrder: 0,
      isActive: true,
      description: '',
    })
  }

  const handleChildCodeSelect = (code: CommonCodeResponse) => {
    store.actions.setSelectedChildCode(code.code || null)
  }

  const startEditChildCode = (code: CommonCodeResponse) => {
    store.actions.startEditChildCode(code)
    const item = store.getters.selectedCodeItem()
    if (!item?.groupCode) return
    resetCodeForm()
    setCodeValues({
      groupCode: item.groupCode,
      code: code.code || '',
      codeName: code.codeName || '',
      childGroupCode: code.groupCode || '',
      sortOrder: code.sortOrder || 0,
      isActive: true,
      description: code.description || '',
    })
  }

  const cancelCodeEdit = () => {
    store.actions.cancelCodeEdit()
    resetCodeForm()
  }

  const toCreateRequest = (
    formValues: CodeFormValues,
  ): CommonCodeCreateRequest => ({
    groupCode: formValues.groupCode,
    code: formValues.code,
    codeName: formValues.codeName,
    sortOrder: formValues.sortOrder,
    isActive: formValues.isActive,
    childGroupCode: formValues.childGroupCode || undefined,
    description: formValues.description || undefined,
  })

  const onCodeSubmit = handleCodeSubmit(async (formValues) => {
    if (store.isSubmittingCode) return
    try {
      store.actions.setSubmittingCode(true)
      if (store.isCodeEditMode === 'add') {
        const success = await store.actions.createCommonCode(
          toCreateRequest(formValues),
        )
        if (success) {
          const gc = store.getters.selectedGroupCodeValue()
          if (gc) await store.actions.fetchCodeList(gc)
          cancelCodeEdit()
          alert('공통코드가 생성되었습니다.')
        }
      } else if (store.isCodeEditMode === 'edit' && store.editingCode) {
        const oldGroupCode = store.getters.selectedGroupCodeValue()
        const oldCode = store.editingCode.code
        if (!oldGroupCode || !oldCode) {
          alert('수정할 수 없습니다.')
          return
        }
        const deleteSuccess = await store.actions.deleteCommonCode(
          oldGroupCode,
          oldCode,
        )
        if (!deleteSuccess) return
        const createSuccess = await store.actions.createCommonCode(
          toCreateRequest(formValues),
        )
        if (createSuccess) {
          await store.actions.fetchCodeList(oldGroupCode)
          cancelCodeEdit()
          alert('공통코드가 수정되었습니다.')
        }
      } else if (store.isChildCodeEditMode === 'add') {
        const item = store.getters.selectedCodeItem()
        if (!store.selectedCode || !item?.groupCode) {
          alert('코드를 선택해주세요.')
          return
        }
        const success = await store.actions.createCommonCode(
          toCreateRequest(formValues),
        )
        if (success) {
          await store.actions.fetchChildCodeList(item.groupCode)
          cancelCodeEdit()
          alert('하위 코드가 생성되었습니다.')
        }
      } else if (
        store.isChildCodeEditMode === 'edit' &&
        store.editingChildCode
      ) {
        const item = store.getters.selectedCodeItem()
        if (!store.selectedCode || !item?.groupCode) {
          alert('수정할 수 없습니다.')
          return
        }
        const oldGroupCode = item.groupCode
        const oldCode = store.editingChildCode.code
        if (!oldCode) {
          alert('수정할 수 없습니다.')
          return
        }
        const deleteSuccess = await store.actions.deleteCommonCode(
          oldGroupCode,
          oldCode,
        )
        if (!deleteSuccess) return
        const createSuccess = await store.actions.createCommonCode(
          toCreateRequest(formValues),
        )
        if (createSuccess) {
          await store.actions.fetchChildCodeList(oldGroupCode)
          cancelCodeEdit()
          alert('하위 코드가 수정되었습니다.')
        }
      }
    } catch (error) {
      console.error('Code submit error:', error)
    } finally {
      store.actions.setSubmittingCode(false)
    }
  })

  const handleDeleteCode = async (code: CommonCodeResponse) => {
    const groupCode = store.getters.selectedGroupCodeValue()
    if (!groupCode || !code.code) {
      alert('삭제할 수 없습니다.')
      return
    }
    const success = await store.actions.deleteCommonCode(groupCode, code.code)
    if (success) {
      await store.actions.fetchCodeList(groupCode)
      if (store.editingCode?.code === code.code) cancelCodeEdit()
      alert('공통코드가 삭제되었습니다.')
    }
  }

  const handleDeleteChildCode = async (code: CommonCodeResponse) => {
    const item = store.getters.selectedCodeItem()
    if (!store.selectedCode || !code.code || !item?.groupCode) {
      alert('삭제할 수 없습니다.')
      return
    }
    const success = await store.actions.deleteCommonCode(
      item.groupCode,
      code.code,
    )
    if (success) {
      await store.actions.fetchChildCodeList(item.groupCode)
      if (store.editingChildCode?.code === code.code) cancelCodeEdit()
      alert('하위 코드가 삭제되었습니다.')
    }
  }

  onMounted(() => {
    store.actions.fetchRootGroups()
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
            <div
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6"
            >
              <div class="mb-4 flex items-center justify-between">
                <h3 class="text-neutral-text-title text-base font-bold">
                  최상위 그룹 목록
                </h3>
                <Button color="primary" size="sm" @click="startAddRootGroup">
                  추가
                </Button>
              </div>
              <div class="space-y-2">
                <div
                  v-for="group in store.rootGroups"
                  :key="group.id"
                  class="border-neutral-stroke-200 hover:bg-neutral-fill-50 cursor-pointer rounded border p-3"
                  :class="{
                    'bg-primary-50 border-primary-300':
                      store.isRootGroupEditMode === 'edit' &&
                      store.selectedRootGroupForEdit?.id === group.id,
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
                  v-if="store.rootGroups.length === 0"
                  class="text-neutral-text-caption py-8 text-center text-sm"
                >
                  최상위 그룹 목록이 없습니다.
                </div>
              </div>
            </div>

            <!-- 최상위 그룹 편집 영역 -->
            <div
              v-if="store.isRootGroupEditMode"
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-2"
            >
              <h3 class="text-neutral-text-title mb-4 text-base font-bold">
                {{
                  store.isRootGroupEditMode === 'add'
                    ? '최상위 그룹 추가'
                    : '최상위 그룹 수정'
                }}
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
                        :readonly="store.isRootGroupEditMode === 'edit'"
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
                    :disabled="store.isSubmittingRootGroup"
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
            <div
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-2"
            >
              <!-- 그룹 선택 -->
              <div class="mb-4">
                <label
                  class="text-neutral-text-title mb-2 block text-sm font-semibold"
                >
                  그룹 선택
                </label>
                <SelectV2
                  :model-value="store.selectedGroupCode"
                  @update:model-value="
                    store.actions.setSelectedGroupCode($event)
                  "
                  :items="store.rootGroups"
                  item-text="groupName"
                  item-value="groupCode"
                  placeholder="그룹을 선택하세요"
                  size="md"
                  clearable
                />
              </div>

              <!-- 코드 선택 -->
              <div v-if="store.selectedGroupCode" class="mb-4">
                <div class="mb-2 flex items-center justify-between">
                  <label
                    class="text-neutral-text-title block text-sm font-semibold"
                  >
                    코드 선택
                  </label>
                  <Button color="primary" size="xs" @click="startAddCode">
                    추가
                  </Button>
                </div>
                <div
                  v-if="store.isLoadingCodes"
                  class="text-neutral-text-caption py-4 text-center text-sm"
                >
                  로딩 중...
                </div>
                <div v-else-if="store.codeList.length > 0" class="space-y-1">
                  <div
                    v-for="item in store.codeList"
                    :key="item.code"
                    class="border-neutral-stroke-200 hover:bg-neutral-fill-50 flex cursor-pointer items-center justify-between rounded border p-2"
                    :class="{
                      'bg-primary-50 border-primary-300':
                        store.selectedCode === item.code ||
                        (store.isCodeEditMode === 'edit' &&
                          store.editingCode?.code === item.code),
                    }"
                    @click="handleCodeSelect(item)"
                  >
                    <div>
                      <div
                        class="text-neutral-text-title text-sm font-semibold"
                      >
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
              <div v-if="store.selectedCode" class="mb-4">
                <div class="mb-2 flex items-center justify-between">
                  <label
                    class="text-neutral-text-title block text-sm font-semibold"
                  >
                    하위 코드 선택
                  </label>
                  <Button color="primary" size="xs" @click="startAddChildCode">
                    추가
                  </Button>
                </div>
                <div
                  v-if="store.isLoadingChildCodes"
                  class="text-neutral-text-caption py-4 text-center text-sm"
                >
                  로딩 중...
                </div>
                <div
                  v-else-if="store.childCodeList.length > 0"
                  class="space-y-1"
                >
                  <div
                    v-for="item in store.childCodeList"
                    :key="item.code"
                    class="border-neutral-stroke-200 hover:bg-neutral-fill-50 flex cursor-pointer items-center justify-between rounded border p-2"
                    :class="{
                      'bg-primary-50 border-primary-300':
                        store.selectedChildCode === item.code ||
                        (store.isChildCodeEditMode === 'edit' &&
                          store.editingChildCode?.code === item.code),
                    }"
                    @click="handleChildCodeSelect(item)"
                  >
                    <div>
                      <div
                        class="text-neutral-text-title text-sm font-semibold"
                      >
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
              v-if="
                (store.isCodeEditMode || store.isChildCodeEditMode) &&
                store.selectedGroupCode
              "
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-1"
            >
              <h3 class="text-neutral-text-title mb-4 text-base font-bold">
                {{
                  store.isCodeEditMode === 'add'
                    ? '코드 추가'
                    : store.isCodeEditMode === 'edit'
                      ? '코드 수정'
                      : store.isChildCodeEditMode === 'add'
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
                        :readonly="store.isChildCodeEditMode !== null"
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
                        :readonly="
                          store.isCodeEditMode === 'edit' ||
                          store.isChildCodeEditMode === 'edit'
                        "
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
                        <label
                          class="text-neutral-text-title mb-2 block text-sm font-semibold"
                        >
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
                    :disabled="store.isSubmittingCode"
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
