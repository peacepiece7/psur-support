<script setup lang="ts">
  import type { VNumberInput } from 'vuetify/components'
  import { INPUT } from '~/constants/design'

  type VNumberInputProps = VNumberInput['$props']

  defineOptions({
    inheritAttrs: false,
  })

  export type NumberInputSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'max'

  export type NumberInputProps = {
    /**
     * v-model 값
     */
    modelValue: VNumberInputProps['modelValue'] | string

    /**
     * 입력 사이즈 (커스텀)
     * - 실제 width/height는 추후 클래스 주입 방식으로 확장 예정
     * - prefix/suffix가 있을 때는 전체 컨테이너(prefix + input + suffix)의 사이즈를 의미
     */
    size?: NumberInputSize

    /** (선택) width */
    width?: string

    /** 라벨 */
    label?: VNumberInputProps['label']
    /** placeholder */
    placeholder?: VNumberInputProps['placeholder']

    /** 비활성 */
    disabled?: VNumberInputProps['disabled']

    /** v-number-input options */
    reverse?: VNumberInputProps['reverse']
    inset?: VNumberInputProps['inset']
    /** prefix - numberInput 바깥 앞쪽에 표시됨 */
    prefix?: string
    /** suffix - numberInput 바깥 뒤쪽에 표시됨 */
    suffix?: string
    /** 최소값 */
    min?: VNumberInputProps['min']
    /** 최대값 */
    max?: VNumberInputProps['max']

    /** 에러 상태 */
    error?: ComputedRef<boolean> | boolean
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string> | string
  }

  const props = withDefaults(defineProps<NumberInputProps>(), {
    size: 'md',
    width: undefined,
    label: undefined,
    disabled: false,
    reverse: true,
    inset: false,
    placeholder: '입력해주세요',
    prefix: undefined,
    suffix: undefined,
    min: 0,
    max: undefined,
    error: undefined,
    errorMessageId: undefined,
  })

  const error = computed(() => unref(props.error))
  const errorMessageId = computed(() => unref(props.errorMessageId))

  const emit = defineEmits<{
    (e: 'update:modelValue', value: VNumberInputProps['modelValue']): void
  }>()

  const value = computed({
    get: () => (props.modelValue ? Number(props.modelValue) : undefined),
    set: (v: VNumberInputProps['modelValue']) =>
      emit('update:modelValue', v ? Number(v) : undefined),
  })

  // prefix 또는 suffix가 있는지 확인
  const hasExternalAffix = computed(() => {
    return !!(props.prefix || props.suffix)
  })

  // size 클래스 생성 (prefix/suffix가 있어도 사용자가 지정한 size 사용)
  const numberInputClass = computed(() => {
    return [`number-input-size-${props.size}`]
  })

  const numberInputWidth = computed(() => {
    return INPUT.width[props.size]
  })

  const attrs = useAttrs()

  const inputAttrs = computed(() => {
    const { class: _ignored, ...rest } = attrs
    return rest
  })

  const inputClass = computed(() => {
    return [
      'pp-v-number-input',
      // prefix 또는 suffix가 있으면 number-input 자체에는 size 클래스를 적용하지 않음 (컨테이너에 적용)
      hasExternalAffix.value ? '' : numberInputClass.value,
      {
        'has-error': error.value,
        error: error.value,
      },
      attrs.class,
    ]
  })

  const inputStyle = computed(() => {
    if (hasExternalAffix.value) {
      return { width: undefined }
    }
    const width = props.width ?? numberInputWidth.value
    return { width }
  })

  // prefix 또는 suffix가 있을 때 컨테이너 클래스
  const containerClass = computed(() => {
    return [
      'number-input-with-affix',
      hasExternalAffix.value ? numberInputClass.value : '',
    ]
  })
</script>

<template>
  <div class="grid gap-1">
    <!--
      NOTE:
      Vuetify의 floating label 구현은 label 요소에 aria-hidden을 붙이는 케이스가 있어
      일부 WCAG 검사기에서 에러로 잡힙니다. 내부 label을 사용하지 않고, 외부 텍스트로 노출합니다.
    -->
    <label
      v-if="props.label"
      class="text-grey-900 text-sm leading-[1.2] font-bold"
    >
      {{ props.label }}
    </label>

    <template v-if="props.prefix || props.suffix">
      <div :class="containerClass" class="flex items-center gap-2">
        <div
          v-if="props.prefix"
          class="number-input-prefix text-grey-900 flex h-12 items-center text-sm font-medium"
        >
          {{ props.prefix }}
        </div>

        <v-number-input
          v-model="value"
          :class="inputClass"
          :style="inputStyle"
          :placeholder="props.placeholder"
          :disabled="props.disabled"
          :reverse="props.reverse"
          variant="solo"
          density="compact"
          control-variant="hidden"
          :inset="props.inset"
          :min="props.min"
          :max="props.max"
          :aria-invalid="error ? 'true' : undefined"
          :aria-describedby="errorMessageId ?? undefined"
          autocomplete="off"
          v-bind="inputAttrs"
        >
          <template v-for="(_, name) in $slots" #[name]="slotProps">
            <slot :name="name" v-bind="slotProps" />
          </template>
        </v-number-input>

        <div
          v-if="props.suffix"
          class="number-input-suffix text-grey-900 flex h-12 items-center text-sm font-medium"
        >
          {{ props.suffix }}
        </div>
      </div>
    </template>

    <v-number-input
      v-else
      v-model="value"
      :class="inputClass"
      :style="inputStyle"
      :placeholder="props.placeholder"
      :disabled="props.disabled"
      :reverse="props.reverse"
      variant="outlined"
      density="compact"
      control-variant="hidden"
      :inset="props.inset"
      :min="props.min"
      :max="props.max"
      :aria-invalid="error ? 'true' : undefined"
      :aria-describedby="errorMessageId ?? undefined"
      autocomplete="off"
      v-bind="inputAttrs"
    >
      <template v-for="(_, name) in $slots" #[name]="slotProps">
        <slot :name="name" v-bind="slotProps" />
      </template>
    </v-number-input>
  </div>
</template>

<style scoped>
  /* 에러 상태일 때 border를 빨간색으로 override */
  .pp-v-number-input.has-error :deep(.v-field__outline),
  .pp-v-number-input.error :deep(.v-field__outline) {
    color: rgb(var(--v-theme-error));
    --v-field-border-opacity: 1 !important;
  }
</style>
