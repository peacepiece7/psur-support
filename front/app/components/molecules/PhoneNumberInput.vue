<script setup lang="ts">
  import TextField from '~/components/atoms/TextField.vue'
  import type { TextFieldSize } from '~/components/atoms/TextField.vue'

  defineOptions({
    inheritAttrs: false,
  })

  export type PhoneNumberValue = {
    part1: string
    part2: string
    part3: string
  }

  export type PhoneNumberInputProps = {
    /**
     * v-model 값 (객체)
     * 예: { part1: "010", part2: "1234", part3: "5678" }
     */
    modelValue: PhoneNumberValue | null

    /** 입력 사이즈 (커스텀) */
    size?: Exclude<TextFieldSize, 'sm' | 'xs'>

    /** 비활성 */
    disabled?: boolean

    /** 읽기전용 */
    readonly?: boolean

    /** 앞자리 placeholder */
    placeholderPart1?: string
    /** 중간자리 placeholder */
    placeholderPart2?: string
    /** 뒷자리 placeholder */
    placeholderPart3?: string

    maxLengthPart1?: number
    maxLengthPart2?: number
    maxLengthPart3?: number

    /** 자동 다음 탭 이동 (maxLength 채울 시) */
    autoNextTab?: boolean

    /** 에러 상태 */
    error?: ComputedRef<boolean> | boolean
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string> | string
  }

  const props = withDefaults(defineProps<PhoneNumberInputProps>(), {
    size: 'md',
    disabled: false,
    readonly: false,
    placeholderPart1: '3자',
    placeholderPart2: '4자',
    placeholderPart3: '4자',
    maxLengthPart1: 3,
    maxLengthPart2: 4,
    maxLengthPart3: 4,
    autoNextTab: false,
    error: undefined,
    errorMessageId: undefined,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: PhoneNumberValue | null): void
    (e: 'blur'): void
  }>()

  const error = computed(() => unref(props.error))
  const errorMessageId = computed(() => unref(props.errorMessageId))

  // 각 TextField ref
  const part1Ref = ref<InstanceType<typeof TextField> | null>(null)
  const part2Ref = ref<InstanceType<typeof TextField> | null>(null)
  const part3Ref = ref<InstanceType<typeof TextField> | null>(null)

  // 현재 값 계산 (null이면 빈 객체)
  const currentValue = computed(() => {
    return (
      props.modelValue || {
        part1: '',
        part2: '',
        part3: '',
      }
    )
  })

  // 각 part의 computed 값
  const part1 = computed({
    get: () => currentValue.value?.part1 || '',
    set: (val: string) => {
      const newValue = {
        part1: val,
        part2: currentValue.value.part2,
        part3: currentValue.value.part3,
      }
      emit('update:modelValue', newValue)
    },
  })

  const part2 = computed({
    get: () => currentValue.value?.part2 || '',
    set: (val: string) => {
      const newValue = {
        part1: currentValue.value.part1,
        part2: val,
        part3: currentValue.value.part3,
      }
      emit('update:modelValue', newValue)
    },
  })

  const part3 = computed({
    get: () => currentValue.value?.part3 || '',
    set: (val: string) => {
      const newValue = {
        part1: currentValue.value.part1,
        part2: currentValue.value.part2,
        part3: val,
      }
      emit('update:modelValue', newValue)
    },
  })

  // 다음 input으로 포커스 이동
  const focusNextInput = (
    ref: typeof part1Ref | typeof part2Ref | typeof part3Ref,
  ) => {
    if (!props.autoNextTab) return

    nextTick(() => {
      if (!ref.value) return

      // TextField 내부의 input 요소 찾기
      const textFieldElement = ref.value.$el as HTMLElement
      const inputElement = textFieldElement.querySelector(
        'input',
      ) as HTMLInputElement

      if (inputElement) {
        inputElement.focus()
      }
    })
  }

  // 각 input의 입력 처리 (숫자만 허용, 최대 길이 제한)
  const handlePart1Input = (e: Event) => {
    const target = e.target as HTMLInputElement
    const value = target.value.replace(/\D/g, '')

    // maxLength 제한
    const limitedValue =
      value.length > props.maxLengthPart1
        ? value.slice(0, props.maxLengthPart1)
        : value

    part1.value = limitedValue

    // autoNextTab이 true이고 maxLength를 채웠을 때 다음 input으로 이동
    if (props.autoNextTab && limitedValue.length === props.maxLengthPart1) {
      focusNextInput(part2Ref)
    }
  }

  const handlePart2Input = (e: Event) => {
    const target = e.target as HTMLInputElement
    const value = target.value.replace(/\D/g, '')

    // maxLength 제한
    const limitedValue =
      value.length > props.maxLengthPart2
        ? value.slice(0, props.maxLengthPart2)
        : value

    part2.value = limitedValue

    // autoNextTab이 true이고 maxLength를 채웠을 때 다음 input으로 이동
    if (props.autoNextTab && limitedValue.length === props.maxLengthPart2) {
      focusNextInput(part3Ref)
    }
  }

  const handlePart3Input = (e: Event) => {
    const target = e.target as HTMLInputElement
    const value = target.value.replace(/\D/g, '')

    // maxLength 제한
    const limitedValue =
      value.length > props.maxLengthPart3
        ? value.slice(0, props.maxLengthPart3)
        : value

    part3.value = limitedValue
  }

  const handleBlur = () => {
    emit('blur')
  }
