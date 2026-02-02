<script setup lang="ts">
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import TextField from '~/components/atoms/TextField.vue'
  import NumberInput from '~/components/atoms/NumberInput.vue'
  import Button from '~/components/atoms/Button.vue'
  import FormField from '~/components/molecules/FormField.vue'

  const commonCodeStore = useCommonCodeStore()

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

  const { handleSubmit, resetForm, setValues } = useForm<RootGroupFormValues>({
    validationSchema: toTypedSchema(rootGroupFormSchema),
    initialValues: {
      groupCode: '',
      groupName: '',
      description: '',
      sortOrder: 0,
    },
  })

  watch(
    () => commonCodeStore.isRootGroupEditMode,
    (mode) => {
      if (mode === 'add') {
        resetForm()
        setValues({
          groupCode: '',
          groupName: '',
          description: '',
          sortOrder: 0,
        })
      } else if (mode === 'edit') {
        const g = commonCodeStore.selectedRootGroupForEdit
        if (g) {
          setValues({
            groupCode: g.groupCode || '',
            groupName: g.groupName || '',
            description: g.description || '',
            sortOrder: g.sortOrder || 0,
          })
        }
      }
    },
    { immediate: true },
  )

  watch(
    () => commonCodeStore.selectedRootGroupForEdit,
    (g) => {
      if (commonCodeStore.isRootGroupEditMode === 'edit' && g) {
        setValues({
          groupCode: g.groupCode || '',
          groupName: g.groupName || '',
          description: g.description || '',
          sortOrder: g.sortOrder || 0,
        })
      }
    },
    { deep: true },
  )

  const handler = {
    add: async (formValues: RootGroupFormValues) => {
      try {
        const success = await commonCodeStore.actions.createRootGroup({
          groupCode: formValues.groupCode,
          groupName: formValues.groupName,
          description: formValues.description,
          sortOrder: formValues.sortOrder,
        })
        if (success) {
          await commonCodeStore.actions.fetchRootGroups()
          handler.cancel()
          alert('루트 그룹이 생성되었습니다.')
        }
      } catch (error) {
        console.error('Root group submit error:', error)
      } finally {
        commonCodeStore.actions.setSubmittingRootGroup(false)
      }
    },
    edit: async () => {
      try {
        if (!commonCodeStore.selectedRootGroupForEdit?.groupCode) {
          alert('수정할 수 없습니다.')
          return
        }
        alert('수정 기능은 아직 구현되지 않았습니다.')
      } catch (error) {
        console.error('Root group submit error:', error)
      } finally {
        commonCodeStore.actions.setSubmittingRootGroup(false)
      }
    },
    cancel: () => {
      commonCodeStore.actions.cancelRootGroupEdit()
      resetForm()
    },
  }

  const handleSubmitForm = handleSubmit(async (formValues) => {
    if (commonCodeStore.isSubmittingRootGroup) return
    commonCodeStore.actions.setSubmittingRootGroup(true)

    const mode = commonCodeStore.isRootGroupEditMode
    await handler[mode](formValues)
  })
</script>

<template>
  <div
    v-if="commonCodeStore.isRootGroupEditMode"
    class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-2"
  >
    <h3 class="text-neutral-text-title mb-4 text-base font-bold">
      {{
        commonCodeStore.isRootGroupEditMode === 'add'
          ? '최상위 그룹 추가'
          : '최상위 그룹 수정'
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
              :readonly="commonCodeStore.isRootGroupEditMode === 'edit'"
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
          @click="handler.cancel"
        >
          취소
        </Button>
        <Button
          v-if="commonCodeStore.isRootGroupEditMode == 'edit'"
          type="submit"
          color="primary"
          size="md"
          :disabled="commonCodeStore.isSubmittingRootGroup"
        >
          수정
        </Button>
        <Button
          v-if="commonCodeStore.isRootGroupEditMode == 'add'"
          type="submit"
          color="primary"
          size="md"
          :disabled="commonCodeStore.isSubmittingRootGroup"
        >
          추가
        </Button>
      </div>
    </form>
  </div>
</template>
