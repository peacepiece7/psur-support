<script setup lang="ts">
  import SelectV2 from '~/components/atoms/SelectV2.vue'

  export type DataTableHeader = {
    title: string
    key: string
    width?: string | number
    sortable?: boolean
    align?: 'start' | 'center' | 'end'
    [key: string]: unknown
  }

  export type TableProps = {
    /**
     * 테이블 헤더 정의
     */
    headers: DataTableHeader[]

    /**
     * 테이블 데이터
     */
    items: Record<string, unknown>[]

    /**
     * 로딩 상태
     */
    loading?: boolean

    /**
     * 선택 가능 여부
     */
    selectable?: boolean

    /**
     * 선택된 항목들 (v-model)
     */
    selected?: Record<string, unknown>[]

    /**
     * 페이지당 항목 수
     */
    itemsPerPage?: number

    /**
     * 현재 페이지
     */
    page?: number

    /**
     * 전체 항목 수
     */
    totalItems?: number

    /**
     * 정렬 필드
     */
    sortBy?: { key: string; order: 'asc' | 'desc' }[]

    /**
     * 높이
     */
    height?: string | number

    /**
     * 고정 헤더
     */
    fixedHeader?: boolean

    /**
     * 아이템의 고유 식별자 필드명 (item-value prop)
     */
    itemValue?: string
  }

  const props = withDefaults(defineProps<TableProps>(), {
    loading: false,
    selectable: false,
    selected: () => [],
    itemsPerPage: 10,
    page: 1,
    totalItems: undefined,
    sortBy: () => [],
    height: undefined,
    fixedHeader: false,
    itemValue: 'id',
  })

  const emit = defineEmits<{
    'update:selected': [value: Record<string, unknown>[]]
    'update:page': [value: number]
    'update:itemsPerPage': [value: number]
    'update:sortBy': [value: { key: string; order: 'asc' | 'desc' }[]]
    'click:row': [item: Record<string, unknown>, index: number]
  }>()

  const selectedItems = computed({
    get: () => props.selected,
    set: (value) => emit('update:selected', value),
  })

  const currentPage = computed({
    get: () => props.page,
    set: (value) => emit('update:page', value),
  })

  const currentItemsPerPage = computed({
    get: () => props.itemsPerPage,
    set: (value) => emit('update:itemsPerPage', value),
  })

  const currentSortBy = computed({
    get: () => props.sortBy,
    set: (value) => emit('update:sortBy', value),
  })

  const handleRowClick = (
    event: MouseEvent,
    { item }: { item: Record<string, unknown> },
  ) => {
    // Vuetify의 click:row 이벤트는 (event, { item }) 형태로 전달됨
    // index는 items 배열에서 찾음
    // item-value prop을 사용하여 고유 식별자로 비교
    const itemValue = props.itemValue
    const itemId = itemValue ? item[itemValue] : undefined
    const index = itemId
      ? props.items.findIndex((i) => i[itemValue] === itemId)
      : props.items.findIndex((i) => i === item)
    emit('click:row', item, index >= 0 ? index : -1)
  }

  // 표시 범위 계산
  const startItem = computed(() => {
    if (props.totalItems === undefined) return undefined
    // items-per-page가 -1이면 전체 표시이므로 범위 계산 불필요
    if (currentItemsPerPage.value <= 0) return undefined
    return (currentPage.value - 1) * currentItemsPerPage.value + 1
  })

  const endItem = computed(() => {
    if (props.totalItems === undefined) return undefined
    // items-per-page가 -1이면 전체 표시이므로 범위 계산 불필요
    if (currentItemsPerPage.value <= 0) return undefined
    return Math.min(
      currentPage.value * currentItemsPerPage.value,
      props.totalItems,
    )
  })

  // 전체 표시 여부 (items-per-page가 -1이거나 0 이하일 때)
  const isShowingAll = computed(() => {
    return currentItemsPerPage.value <= 0
  })

  // 페이지당 항목 수 옵션
  const itemsPerPageOptions = [
    { title: '10', value: 10 },
    { title: '25', value: 25 },
    { title: '50', value: 50 },
    { title: '100', value: 100 },
  ]

  // 스켈레톤 UI용 항목 수 계산 (items-per-page가 -1이거나 유효하지 않을 때 기본값 사용)
  const skeletonItemCount = computed(() => {
    if (props.itemsPerPage <= 0 || props.itemsPerPage > 100) {
      return 10 // 기본값
    }
    return props.itemsPerPage
  })