</script>

<template>
  <div
    class="phone-number-input flex items-center justify-between gap-2"
    :class="{
      'phone-input-size-md': size === 'md',
      'phone-input-size-lg': size === 'lg',
      'phone-input-size-xl': size === 'xl',
      'phone-input-size-max': size === 'max',
    }"
  >
    <!-- 앞자리 Input -->
    <div class="flex-1">
      <TextField
        ref="part1Ref"
        v-model="part1"
        :label="'전화번호 앞자리'"
        :class="{
          'has-error': error,
          error: error,
          'w-fit min-w-0': true,
        }"
        size="max"
        :disabled="disabled"
        :readonly="readonly"
        :placeholder="placeholderPart1"
        :max-length="maxLengthPart1"
        inputmode="numeric"
        type="text"
        :aria-label="'전화번호 앞자리'"
        :aria-invalid="error ? 'true' : undefined"
        :aria-describedby="errorMessageId ?? undefined"
        @input="handlePart1Input"
        @blur="handleBlur"
      />
    </div>
    <span>-</span>
    <!-- 중간자리 Input -->
    <div class="flex-1">
      <TextField
        ref="part2Ref"
        v-model="part2"
        :label="'전화번호 중간자리'"
        :class="{
          'has-error': error,
          error: error,
          'w-fit min-w-0': true,
        }"
        size="max"
        :disabled="disabled"
        :readonly="readonly"
        :placeholder="placeholderPart2"
        :max-length="maxLengthPart2"
        inputmode="numeric"
        type="text"
        :aria-label="'전화번호 중간자리'"
        :aria-invalid="error ? 'true' : undefined"
        :aria-describedby="errorMessageId ?? undefined"
        @input="handlePart2Input"
        @blur="handleBlur"
      />
    </div>
    <span>-</span>
    <!-- 뒷자리 Input -->
    <div class="flex-1">
      <TextField
        ref="part3Ref"
        v-model="part3"
        :label="'전화번호 뒷자리'"
        :class="{
          'has-error': error,
          error: error,
          'w-fit min-w-0': true,
        }"
        size="max"
        :disabled="disabled"
        :readonly="readonly"
        :placeholder="placeholderPart3"
        :max-length="maxLengthPart3"
        inputmode="numeric"
        type="text"
        :aria-label="'전화번호 뒷자리'"
        :aria-invalid="error ? 'true' : undefined"
        :aria-describedby="errorMessageId ?? undefined"
        @input="handlePart3Input"
        @blur="handleBlur"
      />
    </div>
  </div>
</template>

<style scoped>
  .phone-input-size-md {
    max-width: 280px;
  }

  .phone-input-size-lg {
    max-width: 360px;
  }

  .phone-input-size-xl {
    max-width: 480px;
  }

  .phone-input-size-max {
    max-width: 100%;
  }

  .phone-number-input :deep(label) {
    display: none !important;
  }
</style>
