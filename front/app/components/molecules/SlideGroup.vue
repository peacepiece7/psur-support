<script setup lang="ts">
  /**
   * Subheader 정보
   */
  export type SubheaderInfo = {
    /**
     * Subheader 제목
     */
    title: string
    /**
     * Subheader 내용 (2줄)
     */
    content: string[]
  }

  /**
   * SlideGroup 아이템 타입
   */
  export type SlideGroupItem = {
    /**
     * 아이템 고유 ID
     */
    id: string
    /**
     * 탭 제목
     */
    title?: string
    /**
     * 아이콘 이름 (선택사항)
     */
    icon?: string
    /**
     * Subheader 정보 배열 (2개)
     */
    subheaders?: SubheaderInfo[]
    /**
     * 유의사항 제목
     */
    noticeTitle?: string
    /**
     * 유의사항 내용 (2줄)
     */
    noticeContent?: string[]

    /**
     * 추가 데이터(선택) - 슬라이드 그룹 외부에서 활용 가능
     * 템플릿에서 자유롭게 접근할 수 있도록 any 사용
     */
    [key: string]: any
  }

  type Props = {
    /**
     * 슬라이드 그룹에 표시할 아이템 배열
     */
    items: SlideGroupItem[]
    /**
     * 선택된 탭 ID (v-model)
     */
    modelValue: string
    /**
     * 버튼 크기
     */
    btnSize?: 'base' | 'small' | 'large'
    /**
     * 화살표 버튼 표시 여부
     */
    showArrows?: boolean

    /**
     * 한 화면에 보이도록 제한할 아이템 개수 (옵션)
     * (Vuetify slide-group 자체 옵션이 없어 컨테이너 max-width로 제어)
     */
    maxVisibleItems?: number

    /**
     * 아이템(버튼) 1개의 예상 너비(px) - maxVisibleItems 사용 시 필요
     */
    estimatedItemWidthPx?: number

    /**
     * 아이템 간격(px, margin-right 등) - maxVisibleItems 사용 시 필요
     */
    estimatedItemGapPx?: number
  }

  const props = withDefaults(defineProps<Props>(), {
    items: () => [],
    modelValue: '',
    btnSize: 'base',
    showArrows: true,
    maxVisibleItems: undefined,
    estimatedItemWidthPx: undefined,
    estimatedItemGapPx: 8,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string): void
  }>()

  const selectedTab = computed({
    get: () => props.modelValue || props.items[0]?.id || '',
    set: (value) => emit('update:modelValue', value),
  })

  const wrapperStyle = computed(() => {
    const baseStyle = { width: '100%' }

    if (!props.maxVisibleItems || !props.estimatedItemWidthPx) {
      return baseStyle
    }

    const count = props.maxVisibleItems
    const w = props.estimatedItemWidthPx
    const gap = props.estimatedItemGapPx ?? 0

    // v-slide-group 화살표 영역을 대략적으로 더해줌 (prev/next 각 40px 정도)
    const arrows = props.showArrows ? 80 : 0

    const maxWidthPx = w * count + gap * Math.max(0, count - 1) + arrows
    return { ...baseStyle, maxWidth: `${maxWidthPx}px` }
  })
</script>

<template>
  <div class="w-full" :style="wrapperStyle">
    <v-slide-group
      v-model="selectedTab"
      class="w-full"
      mandatory
      :show-arrows="props.showArrows"
    >
      <v-slide-group-item
        v-for="item in props.items"
        :key="item.id"
        v-slot="{ isSelected, toggle }"
        :value="item.id"
      >
        <slot
          name="item"
          :item="item"
          :is-selected="isSelected"
          :toggle="toggle"
        >
          <button
            class="slide-group__tab"
            :class="{
              '': isSelected,
              [`slide-group__tab--${props.btnSize}`]: true,
            }"
            type="button"
            @click="toggle"
          >
            <v-icon v-if="item.icon" class="text-[20px]">
              {{ item.icon }}
            </v-icon>
            {{ item.title ?? '' }}
          </button>
        </slot>
      </v-slide-group-item>
    </v-slide-group>
  </div>
</template>
