<script setup lang="ts">
  /**
   * Divider 방향 타입
   */
  export type DividerOrientation = 'horizontal' | 'vertical'

  /**
   * Divider 색상 타입
   */
  export type DividerColor =
    | 'stroke-200'
    | 'stroke-300'
    | 'grey-50'
    | 'grey-900'

  /**
   * Divider Variant 타입
   * - dotted: grey-300, height 2px
   * - solidGrey: grey-200, height 1px
   * - solidBlack: grey-900, height 2px
   * - solidFat: grey-300, height 10px
   */
  export type DividerVariant =
    | 'dotted'
    | 'solidGrey'
    | 'solidBlack'
    | 'solidFat'

  const DIVIDER_STYLE = {
    dotted: {
      style: 'dotted',
      color: 'stroke-300',
      height: 2,
    },
    solidGrey: {
      style: 'solid',
      color: 'stroke-200',
      height: 1,
    },
    solidBlack: {
      style: 'solid',
      color: 'grey-900',
      height: 2,
    },
    solidFat: {
      style: 'solid',
      color: 'stroke-300',
      height: 10,
    },
  } as const

  type Props = {
    /**
     * Divider 방향
     * - horizontal: 가로 구분선 (기본값)
     * - vertical: 세로 구분선
     */
    orientation?: DividerOrientation
    /**
     * Divider Variant
     * - dotted: 점선, stroke-300, height 2px (기본값)
     * - solidGrey: 실선 회색, stroke-200, height 1px
     * - solidBlack: 실선 검정, grey-900, height 2px
     * - solidFat: 실선 굵게, stroke-300, height 10px
     */
    variant?: DividerVariant
    /**
     * Divider 색상 (variant 기본값을 override)
     * - stroke-200: Neutral/Stroke/Stroke-200 (#e1e7ee)
     * - stroke-300: Neutral/Stroke/Stroke-300 (#d0d4d9)
     * - grey-50: Grey-50 (#f9fafb)
     * - grey-900: Grey-900 (#181e25)
     */
    color?: DividerColor
    /**
     * Divider 높이 (px 단위, variant 기본값을 override)
     * 기본값: variant에 따라 다름
     */
    height?: number
  }

  const props = withDefaults(defineProps<Props>(), {
    orientation: 'horizontal',
    variant: 'dotted',
    color: undefined,
    height: undefined,
  })

  const dividerStyleConfig = computed(() => {
    const variantConfig = DIVIDER_STYLE[props.variant]
    return {
      style: variantConfig.style,
      color: props.color ?? variantConfig.color,
      height: props.height ?? variantConfig.height,
    }
  })

  const colorVar = computed(() => {
    const color = dividerStyleConfig.value.color
    if (color === 'stroke-200') {
      return 'var(--color-neutral-stroke-200)'
    } else if (color === 'stroke-300') {
      return 'var(--color-neutral-stroke-300)'
    } else if (color === 'grey-50') {
      return 'var(--color-grey-50)'
    } else if (color === 'grey-900') {
      return 'var(--color-grey-900)'
    }
    return 'var(--color-neutral-stroke-300)'
  })

  const dividerClass = computed(() => {
    return [
      'divider',
      `divider--${props.orientation}`,
      `divider--${dividerStyleConfig.value.style}`,
    ]
  })

  const dividerStyle = computed(() => {
    const config = dividerStyleConfig.value
    const heightValue = `${config.height}px`
    const colorValue = colorVar.value

    if (props.orientation === 'horizontal') {
      if (config.style === 'dotted') {
        return {
          width: '100%',
          height: '0',
          borderTop: `${config.height}px dotted ${colorValue}`,
          background: 'none',
        }
      } else {
        return {
          width: '100%',
          height: heightValue,
          backgroundColor: colorValue,
        }
      }
    } else {
      if (config.style === 'dotted') {
        return {
          width: '0',
          height: '100%',
          borderLeft: `${config.height}px dotted ${colorValue}`,
          background: 'none',
        }
      } else {
        return {
          width: heightValue,
          height: '100%',
          backgroundColor: colorValue,
        }
      }
    }
  })
</script>

<template>
  <div :class="dividerClass" :style="dividerStyle" v-bind="$attrs" />
</template>

<style scoped>
  .divider {
    flex-shrink: 0;
  }

  .divider--horizontal {
    min-height: 0;
    min-width: 0;
  }

  .divider--vertical {
    min-width: 0;
    min-height: 0;
  }

  .divider--dotted {
    background: none;
    border: none;
  }
</style>
