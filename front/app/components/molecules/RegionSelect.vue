<script setup lang="ts">
  type Props = {
    modelValue: string | null
    label?: string
    placeholder?: string
    disabled?: boolean
  }

  const props = withDefaults(defineProps<Props>(), {
    label: '등록지역',
    placeholder: '등록지역을 선택하세요',
    disabled: false,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string | null): void
  }>()

  const value = computed({
    get: () => props.modelValue,
    set: (v: string | null) => emit('update:modelValue', v),
  })

  const items = ref([
    '서울특별시',
    '부산광역시',
    '대구광역시',
    '인천광역시',
    '광주광역시',
    '대전광역시',
    '울산광역시',
    '세종특별자치시',
    '경기도',
    '강원특별자치도',
    '충청북도',
    '충청남도',
    '전북특별자치도',
    '전라남도',
    '경상북도',
    '경상남도',
    '제주특별자치도',
  ])
</script>

<template>
  <Select
    id="region-select"
    v-model="value"
    :label="label"
    :options="items.map((item) => ({ title: item, value: item }))"
    :placeholder="placeholder"
    :aria-label="label"
  />
</template>
