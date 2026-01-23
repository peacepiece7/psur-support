<script setup lang="ts">
  import type { TextFieldSize } from '~/components/atoms/TextField.vue'

  defineOptions({
    inheritAttrs: false,
  })

  export type IDNumberValue = {
    part1: string
    part2: string
  }

  export type IDNumberInputProps = {
    /**
     * v-model 값 (객체)
     * 예: { part1: "123456", part2: "1234567" }
     */
    modelValue: IDNumberValue | null

    /** 입력 사이즈 (커스텀) */
    size?: Exclude<TextFieldSize, 'sm' | 'xs'>

    /** 비활성 */
    disabled?: boolean

    /** 읽기전용 */
    readonly?: boolean

    /** 앞자리 placeholder */
    placeholderPart1?: string
    /** 뒷자리 placeholder */
    placeholderPart2?: string

    /** 앞자리 aria-label */
    ariaLabelPart1?: string
    /** 뒷자리 aria-label */
    ariaLabelPart2?: string

    /**
     * 앞자리 최대 길이
     * 기본값: 6 (주민등록번호)
     */
    part1MaxLength?: number

    /**
     * 뒷자리 최대 길이
     * 기본값: 7 (주민등록번호)
     */
    part2MaxLength?: number

    /** 에러 상태 */
    error?: ComputedRef<boolean> | boolean
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string> | string
  }

  const props = withDefaults(defineProps<IDNumberInputProps>(), {
    size: 'md',
    disabled: false,
    readonly: false,
    placeholderPart1: '6자리',
    placeholderPart2: '7자리',
    ariaLabelPart1: '주민등록번호 앞자리',
    ariaLabelPart2: '주민등록번호 뒷자리',
    part1MaxLength: 6,
    part2MaxLength: 7,
    error: undefined,
    errorMessageId: undefined,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: IDNumberValue | null): void
    (e: 'blur'): void
  }>()

  const error = computed(() => unref(props.error))
  const errorMessageId = computed(() => unref(props.errorMessageId))

  // 현재 값 계산 (null이면 빈 객체)
  const currentValue = computed(() => {
    return (
      props.modelValue || {
        part1: '',
        part2: '',
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
      }
      emit('update:modelValue', newValue)
    },
  })

  // 마스킹 표시 여부 (눈 아이콘으로 토글)
  const showUnmasked = ref(false)

  // part2 input의 type (password 또는 text)
  const part2InputType = computed(() => {
    return showUnmasked.value ? 'text' : 'password'
  })

  // 눈 아이콘 토글
  const toggleMask = () => {
    showUnmasked.value = !showUnmasked.value
  }

  // 각 input의 입력 처리
  const handlePart1Input = (e: Event) => {
    const target = e.target as HTMLInputElement
    const value = target.value.replace(/\D/g, '')

    if (value.length >= props.part1MaxLength) {
      part1.value = value.slice(0, props.part1MaxLength)
    } else {
      part1.value = value
    }
  }

  // part2 input 입력 처리
  const handlePart2Input = (e: Event) => {
    const target = e.target as HTMLInputElement
    const value = target.value.replace(/\D/g, '')

    // 최대 길이 제한
    if (value.length > props.part2MaxLength) {
      part2.value = value.slice(0, props.part2MaxLength)
    } else {
      part2.value = value
    }
  }

  const handleBlur = () => {
    emit('blur')
  }
</script>

<template>
  <div
    class="id-number-input flex items-center justify-between gap-2"
    :class="{
      'id-input-size-md': size === 'md',
      'id-input-size-lg': size === 'lg',
      'id-input-size-xl': size === 'xl',
      'id-input-size-max': size === 'max',
    }"
  >
    <!-- 앞자리 Input -->
    <div class="flex-1">
      <TextField
        v-model="part1"
        :label="'특수번호 앞자리'"
        :class="{ 'has-error': error, error: error, 'w-fit min-w-0': true }"
        size="max"
        :disabled="disabled"
        :readonly="readonly"
        :placeholder="placeholderPart1"
        :maxlength="part1MaxLength"
        inputmode="numeric"
        autocomplete="off"
        type="text"
        :aria-label="ariaLabelPart1"
        :aria-invalid="error ? 'true' : undefined"
        :aria-describedby="errorMessageId ?? undefined"
        @input="handlePart1Input"
        @blur="handleBlur"
      />
    </div>
    <span>-</span>
    <!-- 뒷자리 Wrapper -->
    <div class="flex-1">
      <!-- 뒷자리 Input (password 또는 text) -->
      <TextField
        v-model="part2"
        :type="part2InputType"
        :label="'특수번호 뒷자리'"
        :class="{ 'has-error': error, error: error, 'has-suffix': true }"
        size="max"
        :disabled="disabled"
        :readonly="readonly"
        :placeholder="placeholderPart2"
        :maxlength="part2MaxLength"
        inputmode="numeric"
        autocomplete="off"
        :aria-label="ariaLabelPart2"
        :aria-invalid="error ? 'true' : undefined"
        :aria-describedby="errorMessageId ?? undefined"
        :append-inner-icon="showUnmasked ? 'local:eye-off' : 'local:eye-on'"
        @click:append-inner="toggleMask"
        @input="handlePart2Input"
        @blur="handleBlur"
      >
      </TextField>
    </div>
  </div>
</template>

<style scoped>
  .id-input-size-md {
    max-width: 280px;
  }

  .id-input-size-lg {
    max-width: 360px;
  }

  .id-input-size-xl {
    max-width: 480px;
  }

  .id-input-size-max {
    max-width: 100%;
  }

  .id-number-input :deep(label) {
    display: none !important;
  }
</style>
