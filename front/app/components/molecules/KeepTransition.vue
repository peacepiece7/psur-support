<script setup lang="ts">
  export type TransitionDirection = 'left' | 'right' | 'up' | 'down'

  const props = withDefaults(
    defineProps<{
      /**
       * Transition 방향
       * @default 'down'
       */
      direction?: TransitionDirection
      /**
       * Transition 지속 시간 (초)
       * @default 0.3
       */
      duration?: number
    }>(),
    {
      direction: 'down',
      duration: 0.3,
    },
  )

  const transitionName = computed(() => `keep-transition-${props.direction}`)
</script>

<template>
  <Transition :name="transitionName" :css="true">
    <slot />
  </Transition>
</template>
