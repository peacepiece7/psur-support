<script setup lang="ts">
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import FormField from '~/components/molecules/FormField.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import DatePicker from '~/components/atoms/DatePicker.vue'
  import Textarea from '~/components/atoms/Textarea.vue'
  import NumberInput from '~/components/atoms/NumberInput.vue'
  import SelectionGroup, {
    type GroupOption,
  } from '~/components/atoms/SelectionGroup.vue'

  // SelectionGroup 옵션 정의
  const businessTypeOptions: GroupOption<string>[] = [
    { label: '개인사업자', value: 'individual' },
    { label: '법인사업자', value: 'corporation' },
    { label: '비영리단체', value: 'nonprofit' },
  ]

  const serviceTypeOptions: GroupOption<string>[] = [
    { label: '축구', value: 'soccer' },
    { label: '농구', value: 'basketball' },
    { label: '야구', value: 'baseball' },
    { label: '배구', value: 'volleyball' },
    { label: '테니스', value: 'tennis' },
  ]

  // Yup 스키마 정의
  const formSchema = yup.object({
    corpOrgName: yup.string().required('⚠️ 법인 단체명을 입력해주세요'),
    representativeBirthDate: yup
      .string()
      .required('⚠️ 대표자 생년월일을 입력해주세요'),
    representativeName: yup.string().required('⚠️ 대표자명을 입력해주세요'),
    applicationReason: yup.string().required('⚠️ 신청이유를 입력해주세요'),
    operationMonths: yup
      .number()
      .required('⚠️ 운영 개월을 입력해주세요')
      .min(1, '⚠️ 최소 1개월 이상 입력해주세요')
      .integer('⚠️ 정수만 입력 가능합니다'),
    businessType: yup
      .string()
      .required('⚠️ 사업자 유형을 선택해주세요')
      .oneOf(
        ['individual', 'corporation', 'nonprofit'],
        '⚠️ 유효한 사업자 유형을 선택해주세요',
      ),
    serviceTypes: yup
      .array()
      .of(yup.string())
      .required('⚠️ 서비스 유형을 최소 1개 이상 선택해주세요')
      .min(1, '⚠️ 서비스 유형을 최소 1개 이상 선택해주세요'),
  })

  type FormValues = yup.InferType<typeof formSchema>

  // useForm 설정
  const { handleSubmit, values, resetForm } = useForm<FormValues>({
    validationSchema: toTypedSchema(formSchema),
    initialValues: {
      corpOrgName: '',
      representativeBirthDate: '',
      representativeName: '',
      applicationReason: '',
      operationMonths: undefined,
      businessType: null,
      serviceTypes: [],
    },
  })

  // Submit 핸들러
  const onSubmit = handleSubmit((formValues) => {
    console.log('Form submitted:', formValues)
    alert('폼 제출 성공!\n' + JSON.stringify(formValues, null, 2))
  })

  // Reset 핸들러
  const onReset = () => {
    resetForm()
  }
</script>

<template>
  <div class="min-h-screen bg-grey-50 p-8">
    <div class="mx-auto max-w-2xl">
      <h1 class="mb-8 text-3xl font-bold text-grey-900">
        FormField + useForm + Yup 예제
      </h1>

      <section
        class="min-h-[420px] rounded-3xl border border-grey-300 bg-white p-10"
      >
        <h2 class="m-0 mb-6 text-2xl font-bold">법인 정보 입력</h2>

        <form class="grid max-w-[720px] gap-4" @submit.prevent="onSubmit">
          <!-- 법인 단체명 -->
          <FormField name="corpOrgName">
            <template #default="{ bind, field }">
              <TextField
                :model-value="field.value"
                v-bind="bind"
                label="법인 단체명"
                placeholder="법인(단체)명을 입력하세요"
                size="md"
              />
            </template>
          </FormField>

          <!-- 대표자 생년월일 -->
          <FormField name="representativeBirthDate">
            <template #default="{ bind, field }">
              <DatePicker
                :model-value="field.value"
                v-bind="bind"
                label="대표자 생년월일"
              />
            </template>
          </FormField>

          <!-- 대표자명 -->
          <FormField name="representativeName">
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

          <!-- 신청이유 -->
          <FormField name="applicationReason">
            <template #default="{ bind, field }">
              <Textarea
                :model-value="field.value"
                v-bind="bind"
                label="신청이유"
                placeholder="신청이유를 입력해주세요"
                size="md"
                :rows="4"
              />
            </template>
          </FormField>

          <!-- 운영 개월 -->
          <FormField name="operationMonths">
            <template #default="{ bind, field }">
              <NumberInput
                :model-value="field.value"
                v-bind="bind"
                label="운영 개월"
                size="md"
                prefix="운영"
                suffix="개월"
                :min="1"
              />
            </template>
          </FormField>

          <!-- 사업자 유형 (Radio) -->
          <FormField name="businessType">
            <template #default="{ bind, field }">
              <SelectionGroup
                type="radio"
                :model-value="field.value"
                v-bind="bind"
                :items="businessTypeOptions"
                label="사업자 유형"
                direction="horizontal"
                color-set="primary"
              />
            </template>
          </FormField>

          <!-- 서비스 유형 (Checkbox) -->
          <FormField name="serviceTypes">
            <template #default="{ bind, field }">
              <SelectionGroup
                type="checkbox"
                :model-value="field.value"
                v-bind="bind"
                :items="serviceTypeOptions"
                label="서비스 유형"
                direction="horizontal"
                color-set="success"
              />
            </template>
          </FormField>

          <!-- 버튼 영역 -->
          <div class="mt-6 flex gap-4">
            <button
              type="submit"
              class="rounded-lg bg-blue-600 px-6 py-3 font-semibold text-white transition-colors hover:bg-blue-700"
            >
              제출하기
            </button>
            <button
              type="button"
              class="rounded-lg border border-grey-300 bg-white px-6 py-3 font-semibold text-grey-700 transition-colors hover:bg-grey-50"
              @click="onReset"
            >
              초기화
            </button>
          </div>
        </form>

        <!-- 디버깅용: 현재 폼 값 표시 -->
        <div class="mt-8 rounded-lg bg-grey-100 p-4">
          <h3 class="mb-2 text-sm font-semibold text-grey-700">
            현재 폼 값 (디버깅용):
          </h3>
          <pre class="text-xs text-grey-600">{{
            JSON.stringify(values, null, 2)
          }}</pre>
        </div>
      </section>
    </div>
  </div>
</template>
