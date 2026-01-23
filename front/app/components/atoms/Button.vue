<script setup lang="ts">
  // Button Size 타입 정의
  export type ButtonSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'icon'

  // Button Color 타입 정의
  export type ButtonColor =
    | 'primary'
    | 'secondary'
    | 'tertiary'
    | 'warning'
    | 'surface-strong'
    | 'surface'
    | 'success'
    | 'muted'
    | 'inverse'

  // Button Variant 타입 정의
  export type ButtonVariant = 'fill' | 'outlined'

  // Button Shape 타입 정의
  export type ButtonShape = 'square' | 'soft' | 'rounded' | 'circle'

  export type ButtonProps = {
    /**
     * 버튼 크기 (커스텀)
     * - xs: height 23px, padding 0 6px (extra small)
     * - sm: height 32px, padding 0 12px (small)
     * - md: height 40px, padding 0 18px (medium, 기본값)
     * - lg: height 48px, padding 0 24px (large)
     * - xl: height 56px, padding 0 36px (extra large)
     */
    size?: ButtonSize
    /**
     * 버튼 색상 (커스텀)
     * - primary: #0630AE (var(--color-new-blue-900))
     * - secondary: Palette/Grey/600 (var(--color-grey-600))
     * - tertiary: #D0DBFF (var(--color-new-blue-200)), text: primary/700
     * - warning: 주황색
     * - surface-strong: 강한 표면 색상 (검은색)
     * - surface: 표면 색상 (어두운 회색)
     * - success: 초록색
     * - muted: 중립 회색
     * - inverse: 하얀색
     */
    color?: ButtonColor
    /**
     * 버튼 스타일 변형
     * - fill: 배경색이 color와 동일, 텍스트는 대비되는 색상 (기본값)
     * - outlined: border만 color와 동일, 텍스트도 color와 동일, 배경 투명
     */
    variant?: ButtonVariant
    /**
     * 버튼 모양
     * - square: 직각 모서리
     * - soft: 약간 둥근 모서리
     * - rounded: 둥근 모서리 (기본값)
     * - circle: 완전히 둥근 모양
     */
    shape?: ButtonShape
  }

  const props = withDefaults(defineProps<ButtonProps>(), {
    size: 'md',
    color: 'primary',
    variant: 'fill',
    shape: 'rounded',
  })

  // size, color, variant, shape에 따른 클래스 추가
  const buttonClass = computed(() => {
    return [
      'pp-btn',
      `button-size-${props.size}`,
      `button-color-${props.color}`,
      `button-variant-${props.variant}`,
      `button-shape-${props.shape}`,
    ]
  })
</script>

<template>
  <v-btn
    ref="buttonRef"
    type="button"
    v-bind="$attrs"
    :class="buttonClass"
    :ripple="{ class: 'vuetify-ripple-effect' }"
  >
    <slot />
  </v-btn>
</template>
