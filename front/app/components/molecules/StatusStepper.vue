<script setup lang="ts">
  import { computed } from 'vue'

  export type StatusStep = {
    step: number
    title: string
  }

  type Props = {
    steps: StatusStep[]
    /** 0-based */
    modelValue: number
    /** 읽기 전용 모드 (기본값: true, 클릭 불가) */
    readonly?: boolean
    /** 반려된 step index 목록 (0-based) */
    rejectedIndices?: number[]
  }

  const props = withDefaults(defineProps<Props>(), {
    readonly: true,
    rejectedIndices: () => [],
  })
  const emit = defineEmits<{
    (e: 'update:modelValue' | 'click', value: number): void
  }>()

  const current = computed(() => props.modelValue)

  const isDisabled = computed(() => props.readonly)

  const onStepClick = (index: number) => {
    if (isDisabled.value) return
    emit('click', index)
    emit('update:modelValue', index)
  }

  // 스텝 상태 결정
  const getStepState = (index: number) => {
    if (props.rejectedIndices?.includes(index)) {
      return 'rejected'
    }
    if (index < current.value) {
      return 'done'
    }
    if (index === current.value) {
      return 'active'
    }
    return 'pending'
  }

  // 버튼 배경 및 테두리 스타일
  const getButtonClass = (index: number) => {
    const state = getStepState(index)
    const cursorClass = isDisabled.value ? 'cursor-default' : 'cursor-pointer'
    const baseClass = `flex items-center gap-4 pl-2 pr-4 py-2 rounded-full w-[144px] transition-colors duration-300 ease-in-out ${cursorClass} disabled:cursor-not-allowed disabled:opacity-45`

    switch (state) {
      case 'done':
        return `${baseClass} bg-[var(--color-primary-700)]`
      case 'active':
        return `${baseClass} bg-[var(--color-static-white)] border-2 border-[var(--color-primary-700)]`
      case 'rejected':
        return `${baseClass} bg-[var(--color-static-white)] border-2 border-[var(--color-functional-negative)]`
      case 'pending':
      default:
        return `${baseClass} bg-[var(--color-grey-200)]`
    }
  }

  // 숫자 배지 스타일
  const getNumBadgeClass = (index: number) => {
    const state = getStepState(index)
    const baseClass =
      'flex items-center justify-center rounded-full size-10 text-xl font-semibold leading-[1.5] tracking-[-0.4px] transition-colors duration-300 ease-in-out'

    switch (state) {
      case 'done':
        return `${baseClass} bg-[var(--color-static-white)] text-[var(--color-primary-700)]`
      case 'active':
        return `${baseClass} bg-[var(--color-primary-700)] text-[var(--color-static-white)]`
      case 'rejected':
        return `${baseClass} bg-[var(--color-functional-negative)] text-[var(--color-static-white)]`
      case 'pending':
      default:
        return `${baseClass} bg-[var(--color-neutral-fill-100)] text-[var(--color-neutral-text-disabled)]`
    }
  }

  // 텍스트 스타일
  const getTextClass = (index: number) => {
    const state = getStepState(index)
    const baseClass =
      'text-xl font-medium leading-[1.5] tracking-[-0.4px] transition-colors duration-300 ease-in-out'

    switch (state) {
      case 'done':
        return `${baseClass} text-[var(--color-static-white)] font-bold`
      case 'active':
        return `${baseClass} text-[var(--color-primary-700)] font-bold`
      case 'rejected':
        return `${baseClass} text-[var(--color-functional-negative)]`
      case 'pending':
      default:
        return `${baseClass} text-[var(--color-neutral-text-disabled)]`
    }
  }

  // Divider 색상 결정
  const getDividerColorClass = (index: number) => {
    const isCurrentDone = index < current.value
    const isNextDone = index + 1 < current.value
    const isNextActive = index + 1 === current.value

    // 완료된 스텝 사이 또는 완료-현재 스텝 사이는 파란색
    if (isCurrentDone && (isNextDone || isNextActive)) {
      return 'text-[var(--color-primary-600)]'
    }
    return 'text-[var(--color-grey-600)]'
  }
</script>

<template>
  <nav class="w-full" aria-label="신청 단계">
    <ol class="m-0 flex list-none items-center gap-4 p-0">
      <template v-for="(s, i) in props.steps" :key="s.step">
        <!-- Step item -->
        <li class="flex items-center">
          <button
            type="button"
            :class="getButtonClass(i)"
            :aria-current="i === current ? 'step' : undefined"
            :aria-label="`${s.step}단계: ${s.title}`"
            :aria-disabled="isDisabled ? 'true' : undefined"
            @click="onStepClick(i)"
          >
            <!-- 숫자 배지 -->
            <div :class="getNumBadgeClass(i)">
              {{ s.step }}
            </div>
            <!-- 텍스트 -->
            <span :class="getTextClass(i)">
              {{ s.title }}
            </span>
          </button>
        </li>

        <!-- Divider -->
        <div
          v-if="i < props.steps.length - 1"
          class="flex h-0 w-16 items-center justify-center"
        >
          <svg
            class="pointer-events-none h-[3px] w-full flex-shrink transition-colors duration-300 ease-in-out"
            viewBox="0 0 67 3"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            preserveAspectRatio="none"
            :class="getDividerColorClass(i)"
          >
            <path
              d="M1.5 1.5H65.5"
              stroke="currentColor"
              stroke-width="3"
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-dasharray="0.1 8"
            />
          </svg>
        </div>
      </template>
    </ol>
  </nav>
</template>