</script>

<template>
  <div class="w-full">
    <!-- 상단 툴바 (페이지당 항목 수 선택 및 표시 범위) -->
    <div
      v-if="totalItems !== undefined"
      class="border-grey-300 mb-2 flex items-center justify-end gap-4 rounded-lg border bg-white px-4 py-3"
    >
      <!-- 표시 범위 -->
      <div v-if="loading" class="flex items-center gap-2">
        <v-skeleton-loader type="text" width="80px" height="1.25rem" />
      </div>
      <div v-else-if="isShowingAll" class="text-grey-600 text-sm">
        전체 {{ totalItems.toLocaleString() }}개
      </div>
      <div v-else class="text-grey-600 text-sm">
        {{ totalItems.toLocaleString() }} 중
        {{ startItem?.toLocaleString() }}-{{ endItem?.toLocaleString() }}
      </div>

      <!-- 페이지당 항목 수 선택 -->
      <div v-if="!isShowingAll" class="flex items-center gap-2">
        <span v-if="loading" class="text-grey-600 text-sm">
          <v-skeleton-loader type="text" width="100px" height="1.25rem" />
        </span>
        <span v-else class="text-grey-600 text-sm">페이지 당 항목 수:</span>
        <div v-if="loading" class="w-20">
          <v-skeleton-loader type="text" width="100%" height="40px" />
        </div>
        <SelectV2
          v-else
          :model-value="currentItemsPerPage"
          :items="itemsPerPageOptions"
          item-text="title"
          item-value="value"
          size="sm"
          class="w-20"
          @update:model-value="(v) => (currentItemsPerPage = v as number)"
        />
      </div>
    </div>

    <!-- 스켈레톤 UI -->
    <div
      v-if="loading"
      class="border-grey-300 w-full overflow-hidden rounded-lg border"
    >
      <!-- 헤더 스켈레톤 -->
      <div class="border-grey-300 border-b bg-white">
        <div class="flex">
          <template v-for="(header, index) in headers" :key="index">
            <div
              class="border-grey-300 flex-1 border-r p-4 last:border-r-0"
              :style="{ minWidth: header.width || '120px' }"
            >
              <v-skeleton-loader type="text" width="80%" height="1.5rem" />
            </div>
          </template>
        </div>
      </div>

      <!-- 행 스켈레톤 -->
      <div
        v-for="i in skeletonItemCount"
        :key="i"
        class="border-grey-300 border-b bg-white last:border-b-0"
      >
        <div class="flex">
          <template v-for="(header, index) in headers" :key="index">
            <div
              class="border-grey-300 flex-1 border-r p-4 last:border-r-0"
              :style="{ minWidth: header.width || '120px' }"
            >
              <v-skeleton-loader
                type="text"
                :width="index === 0 ? '90%' : '70%'"
                height="1.25rem"
              />
            </div>
          </template>
        </div>
      </div>
    </div>

    <!-- 실제 테이블 -->
    <v-data-table
      v-else
      v-model="selectedItems"
      :headers="headers"
      :items="items"
      :items-per-page="currentItemsPerPage"
      :page="currentPage"
      :items-per-page-options="[10, 25, 50, 100]"
      :sort-by="currentSortBy"
      :height="height"
      :fixed-header="fixedHeader"
      class="border-grey-300 border"
      :item-value="itemValue"
      @click:row="handleRowClick"
    >
      <template v-for="(_, name) in $slots" #[name]="slotProps">
        <slot :name="name" v-bind="slotProps" />
      </template>
    </v-data-table>
  </div>
</template>

<style scoped>
  :deep(.v-data-table) {
    border-radius: 4px;
  }

  :deep(.v-data-table__thead) {
    background-color: var(--color-grey-50);
  }

  :deep(.v-data-table__thead th) {
    background-color: var(--color-grey-50);
    color: var(--color-grey-900);
    font-weight: 600;
    font-size: 0.875rem;
    border-bottom: 1px solid var(--color-grey-300);
  }

  :deep(.v-data-table__tbody tr) {
    border-bottom: 1px solid var(--color-grey-200);
  }

  :deep(.v-data-table__tbody tr:hover) {
    background-color: var(--color-grey-50);
  }

  :deep(.v-data-table__tbody td) {
    color: var(--color-grey-900);
    font-size: 0.875rem;
  }

  :deep(.v-data-table-footer) {
    display: none !important;
  }
</style>
