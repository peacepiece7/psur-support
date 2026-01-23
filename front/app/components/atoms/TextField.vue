<script setup lang="ts">
  import type { VTextField } from 'vuetify/components'

  type VTextFieldProps = VTextField['$props']

  defineOptions({
    inheritAttrs: false,
  })

  export type TextFieldSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'max'
  export type TextFieldVariant = NonNullable<VTextFieldProps['variant']>

  export type TextFieldProps = {
    /**
     * v-model 값
     */
    modelValue: VTextFieldProps['modelValue']

    /**
     * 입력 사이즈 (커스텀)
     * - 실제 width/height는 추후 클래스 주입 방식으로 확장 예정
     * - prefix/suffix가 있을 때는 전체 컨테이너(prefix + input + suffix)의 사이즈를 의미
     */
    size?: TextFieldSize

    /** 라벨 */
    label: VTextFieldProps['label']
    /** placeholder */
    placeholder?: VTextFieldProps['placeholder']
    /** type */
    type?: VTextFieldProps['type']
    /** 비활성 */
    disabled?: VTextFieldProps['disabled']
    /** 읽기전용 */
    readonly?: VTextFieldProps['readonly']

    /** append-inner-icon */
    appendInnerIcon?: VTextFieldProps['appendInnerIcon']

    /** maxlength */
    maxLength?: number | string
    /** autocomplete */
    autocomplete?: VTextFieldProps['autocomplete']

    /** prefix - textField 바깥 앞쪽에 표시됨 */
    prefix?: string
    /** suffix - textField 바깥 뒤쪽에 표시됨 */
    suffix?: string

    /** 에러 상태 */
    error?: ComputedRef<boolean> | boolean
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string> | string
  }

  const props = withDefaults(defineProps<TextFieldProps>(), {
    size: 'md',
    type: 'text',
    disabled: false,
    readonly: false,
    autocomplete: 'on',
    placeholder: '입력해주세요',
    prefix: undefined,
    suffix: undefined,
    maxLength: undefined,
    appendInnerIcon: undefined,
    error: undefined,
    errorMessageId: undefined,
  })

  const textFieldId = useId()
  const error = computed(() => unref(props.error))
  const errorMessageId = computed(() => unref(props.errorMessageId))

  const emit = defineEmits<{
    (e: 'update:modelValue', value: VTextFieldProps['modelValue']): void
  }>()

  const value = computed({
    get: () => props.modelValue,
    set: (v: VTextFieldProps['modelValue']) => emit('update:modelValue', v),
  })

  // prefix 또는 suffix가 있는지 확인
  const hasExternalAffix = computed(() => {
    return !!(props.prefix || props.suffix)
  })

  // size 클래스 생성 (prefix/suffix가 있어도 사용자가 지정한 size 사용)
  const textFieldClass = computed(() => {
    return [`text-field-size-${props.size}`]
  })

  const attrs = useAttrs()

  const inputClass = computed(() => {
    return [
      'pp-v-text-field',
      // prefix 또는 suffix가 있으면 text-field 자체에는 size 클래스를 적용하지 않음 (컨테이너에 적용)
      hasExternalAffix.value ? '' : textFieldClass.value,
      {
        'has-error': error.value,
        error: error.value,
      },
      attrs.class,
    ]
  })

  const inputStyle = computed(() => {
    return {
      width: hasExternalAffix.value ? undefined : '100%',
    }
  })

  // prefix 또는 suffix가 있을 때 컨테이너 클래스
  const containerClass = computed(() => {
    return [
      'text-field-with-affix',
      hasExternalAffix.value ? textFieldClass.value : '',
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
      :for="textFieldId"
      class="text-grey-900 text-sm leading-[1.2] font-bold"
    >
      {{ props.label }}
    </label>

    <template v-if="props.prefix || props.suffix">
      <div :class="containerClass" class="flex items-center gap-2">
        <div
          v-if="props.prefix"
          class="text-field-prefix text-grey-900 flex h-12 items-center text-sm font-medium"
        >
          {{ props.prefix }}
        </div>

        <v-text-field
          v-bind="attrs"
          :id="textFieldId"
          v-model="value"
          :class="inputClass"
          :style="inputStyle"
          :label="undefined"
          :type="props.type"
          :placeholder="props.placeholder"
          :disabled="props.disabled"
          :readonly="props.readonly"
          variant="outlined"
          density="compact"
          :append-inner-icon="props.appendInnerIcon"
          :maxlength="props.maxLength"
          :autocomplete="props.autocomplete"
          :aria-invalid="error ? 'true' : undefined"
          :aria-describedby="errorMessageId ?? undefined"
        >
          <template v-for="(_, name) in $slots" #[name]="slotProps">
            <slot :name="name" v-bind="slotProps" />
          </template>
        </v-text-field>

        <div
          v-if="props.suffix"
          class="text-field-suffix text-grey-900 flex h-12 items-center text-sm font-medium"
        >
          {{ props.suffix }}
        </div>
      </div>
    </template>

    <v-text-field
      v-else
      v-bind="attrs"
      :id="textFieldId"
      v-model="value"
      :class="inputClass"
      :style="inputStyle"
      :label="undefined"
      :type="props.type"
      :placeholder="props.placeholder"
      :disabled="props.disabled"
      :readonly="props.readonly"
      variant="outlined"
      density="compact"
      :append-inner-icon="props.appendInnerIcon"
      :maxlength="props.maxLength"
      :autocomplete="props.autocomplete"
      :aria-invalid="error ? 'true' : undefined"
      :aria-describedby="errorMessageId ?? undefined"
    >
      <template v-for="(_, name) in $slots" #[name]="slotProps">
        <slot :name="name" v-bind="slotProps" />
      </template>
    </v-text-field>
  </div>
</template>

<style scoped>
  /* 에러 상태일 때 border를 빨간색으로 override */
  .pp-v-text-field.has-error :deep(.v-field__outline),
  .pp-v-text-field.error :deep(.v-field__outline) {
    color: rgb(var(--v-theme-error));
  }
</style>
