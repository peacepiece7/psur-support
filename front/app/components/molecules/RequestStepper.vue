<script setup lang="ts">
  import { computed } from 'vue'

  export type RequestStep = {
    step: number
    title: string
  }

  export type StepperLabelMode = 'side' | 'down'

  type Props = {
    steps: RequestStep[]
    /** 0-based */
    modelValue: number
    /** 읽기 전용 모드 (기본값: true, 클릭 불가) */
    readonly?: boolean
    /** 클릭 불가 step index 목록 (0-based) */
    disabledIndices?: number[]
    /** 레이블 위치 모드 (side: 옆, down: 아래) */
    labelMode?: StepperLabelMode
  }

  const props = withDefaults(defineProps<Props>(), {
    readonly: true,
    disabledIndices: () => [],
    labelMode: 'down',
  })
  const emit = defineEmits<{
    (e: 'update:modelValue' | 'click', value: number): void
  }>()

  const current = computed(() => props.modelValue)

  // 그리드 컬럼 수를 steps와 dividers에 맞게 동적 설정
  // li (auto) divider (1fr) li (auto) divider (1fr) ... li (auto)
  const gridColumns = computed(() => {
    const columns: string[] = []
    for (let i = 0; i < props.steps.length; i++) {
      columns.push('auto') // li
      if (i < props.steps.length - 1) {
        columns.push('minmax(0, 1fr)') // divider (flexible)
      }
    }
    return columns.join(' ')
  })

  const isDisabled = (index: number) => {
    return props.readonly || props.disabledIndices.includes(index)
  }

  const onStepClick = (index: number) => {
    if (isDisabled(index)) return
    emit('click', index)
    emit('update:modelValue', index)
  }

  // 연결선 색상 결정: complete-complete 또는 complete-inprogress만 파란색
  const getDividerColorClass = (index: number) => {
    const isCurrentComplete = index < current.value
    const isNextComplete = index + 1 < current.value
    const isNextInprogress = index + 1 === current.value

    // complete-complete 또는 complete-inprogress인 경우 파란색
    if (isCurrentComplete && (isNextComplete || isNextInprogress)) {
      return 'text-[var(--color-primary-600)]'
    }
    return 'text-[var(--color-grey-600)]'
  }

  // Dot 스타일 클래스
  const getDotClass = (index: number) => {
    const isLastStep = index === props.steps.length - 1
    const isActive = index === current.value
    const isDone = index < current.value || (isLastStep && isActive)
    const hoverClass = props.readonly
      ? ''
      : 'hover:!border-[var(--color-primary-700)]'

    if (isDone) {
      return '!border-[var(--color-primary-700)] !bg-[var(--color-primary-700)] !text-white'
    }
    if (isActive) {
      return '!border-[var(--color-primary-700)] !bg-white !text-[var(--color-primary-700)]'
    }
    return `!border-[var(--color-grey-600)] !bg-white !text-[var(--color-grey-600)] ${hoverClass}`
  }

  // Label 스타일 클래스
  const getLabelStepClass = (index: number) => {
    const isActive = index === current.value
    const isDone = index < current.value

    if (isActive || isDone) {
      return 'text-[var(--color-primary-700)]'
    }
    return 'text-[var(--color-grey-700)]'
  }

  const getLabelTitleClass = (index: number) => {
    const isLastStep = index === props.steps.length - 1
    const isActive = index === current.value
    const isDone = index < current.value || (isLastStep && isActive)

    if (isDone) {
      return `text-[var(--color-static-black)]`
    }
    if (isActive) {
      return `text-[var(--color-primary-700)]`
    }
    return `text-[var(--color-grey-600)]`
  }
</script>

<template>
  <nav class="w-full" aria-label="신청 단계">
    <ol
      class="m-0 mx-auto grid w-fit list-none items-start gap-y-6 p-0"
      :style="{ gridTemplateColumns: gridColumns }"
    >
      <template v-for="(s, i) in props.steps" :key="s.step">
        <!-- Step item -->
        <li
          :class="[
            'relative',
            props.labelMode === 'side'
              ? 'flex items-center gap-2'
              : 'grid max-w-24 min-w-24 justify-items-center gap-2',
          ]"
        >
          <button
            type="button"
            :class="[
              'relative z-[1] grid h-10 w-10 shrink-0 place-items-center rounded-full border-2 p-0 text-xl font-bold focus-visible:outline-2 focus-visible:outline-offset-[3px] focus-visible:outline-[var(--color-primary-700)]',
              props.readonly
                ? 'cursor-default'
                : 'cursor-pointer hover:shadow-[0_0_0_4px_rgba(26,72,213,0.12)] active:translate-y-[1px] active:scale-[0.98]',
              getDotClass(i),
              isDisabled(i) &&
                'disabled:cursor-not-allowed disabled:opacity-45 disabled:shadow-none disabled:hover:shadow-none disabled:active:transform-none',
            ]"
            :aria-current="i === current ? 'step' : undefined"
            :aria-label="`${s.step}단계: ${s.title}`"
            :aria-disabled="isDisabled(i) ? 'true' : undefined"
            @click="onStepClick(i)"
          >
            <span class="relative z-[3]">
              {{ s.step }}
            </span>
          </button>

          <div
            :class="[
              'min-w-0',
              props.labelMode === 'side' ? 'text-left' : 'text-center',
            ]"
          >
            <div
              :class="[
                'overflow-hidden text-xl leading-[1.5] font-medium tracking-[-0.4px]',
                props.labelMode === 'side'
                  ? 'whitespace-nowrap'
                  : 'text-ellipsis whitespace-nowrap',
                getLabelTitleClass(i),
              ]"
            >
              {{ s.title }}
            </div>
          </div>
        </li>

        <!-- Divider -->
        <div
          v-if="i < props.steps.length - 1"
          :class="[
            'hidden min-w-0 items-center justify-center sm:flex',
            props.labelMode === 'side' ? 'self-center' : '',
          ]"
        >
          <svg
            :class="[
              'pointer-events-none h-[3px] w-full flex-shrink',
              props.labelMode === 'side' ? 'px-2' : 'mt-4',
              getDividerColorClass(i),
            ]"
            viewBox="0 0 67 3"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            preserveAspectRatio="none"
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
