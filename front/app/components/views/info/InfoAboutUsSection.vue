<script setup lang="ts">
  const { isMobile } = useDevice()

  const aboutUsRef = ref<HTMLElement | null>(null)
  const isAboutUsVisible = ref(false)

  // About Us 섹션 관찰
  useIntersectionObserver(aboutUsRef, (entry) => {
    if (entry.isIntersecting) isAboutUsVisible.value = true
  })
</script>

<template>
  <section
    ref="aboutUsRef"
    class="relative bg-white px-4 py-12 transition-all duration-1000 ease-out"
    :class="{
      'translate-y-[50px] opacity-0': !isAboutUsVisible,
      'translate-y-0 opacity-100': isAboutUsVisible,
    }"
  >
    <div class="mx-auto max-w-[1400px]">
      <div class="mb-8 text-center">
        <span
          class="text-new-blue-700 inline-block rounded-full px-3 py-1 text-sm font-medium tracking-wider uppercase"
          style="
            background-color: color-mix(
              in srgb,
              var(--color-primary-700) 10%,
              transparent
            );
          "
          >About Us</span
        >
        <h2
          class="text-grey-900 mx-0 my-1 flex items-center justify-center gap-3 text-3xl font-bold"
        >
          스포츠클럽이 뭔가요?
        </h2>
      </div>
      <div
        class="border-new-blue-700 mx-auto max-w-[1000px] rounded-2xl border-l-4 p-6 text-center shadow-[0_2px_8px_rgba(0,0,0,0.05)]"
        style="
          background: linear-gradient(
            135deg,
            color-mix(in srgb, var(--color-primary-700) 3%, transparent) 0%,
            color-mix(in srgb, var(--color-primary-500) 5%, transparent) 100%
          );
        "
      >
        <p class="text-grey-600 text-lg leading-[1.8]">
          스포츠클럽은 회원의 정기적인 체육활동을 위하여 법률(스포츠클럽법
          6조)에 따라<br v-if="!isMobile" />
          등록을 하고 지역사회의 체육활동 진흥을 위하여 운영되는 법인 또는
          단체를 말하며,<br v-if="!isMobile" />
          스포츠클럽은 등록 제도와 지정 제도가 있습니다.
        </p>
      </div>
    </div>
  </section>
</template>
