<script setup lang="ts">
  type Props = {
    canPrev?: boolean
    canNext?: boolean
    /** step7 등에서 위젯 비활성/퇴장 애니메이션 */
    disconnect?: boolean
    prevText?: string
    nextText?: string
    saveText?: string
  }

  const props = withDefaults(defineProps<Props>(), {
    canPrev: true,
    canNext: true,
    prevText: '이전으로',
    nextText: '다음으로',
    saveText: '임시저장',
    disconnect: false,
  })

  const emit = defineEmits<{
    (e: 'prev' | 'next' | 'save'): void
  }>()

  const isDisconnected = computed(() => !!props.disconnect)
</script>

<template>
  <button
    type="button"
    class="rqs-act__btn rqs-act__btn--outline"
    :disabled="!props.canPrev || isDisconnected"
    @click="emit('prev')"
  >
    {{ props.prevText }}
  </button>

  <div class="relative z-[1] flex-1" />

  <button
    type="button"
    class="rqs-act__btn rqs-act__btn--outline"
    :disabled="isDisconnected"
    @click="emit('save')"
  >
    {{ props.saveText }}
  </button>

  <button
    type="button"
    class="rqs-act__btn rqs-act__btn--primary"
    :disabled="!props.canNext || isDisconnected"
    @click="emit('next')"
  >
    {{ props.nextText }}
  </button>
</template>

<style scoped>
  /* Button 스타일 */
  .rqs-act__btn {
    position: relative;
    z-index: 1;
    cursor: pointer;
    white-space: nowrap;
    border-radius: 1rem; /* rounded-2xl */
    padding: 0.75rem 1.5rem; /* py-3 px-6 */
    font-size: 1rem; /* text-base */
    font-weight: 600; /* font-semibold */
    transition:
      background-color 200ms ease-in-out,
      border-color 200ms ease-in-out,
      color 200ms ease-in-out,
      opacity 200ms ease-in-out;
  }

  .rqs-act__btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .rqs-act__btn--outline {
    background: #ffffff;
    border: 1px solid #d1d5db;
    color: #111827;
  }

  .rqs-act__btn--outline:hover:not(:disabled) {
    background: var(--color-grey-50);
    border-color: var(--color-grey-300);
  }

  .rqs-act__btn--primary {
    background: var(--color-primary-700);
    border: 1px solid var(--color-primary-700);
    color: #ffffff;
  }

  .rqs-act__btn--primary:hover:not(:disabled) {
    background: var(--color-primary-800);
    border-color: var(--color-primary-800);
  }
</style>
