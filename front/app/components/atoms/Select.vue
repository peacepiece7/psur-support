<script setup lang="ts">
  import { INPUT } from '~/constants/design'

  defineOptions({
    inheritAttrs: false,
  })

  export type SelectSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'max'

  export type SelectOption = {
    title: string
    value: string
    disabled?: boolean
  }

  type SelectProps = {
    /** v-model */
    modelValue: string | null

    /** 옵션 목록 */
    options: SelectOption[]

    /** 입력 사이즈 */
    size?: SelectSize

    /** 라벨 */
    label?: string

    /** placeholder 옵션 텍스트 (빈 값 옵션) */
    placeholder?: string

    /** disabled */
    disabled?: boolean

    /** readonly */
    readonly?: boolean

    /** name */
    name?: string

    /** select에 붙일 class */
    selectClass?: string
  }

  const props = withDefaults(defineProps<SelectProps>(), {
    modelValue: null,
    size: 'md',
    label: undefined,
    placeholder: '선택',
    disabled: false,
    readonly: false,
    name: undefined,
    selectClass: undefined,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string | null): void
  }>()

  const selectId = useId()

  const value = computed({
    get: () => props.modelValue || '',
    set: (v: string) => emit('update:modelValue', v || null),
  })

  const selectClass = computed(() => {
    return [
      'pp-v-select',
      `select-size-${props.size}`,
      { 'pp-v-select--readonly': props.readonly },
      props.selectClass,
    ]
  })

  const selectWidth = computed(() => {
    return INPUT.width[props.size]
  })

  const selectStyle = computed(() => {
    return {
      width: selectWidth.value,
    }
  })

  const attrs = useAttrs()
</script>

<template>
  <div class="grid gap-1">
    <!--
      NOTE:
      TextField와 동일하게 외부 label을 사용하여 접근성을 개선합니다.
    -->
    <label
      v-if="props.label"
      :for="selectId"
      class="text-grey-900 text-sm leading-[1.2] font-bold"
    >
      {{ props.label }}
    </label>

    <select
      :id="selectId"
      v-model="value"
      :name="props.name"
      :disabled="props.disabled || props.readonly"
      :class="selectClass"
      :style="selectStyle"
      :aria-label="props.label"
      v-bind="attrs"
    >
      <option value="" disabled>
        {{ props.placeholder }}
      </option>
      <option
        v-for="opt in props.options"
        :key="opt.value"
        :value="opt.value"
        :disabled="opt.disabled"
      >
        {{ opt.title }}
      </option>
    </select>
  </div>
</template>
