<script setup lang="ts">
  import type { VSelect } from 'vuetify/components'

  type VSelectProps = VSelect['$props']

  defineOptions({
    inheritAttrs: false,
  })

  export type SelectV2Size = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'max'

  export type SelectV2Props = {
    /**
     * v-model 값
     */
    modelValue: VSelectProps['modelValue']

    /**
     * 옵션 목록
     */
    items: Record<string, unknown>[]

    /**
     * 옵션에서 표시할 텍스트 필드명 (예: "codeName", "cdNm")
     */
    itemText?: string

    /**
     * 옵션에서 사용할 값 필드명 (예: "code", "commnCd")
     */
    itemValue?: string

    /**
     * 입력 사이즈
     */
    size?: SelectV2Size

    /** 라벨 */
    label?: VSelectProps['label']

    /** placeholder */
    placeholder?: VSelectProps['placeholder']

    /** 비활성 */
    disabled?: VSelectProps['disabled']

    /** 읽기전용 */
    readonly?: VSelectProps['readonly']

    /** clearable */
    clearable?: boolean

    /** multiple */
    multiple?: boolean

    /** 에러 상태 */
    error?: ComputedRef<boolean> | boolean
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string> | string
  }

  const props = withDefaults(defineProps<SelectV2Props>(), {
    itemText: 'title',
    itemValue: 'value',
    size: 'md',
    label: undefined,
    disabled: false,
    readonly: false,
    clearable: false,
    multiple: false,
    placeholder: '선택',
    error: undefined,
    errorMessageId: undefined,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: VSelectProps['modelValue']): void
  }>()

  const error = computed(() => unref(props.error))
  const errorMessageId = computed(() => unref(props.errorMessageId))

  const value = computed({
    get: () => props.modelValue,
    set: (v: VSelectProps['modelValue']) => emit('update:modelValue', v),
  })

  const selectClass = computed(() => {
    return [`select-v2-size-${props.size}`]
  })

  const attrs = useAttrs()

  const inputAttrs = computed(() => {
    const { class: _ignored, ...rest } = attrs
    return rest
  })

  const inputClass = computed(() => {
    return [
      'pp-v-select-v2',
      selectClass.value,
      {
        'has-error': error.value,
        error: error.value,
      },
      attrs.class,
    ]
  })

  const inputStyle = computed(() => {
    return {
      width: '100%',
    }
  })
</script>

<template>
  <div class="grid gap-1">
    <!--
      NOTE:
      TextField와 동일하게 외부 label을 사용하여 접근성을 개선합니다.
    -->
    <div
      v-if="props.label"
      class="text-grey-900 text-sm leading-[1.2] font-bold"
    >
      {{ props.label }}
    </div>

    <v-select
      v-model="value"
      :items="props.items"
      :item-title="props.itemText"
      :item-value="props.itemValue"
      :class="inputClass"
      :style="inputStyle"
      :label="undefined"
      :placeholder="props.placeholder"
      :disabled="props.disabled"
      :readonly="props.readonly"
      :clearable="props.clearable"
      :multiple="props.multiple"
      variant="outlined"
      density="compact"
      :aria-invalid="error ? 'true' : undefined"
      :aria-describedby="errorMessageId ?? undefined"
      v-bind="inputAttrs"
    >
      <template v-for="(_, name) in $slots" #[name]="slotProps">
        <slot :name="name" v-bind="slotProps" />
      </template>
    </v-select>
  </div>
</template>

<style scoped>
  /* .pp-v-select-v2 :deep(.v-field) {
    padding-right: 0 !important;
  } */

  /* 에러 상태일 때 border를 빨간색으로 override */
  .pp-v-select-v2.has-error :deep(.v-field__outline),
  .pp-v-select-v2.error :deep(.v-field__outline) {
    color: rgb(var(--v-theme-error));
  }

  .pp-v-select-v2 :deep(.v-field__append-inner) {
    background-color: var(--color-static-white) !important;
    padding-right: 10px;
  }
</style>
