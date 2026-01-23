<script setup lang="ts">
  type Props = {
    /** step7 등에서 위젯 비활성/퇴장 애니메이션 */
    disconnect?: boolean
  }

  const props = withDefaults(defineProps<Props>(), {
    disconnect: false,
  })

  const isDisconnected = computed(() => !!props.disconnect)
  const isHidden = ref(false)

  const DISCONNECT_MS = 520

  let hideTimer: number | undefined

  watch(
    isDisconnected,
    (v) => {
      if (v) {
        if (hideTimer) window.clearTimeout(hideTimer)
        // 애니메이션 종료 후 DOM에서 제거
        hideTimer = window.setTimeout(() => {
          isHidden.value = true
        }, DISCONNECT_MS)
      } else {
        if (hideTimer) window.clearTimeout(hideTimer)
        isHidden.value = false
      }
    },
    { immediate: true },
  )
</script>

<template>
  <Transition name="rqs-act" appear>
    <div
      v-if="!isHidden"
      class="z-request-action pointer-events-none fixed right-0 bottom-6 left-0 px-4 py-0"
      :class="{
        'rqs-act--disconnect': isDisconnected,
      }"
      role="region"
      aria-label="신청 단계 이동"
      :aria-hidden="isDisconnected"
    >
      <div
        class="rqs-act-inner pointer-events-auto relative mx-auto flex max-w-[960px] items-center gap-3 overflow-hidden rounded-3xl border border-grey-200 p-4"
        style="
          background: color-mix(in srgb, #ffffff 92%, transparent);
          box-shadow: 0 12px 36px rgba(15, 23, 42, 0.18);
          transform-origin: 50% 50%;
          will-change: transform, opacity;
        "
      >
        <div
          class="rqs-act-blackout pointer-events-none absolute inset-0 z-[2] bg-black opacity-0"
          aria-hidden="true"
        />
        <slot />
      </div>
    </div>
  </Transition>
</template>

<style scoped>
  /* disconnect 애니메이션: TV 전원 꺼짐 + 살짝 튕김 */
  .rqs-act--disconnect {
    pointer-events: none;
  }

  /* enter 애니메이션: Transition 기반 (아래 -> 위로 슥) */
  .rqs-act-enter-active,
  .rqs-act-appear-active {
    transition:
      transform 720ms cubic-bezier(0.2, 0.9, 0.2, 1),
      opacity 720ms cubic-bezier(0.2, 0.9, 0.2, 1);
  }

  .rqs-act-enter-from,
  .rqs-act-appear-from {
    opacity: 0;
    transform: translateY(48px);
  }

  .rqs-act-enter-to,
  .rqs-act-appear-to {
    opacity: 1;
    transform: translateY(0);
  }

  .rqs-act--disconnect .rqs-act-inner {
    animation: rqs-disconnect 520ms cubic-bezier(0.2, 0.9, 0.2, 1) forwards;
  }

  .rqs-act--disconnect .rqs-act-blackout {
    animation: rqs-blackout 520ms cubic-bezier(0.2, 0.9, 0.2, 1) forwards;
  }

  @media (prefers-reduced-motion: reduce) {
    .rqs-act-enter-active,
    .rqs-act-appear-active,
    .rqs-act--disconnect .rqs-act-inner,
    .rqs-act--disconnect .rqs-act-blackout {
      animation: none !important;
      transition: none !important;
    }
  }
</style>
