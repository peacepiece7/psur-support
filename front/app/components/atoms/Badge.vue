<script setup lang="ts">
  export type BadgeSize = 'sm' | 'md'

  export type BadgeSemantic =
    | 'positive' // 성공, 정상
    | 'informative' // 정보
    | 'negative' // 실패, 오류
    | 'notice' // 주의
    | 'neutral' // 중립

  export type BadgeColor =
    | 'blue'
    | 'sky-blue'
    | 'green'
    | 'purple'
    | 'red'
    | 'orange'
    | 'yellow'
    | 'grey'
    | 'black'

  export type BadgeType = 'tint' | 'solid' | 'line'

  const SEMANTIC_TO_COLOR: Record<BadgeSemantic, BadgeColor> = {
    positive: 'green',
    informative: 'blue',
    negative: 'red',
    notice: 'orange',
    neutral: 'grey',
  }

  type Props = {
    /**
     * 배지에 표시할 텍스트
     * - 슬롯이 있으면 슬롯이 우선됩니다.
     */
    label?: string

    /** 배지 의미 (semantic) */
    semantic?: BadgeSemantic

    /** 배지 색상 (color) - semantic을 override할 수 있음 */
    color?: BadgeColor

    /** 배지 타입 (type) - tint, solid, line */
    type?: BadgeType

    /** 크기 (default: md) */
    size?: BadgeSize
  }

  const props = withDefaults(defineProps<Props>(), {
    label: '',
    semantic: 'neutral',
    color: undefined,
    type: 'tint',
    size: 'md',
  })

  const resolvedColor = computed(() => {
    return props.color ?? SEMANTIC_TO_COLOR[props.semantic ?? 'neutral']
  })
</script>

<template>
  <div
    class="inline-flex items-center justify-center rounded-md leading-none font-medium whitespace-nowrap"
    :class="[
      `badge--${props.size}`,
      `badge--${props.type}`,
      `badge--${props.type}--${resolvedColor}`,
    ]"
    v-bind="$attrs"
  >
    <slot>{{ props.label }}</slot>
  </div>
</template>
