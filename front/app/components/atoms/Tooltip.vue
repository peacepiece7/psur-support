<script setup lang="ts">
  import { ref, computed } from 'vue'

  export type TooltipVariant = 'black' | 'white'
  export type TooltipLocation = 'top' | 'bottom' | 'left' | 'right'

  type Props = {
    text: string
    variant?: TooltipVariant
    location?: TooltipLocation
    /** hover 시에도 툴팁 표시 (기본값: true) */
    openOnHover?: boolean
    /** 툴팁이 닫히지 않고 유지되는지 여부 (기본값: false) */
    persistent?: boolean
    /** 렌더 시 바로 표시할지 여부 (기본값: false, hover 시만 표시) */
    initialOpen?: boolean
  }

  const props = withDefaults(defineProps<Props>(), {
    variant: 'black',
    location: 'top',
    openOnHover: true,
    persistent: false,
    initialOpen: false,
  })

  const open = ref(props.initialOpen)

  // variant별 스타일 클래스
  const tooltipClasses = computed(() => {
    const baseClasses =
      'relative px-3 py-2 rounded-md text-[13px] leading-[1.4] max-w-[240px]'

    if (props.variant === 'black') {
      return `${baseClasses} bg-[var(--color-grey-900)] text-[var(--color-static-white)]`
    }
    return `${baseClasses} bg-[var(--color-static-white)] text-[var(--color-grey-900)] border border-[var(--color-grey-300)]`
  })
</script>

<template>
  <VTooltip
    v-model="open"
    :location="location"
    :open-on-hover="openOnHover"
    :persistent="persistent"
    class="tooltip-root"
  >
    <!-- activator -->
    <template #activator="{ props: actProps }">
      <span v-bind="actProps">
        <slot name="activator">ⓘ</slot>
      </span>
    </template>

    <!-- tooltip content -->
    <div :class="tooltipClasses">
      <span class="block">
        {{ text }}
      </span>
    </div>
  </VTooltip>
</template>

<style scoped>
  /* v-overlay__content의 배경색 제거 */
  .tooltip-root :deep(.v-overlay__content) {
    background-color: transparent !important;
  }
</style>
