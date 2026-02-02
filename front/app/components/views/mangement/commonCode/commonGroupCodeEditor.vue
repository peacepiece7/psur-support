<script setup lang="ts">
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import type { CommonCodeCreateRequest } from '~/types/models/CommonCodeCreateRequest'
  import type { CommonCodeUpdateRequest } from '~/types/models/CommonCodeUpdateRequest'
  import TextField from '~/components/atoms/TextField.vue'
  import NumberInput from '~/components/atoms/NumberInput.vue'
  import Button from '~/components/atoms/Button.vue'
  import Switch from '~/components/atoms/Switch.vue'
  import FormField from '~/components/molecules/FormField.vue'

  const commonCodeStore = useCommonCodeStore()

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

  const { handleSubmit, resetForm, setValues } = codeForm

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

  const toUpdateRequest = (
    formValues: CodeFormValues,
  ): CommonCodeUpdateRequest => ({
    groupCode: formValues.groupCode,
    code: formValues.code,
    codeName: formValues.codeName,
    sortOrder: formValues.sortOrder,
    isActive: formValues.isActive,
    childGroupCode: formValues.childGroupCode || undefined,
    description: formValues.description || undefined,
  })

  const handler = {
    cancel: () => {
      commonCodeStore.actions.cancelCodeEdit()
      resetForm()
    },
    codeAdd: async (formValues: CodeFormValues) => {
      try {
        const success = await commonCodeStore.actions.createCommonCode(
          toCreateRequest(formValues),
        )
        if (success) {
          const gc = commonCodeStore.getters.selectedGroupCodeValue()
          if (gc) await commonCodeStore.actions.fetchCodeList(gc)
          handler.cancel()
          alert('공통코드가 생성되었습니다.')
        }
      } catch (error) {
        console.error('Code submit error:', error)
      }
    },
    codeEdit: async (formValues: CodeFormValues) => {
      try {
        const groupCode = commonCodeStore.getters.selectedGroupCodeValue()
        if (!groupCode) {
          alert('수정할 수 없습니다.')
          return
        }
        const success = await commonCodeStore.actions.updateCommonCode(
          toUpdateRequest(formValues),
        )
        if (success) {
          await commonCodeStore.actions.fetchCodeList(groupCode)
          handler.cancel()
          alert('공통코드가 수정되었습니다.')
        }
      } catch (error) {
        console.error('Code submit error:', error)
      }
    },
    childAdd: async (formValues: CodeFormValues) => {
      try {
        const item = commonCodeStore.getters.selectedCodeItem()
        if (!commonCodeStore.selectedCode || !item?.groupCode) {
          alert('코드를 선택해주세요.')
          return
        }
        const success = await commonCodeStore.actions.createCommonCode(
          toCreateRequest(formValues),
        )
        if (success) {
          await commonCodeStore.actions.fetchChildCodeList(item.groupCode)
          handler.cancel()
          alert('하위 코드가 생성되었습니다.')
        }
      } catch (error) {
        console.error('Code submit error:', error)
      }
    },
    childEdit: async (formValues: CodeFormValues) => {
      try {
        const item = commonCodeStore.getters.selectedCodeItem()
        if (!commonCodeStore.selectedCode || !item?.groupCode) {
          alert('수정할 수 없습니다.')
          return
        }
        const oldGroupCode = item.groupCode
        const oldCode = commonCodeStore.editingChildCode?.code
        if (!oldCode) {
          alert('수정할 수 없습니다.')
          return
        }
        const deleteSuccess = await commonCodeStore.actions.deleteCommonCode(
          oldGroupCode,
          oldCode,
        )
        if (!deleteSuccess) return
        const createSuccess = await commonCodeStore.actions.createCommonCode(
          toCreateRequest(formValues),
        )
        if (createSuccess) {
          await commonCodeStore.actions.fetchChildCodeList(oldGroupCode)
          handler.cancel()
          alert('하위 코드가 수정되었습니다.')
        }
      } catch (error) {
        console.error('Code submit error:', error)
      }
    },
  }

  watch(
    [
      () => commonCodeStore.isCodeEditMode,
      () => commonCodeStore.isChildCodeEditMode,
      () => commonCodeStore.editingCode,
      () => commonCodeStore.editingChildCode,
      () => commonCodeStore.selectedGroupCode,
    ],
    () => {
      const mode = commonCodeStore.isCodeEditMode
      const childMode = commonCodeStore.isChildCodeEditMode
      if (!mode && !childMode) return
      if (mode === 'add') {
        const gc = commonCodeStore.getters.selectedGroupCodeValue()
        setValues({
          groupCode: gc ?? '',
          code: '',
          codeName: '',
          childGroupCode: '',
          sortOrder: 0,
          isActive: true,
          description: '',
        })
      } else if (mode === 'edit' && commonCodeStore.editingCode) {
        const c = commonCodeStore.editingCode
        setValues({
          groupCode: commonCodeStore.selectedGroupCode?.groupCode ?? '',
          code: c.code || '',
          codeName: c.codeName || '',
          childGroupCode: c.groupCode || '',
          sortOrder: c.sortOrder || 0,
          isActive: true,
          description: c.description || '',
        })
      } else if (childMode === 'add') {
        const item = commonCodeStore.getters.selectedCodeItem()
        if (item?.groupCode) {
          setValues({
            groupCode: item.groupCode,
            code: '',
            codeName: '',
            childGroupCode: '',
            sortOrder: 0,
            isActive: true,
            description: '',
          })
        }
      } else if (childMode === 'edit' && commonCodeStore.editingChildCode) {
        const item = commonCodeStore.getters.selectedCodeItem()
        const c = commonCodeStore.editingChildCode
        if (item?.groupCode) {
          setValues({
            groupCode: item.groupCode,
            code: c.code || '',
            codeName: c.codeName || '',
            childGroupCode: c.groupCode || '',
            sortOrder: c.sortOrder || 0,
            isActive: true,
            description: c.description || '',
          })
        }
      }
    },
    { immediate: true, deep: true },
  )

  const handleSubmitForm = handleSubmit(async (formValues) => {
    if (commonCodeStore.isSubmittingCode) return
    commonCodeStore.actions.setSubmittingCode(true)
    try {
      if (commonCodeStore.isCodeEditMode === 'add') {
        await handler.codeAdd(formValues)
      } else if (commonCodeStore.isCodeEditMode === 'edit') {
        await handler.codeEdit(formValues)
      } else if (commonCodeStore.isChildCodeEditMode === 'add') {
        await handler.childAdd(formValues)
      } else if (commonCodeStore.isChildCodeEditMode === 'edit') {
        await handler.childEdit(formValues)
      }
    } finally {
      commonCodeStore.actions.setSubmittingCode(false)
    }
  })
