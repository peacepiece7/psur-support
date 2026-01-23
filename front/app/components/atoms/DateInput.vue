<script setup lang="ts">
  import dayjs from 'dayjs'

  type Props = {
    /**
     * 선택된 날짜 (YYYY-MM-DD)
     */
    modelValue: string | null

    /** 라벨 */
    label?: string

    /** 비활성화 */
    disabled?: boolean

    /** 지우기 버튼 */
    clearable?: boolean

    /** 확인/취소 버튼 숨김 여부 */
    hideActions?: boolean

    /** 주 시작 요일 (월: 1) */
    firstDayOfWeek?: number
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: null,
    label: '',
    disabled: false,
    clearable: true,
    hideActions: false,
    firstDayOfWeek: 1,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string | null): void
  }>()

  const displayFormat = (date: unknown) => {
    if (!date) return ''
    const d = dayjs(date as any)
    return d.isValid() ? d.format('YYYY-MM-DD') : ''
  }

  const internalValue = computed({
    get: () => props.modelValue,
    set: (val: unknown) => {
      if (!val) return emit('update:modelValue', null)

      const d = dayjs(val as any)
      if (!d.isValid()) return emit('update:modelValue', null)

      emit('update:modelValue', d.format('YYYY-MM-DD'))
    },
  })
</script>

<template>
  <v-date-input
    v-model="internalValue"
    class="pp-v-field"
    :label="props.label"
    :disabled="props.disabled"
    :clearable="props.clearable"
    :hide-actions="props.hideActions"
    :first-day-of-week="props.firstDayOfWeek"
    cancel-text="취소"
    ok-text="확인"
    format="yyyy-MM-dd"
    :display-format="displayFormat"
    v-bind="$attrs"
  />
</template>
