<script setup lang="ts">
  export type PaginationProps = {
    /**
     * 현재 페이지
     */
    modelValue: number

    /**
     * 전체 페이지 수
     */
    length: number

    /**
     * 페이지당 항목 수
     */
    itemsPerPage?: number

    /**
     * 전체 항목 수
     */
    totalItems?: number

    /**
     * 로딩 상태
     */
    loading?: boolean

    /**
     * 둥근 모서리 사용 여부
     */
    rounded?: boolean

    /**
     * 색상
     */
    color?: string
  }

  const props = withDefaults(defineProps<PaginationProps>(), {
    itemsPerPage: 10,
    totalItems: undefined,
    loading: false,
    rounded: true,
    color: 'primary',
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: number): void
  }>()

  const currentPage = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value),
  })

  const totalPages = computed(() => {
    if (props.totalItems && props.itemsPerPage) {
      return Math.ceil(props.totalItems / props.itemsPerPage)
    }
    return props.length
  })

  const startItem = computed(() => {
    if (props.totalItems === undefined) return undefined
    return (currentPage.value - 1) * props.itemsPerPage + 1
  })

  const endItem = computed(() => {
    if (props.totalItems === undefined) return undefined
    return Math.min(currentPage.value * props.itemsPerPage, props.totalItems)
  })
</script>

<template>
  <div class="flex flex-col items-center gap-4">
    <!-- 페이지네이션 정보 -->
    <div v-if="totalItems !== undefined" class="text-grey-600 text-sm">
      <!-- <span v-if="!loading">
        전체 {{ totalItems.toLocaleString() }}개 중
        {{ startItem?.toLocaleString() }} - {{ endItem?.toLocaleString() }}개
        표시
      </span> -->
      <span v-if="loading" class="flex items-center gap-2">
        <v-progress-circular indeterminate size="16" width="2" color="grey" />
        로딩 중...
      </span>
    </div>

    <!-- 페이지네이션 컨트롤 -->
    <v-pagination
      v-model="currentPage"
      :length="totalPages"
      :disabled="loading"
      :rounded="rounded"
      :color="color"
      :total-visible="7"
      class="pagination-controls"
    />
  </div>
</template>

<style scoped></style>
