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
    class="px-4 py-12"
    style="
      position: relative;
      background-color: #ffffff;
      opacity: 0;
      transform: translateY(50px);
      transition:
        opacity 0.8s ease-out,
        transform 0.8s ease-out;
    "
    :class="{ 'about-us-area--visible': isAboutUsVisible }"
  >
    <div class="" style="max-width: 1400px; margin: 0 auto">
      <div class="mb-8" style="text-align: center">
        <span
          class="rounded-full px-3 py-1 text-sm font-medium"
          style="
            display: inline-block;
            color: var(--color-primary-700);
            text-transform: uppercase;
            letter-spacing: 0.1em;
            background-color: color-mix(
              in srgb,
              var(--color-primary-700) 10%,
              transparent
            );
          "
          >About Us</span
        >
        <h2
          class="text-grey-900 mx-0 my-1 gap-3 text-3xl font-bold"
          style="display: flex; align-items: center; justify-content: center"
        >
          등록스포츠클럽
        </h2>
      </div>
      <div
        class="rounded-2xl p-6"
        style="
          max-width: 1000px;
          text-align: center;
          margin: 0 auto;
          background: linear-gradient(
            135deg,
            color-mix(in srgb, var(--color-primary-700) 3%, transparent) 0%,
            color-mix(in srgb, var(--color-primary-500) 5%, transparent) 100%
          );
          border-left: 4px solid var(--color-primary-700);
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        "
      >
        <p class="text-grey-600 text-lg" style="line-height: 1.8">
          지역사회 내에서 정기적인 체육활동을 하는 단체가 효율적인 체육활동을
          영위하기 위해<br v-if="!isMobile" />
          「스포츠클럽법」 조항에 근거한 설립 요건을 갖추어 해당 지방자치단체에
          등록하는 제도입니다.<br v-if="!isMobile" />
          스포츠클럽 등록은 의무제가 아닌 자율 등록제입니다.
        </p>
      </div>
    </div>
  </section>
</template>