</script>

<template>
  <div
    v-if="
      (commonCodeStore.isCodeEditMode || commonCodeStore.isChildCodeEditMode) &&
      commonCodeStore.selectedGroupCode
    "
    class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-1"
  >
    <h3 class="text-neutral-text-title mb-4 text-base font-bold">
      {{
        commonCodeStore.isCodeEditMode === 'add'
          ? '코드 추가'
          : commonCodeStore.isCodeEditMode === 'edit'
            ? '코드 수정'
            : commonCodeStore.isChildCodeEditMode === 'add'
              ? '하위 코드 추가'
              : '하위 코드 수정'
      }}
    </h3>
    <form @submit.prevent="handleSubmitForm">
      <div class="space-y-4">
        <FormField name="groupCode">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="그룹 코드"
              placeholder="그룹 코드를 입력하세요"
              size="md"
              :readonly="commonCodeStore.isChildCodeEditMode !== null"
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
                commonCodeStore.isCodeEditMode === 'edit' ||
                commonCodeStore.isChildCodeEditMode === 'edit'
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
              <Switch :model-value="field.value" v-bind="bind" label="활성" />
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
          @click="handler.cancel"
        >
          취소
        </Button>
        <Button
          type="submit"
          color="primary"
          size="md"
          :disabled="commonCodeStore.isSubmittingCode"
        >
          저장
        </Button>
      </div>
    </form>
  </div>
</template>
