<script setup lang="ts">
  import { computed } from 'vue'

  export type ProgressStep = {
    step: number
    title: string
  }

  type Props = {
    steps: ProgressStep[]
    /** 0-based */
    current: number
    /** 반려 상태 (true일 경우 빨간색 표시) */
    reject?: boolean
  }

  const props = withDefaults(defineProps<Props>(), {
    reject: false,
  })

  const currentStep = computed(() => props.current + 1)
  const totalSteps = computed(() => props.steps.length)
  const currentStepData = computed(() => props.steps[props.current])
  const nextStepData = computed(() => {
    const nextIndex = props.current + 1
    return nextIndex < props.steps.length ? props.steps[nextIndex] : null
  })
  const previousStepData = computed(() => {
    const prevIndex = props.current - 1
    return prevIndex >= 0 ? props.steps[prevIndex] : null
  })

  // 진행률 계산 (0-100)
  const progress = computed(() => {
    if (totalSteps.value === 0) return 0
    return (currentStep.value / totalSteps.value) * 100
  })

  // 원형 progress의 stroke-dasharray 계산
  const circumference = computed(() => 2 * Math.PI * 18) // 반지름 18 (크기 40의 절반 - stroke-width 4)
  const strokeDashoffset = computed(() => {
    return circumference.value - (progress.value / 100) * circumference.value
  })

  // 색상 결정
  const getProgressColor = computed(() => {
    if (props.reject) {
      return 'var(--color-functional-negative)'
    }
    return 'var(--color-primary-700)'
  })

  const getBorderColor = computed(() => {
    if (props.reject) {
      return 'var(--color-functional-negative)'
    }
    return 'var(--color-grey-300)'
  })

  const getTextColor = computed(() => {
    if (props.reject) {
      return 'var(--color-functional-negative)'
    }
    return 'var(--color-grey-700)'
  })
</script>

<template>
  <div class="flex items-center gap-4">
    <!-- 원형 Progress Indicator -->
    <div class="relative shrink-0">
      <svg class="size-10 -rotate-90" viewBox="0 0 40 40">
        <!-- 배경 원 (회색) -->
        <circle
          cx="20"
          cy="20"
          r="18"
          fill="none"
          :stroke="getBorderColor"
          stroke-width="4"
        />
        <!-- 진행 원 (primary 또는 빨간색) -->
        <circle
          cx="20"
          cy="20"
          r="18"
          fill="none"
          :stroke="getProgressColor"
          stroke-width="4"
          stroke-linecap="round"
          :stroke-dasharray="circumference"
          :stroke-dashoffset="strokeDashoffset"
          class="transition-all duration-300 ease-in-out"
        />
      </svg>
      <!-- 중앙 텍스트 (현재 단계 / 총 단계) -->
      <div class="absolute inset-0 flex items-center justify-center">
        <span
          :class="[
            'text-xs leading-[1.5] font-medium',
            props.reject
              ? 'text-[var(--color-functional-negative)]'
              : 'text-[var(--color-grey-700)]',
          ]"
        >
          {{ currentStep }} / {{ totalSteps }}
        </span>
      </div>
    </div>

    <!-- 라벨 영역 -->
    <div class="flex min-w-0 flex-1 flex-col gap-1">
      <!-- 현재 단계 라벨 -->
      <div
        :class="[
          'text-xl leading-[1.5] font-medium tracking-[-0.4px]',
          props.reject
            ? 'text-[var(--color-functional-negative)]'
            : 'text-[var(--color-grey-900)]',
        ]"
      >
        {{ currentStepData?.title }}
      </div>
      <!-- 다음 단계 라벨 또는 이전 단계 라벨 -->
      <div
        v-if="nextStepData"
        class="text-sm leading-[1.5] tracking-[-0.4px] text-[var(--color-grey-600)]"
      >
        다음: {{ nextStepData.title }}
      </div>
      <div
        v-else-if="previousStepData"
        class="text-sm leading-[1.5] tracking-[-0.4px] text-[var(--color-grey-600)]"
      >
        이전: {{ previousStepData.title }}
      </div>
    </div>
  </div>
</template>
