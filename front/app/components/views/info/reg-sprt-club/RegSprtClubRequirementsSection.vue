<script setup lang="ts">
  interface Requirement {
    text: string
  }

  const requirements: Requirement[] = [
    {
      text: '스포츠클럽의 운영 및 의사결정 등에 관한 정관이 있을 것',
    },
    {
      text: '스포츠클럽 연간 운영계획서를 보유하고 있을 것',
    },
    {
      text: '대표자 및 스포츠클럽 회원의 대의기구가 있을 것',
    },
    {
      text: '정기적으로 회비를 납부하고 활동하는 회원의 수를 상시적으로 대통령령으로 정하는 수 이상(종목별 10명 이상)으로 유지할 것',
    },
    {
      text: '그 밖에 스포츠클럽 등록에 관하여 대통령령으로 정하는 사항',
    },
  ]

  const sectionRef = ref<HTMLElement | null>(null)
  const isVisible = ref(false)

  useIntersectionObserver(sectionRef, (entry) => {
    if (entry.isIntersecting) isVisible.value = true
  })
</script>

<template>
  <section
    ref="sectionRef"
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
    :class="{ 'inf-reg-requirements-sec--visible': isVisible }"
  >
    <div class="" style="max-width: 1400px; margin: 0 auto">
      <div class="mb-8" style="text-align: center">
        <h2 class="text-grey-900 m-0 text-3xl font-bold">
          등록스포츠클럽 요건
        </h2>
      </div>
      <div
        class="gap-4"
        style="
          max-width: 900px;
          margin: 0 auto;
          display: flex;
          flex-direction: column;
        "
      >
        <div
          v-for="(requirement, index) in requirements"
          :key="index"
          class="gap-4 rounded-xl p-5"
          :style="{
            '--delay': `${index * 0.1}s`,
            display: 'flex',
            alignItems: 'flex-start',
            background:
              'linear-gradient(135deg, color-mix(in srgb, var(--color-primary-700) 3%, transparent) 0%, color-mix(in srgb, var(--color-primary-500) 5%, transparent) 100%)',
            borderLeft: '4px solid var(--color-primary-700)',
            opacity: isVisible ? '1' : '0',
            transform: isVisible ? 'translateX(0)' : 'translateX(-20px)',
            transition:
              'opacity 0.5s ease-out var(--delay), transform 0.5s ease-out var(--delay)',
          }"
        >
          <div class="mt-1" style="flex-shrink: 0">
            <v-icon color="primary">mdi-check-circle</v-icon>
          </div>
          <p
            class="text-grey-900 m-0 text-base"
            style="line-height: 1.8; flex: 1"
          >
            {{ requirement.text }}
          </p>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
  .inf-reg-requirements-sec--visible {
    opacity: 1 !important;
    transform: translateY(0) !important;
  }
</style>
