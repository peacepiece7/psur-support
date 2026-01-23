<script setup lang="ts">
  import type { VTextarea } from 'vuetify/components'
  import { TEXTAREA_MAX_LENGTH } from '~/constants/ux'
  import KeepTransition from '~/components/molecules/KeepTransition.vue'

  type VTextareaProps = VTextarea['$props']

  defineOptions({
    inheritAttrs: false,
  })

  export type TextareaSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'max'

  export type TextareaProps = {
    /**
     * v-model 값
     */
    modelValue: VTextareaProps['modelValue']

    /**
     * 입력 사이즈 (커스텀)
     * - 실제 width는 추후 클래스 주입 방식으로 확장 예정
     */
    size?: TextareaSize

    /** 라벨 */
    label?: VTextareaProps['label']
    /** placeholder */
    placeholder?: VTextareaProps['placeholder']
    /** 비활성 */
    disabled?: VTextareaProps['disabled']
    /** 읽기전용 */
    readonly?: VTextareaProps['readonly']
    /** rows (높이 조절) */
    rows?: VTextareaProps['rows']
    /** autoGrow (자동 높이 증가) */
    autoGrow?: VTextareaProps['autoGrow']
    /** maxlength */
    maxLength?: number | string
    /** autocomplete */
    autocomplete?: VTextareaProps['autocomplete']

    /** 에러 상태 */
    error?: ComputedRef<boolean>
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string>
  }

  const props = withDefaults(defineProps<TextareaProps>(), {
    size: 'md',
    label: undefined,
    disabled: false,
    readonly: false,
    rows: 4,
    autoGrow: false,
    autocomplete: 'off',
    placeholder: '입력해주세요',
    maxLength: TEXTAREA_MAX_LENGTH,
    error: undefined,
    errorMessageId: undefined,
  })

  const textareaId = useId()

  const emit = defineEmits<{
    (e: 'update:modelValue', value: VTextareaProps['modelValue']): void
  }>()

  const value = computed({
    get: () => props.modelValue,
    set: (v: VTextareaProps['modelValue']) => emit('update:modelValue', v),
  })

  const textareaClass = computed(() => {
    return [`textarea-size-${props.size}`]
  })

  const attrs = useAttrs()

  const inputAttrs = computed(() => {
    const { class: _ignored, ...rest } = attrs
    return rest
  })

  const inputClass = computed(() => {
    return [
      'pp-v-textarea',
      textareaClass.value,
      {
        'has-error': props.error?.value,
        error: props.error?.value,
      },
      attrs.class,
    ]
  })

  const inputStyle = computed(() => {
    return {
      width: '100%',
    }
  })

  // 현재 입력된 글자 수 계산
  const currentLength = computed(() => {
    if (!value.value) return 0
    return String(value.value).length
  })

  // maxLength 값 (number 또는 string을 number로 변환)
  const maxLengthValue = computed(() => {
    if (!props.maxLength) return null
    return typeof props.maxLength === 'string'
      ? parseInt(props.maxLength, 10)
      : props.maxLength
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
      :for="textareaId"
      class="text-grey-900 text-sm leading-[1.2] font-bold"
    >
      {{ props.label }}
    </label>

    <div class="relative">
      <v-textarea
        :id="textareaId"
        v-model="value"
        :class="inputClass"
        :style="inputStyle"
        :label="undefined"
        :placeholder="props.placeholder"
        :disabled="props.disabled"
        :readonly="props.readonly"
        variant="outlined"
        density="compact"
        :rows="props.rows"
        :auto-grow="props.autoGrow"
        :maxlength="props.maxLength"
        :autocomplete="props.autocomplete"
        :aria-invalid="props.error?.value ? 'true' : undefined"
        :aria-describedby="props.errorMessageId?.value ?? undefined"
        v-bind="inputAttrs"
        persistent-hint
      >
        <template v-for="(_, name) in $slots" #[name]="slotProps">
          <slot :name="name" v-bind="slotProps" />
        </template>
      </v-textarea>
      <div :class="textareaClass" class="flex">
        <KeepTransition direction="down">
          <span v-if="error?.value" class="text-error">
            <slot name="validation-text" />
          </span>
        </KeepTransition>

        <span class="text-grey-600 flex-1 text-end">
          <span class="text-new-blue-700">{{ currentLength }} </span>/{{
            maxLengthValue
          }}
          <span>자</span>
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .pp-v-textarea.has-error :deep(.v-field__outline),
  .pp-v-textarea.error :deep(.v-field__outline) {
    color: rgb(var(--v-theme-error));
  }

  .text-error {
    color: rgb(var(--v-theme-error));
  }
</style>
