<script setup lang="ts">
  interface ProcessStep {
    step: number
    title: string
    description: string
  }

  const processSteps: ProcessStep[] = [
    {
      step: 1,
      title: '홈페이지',
      description: '등록 신청',
    },
    {
      step: 2,
      title: '스포츠클럽',
      description: '신청서류',
    },
    {
      step: 3,
      title: '시·군·구체육회',
      description: '적정성 검토',
    },
    {
      step: 4,
      title: '지자체',
      description: '등록 여부 결정, 등록증 발급 / 업로드',
    },
    {
      step: 5,
      title: '스포츠클럽 상세',
      description: '등록증 다운로드',
    },
  ]

  const sectionRef = ref<HTMLElement | null>(null)
  const isVisible = ref(false)

  useIntersectionObserver(sectionRef, (entry) => {
    if (entry.isIntersecting) isVisible.value = true
  })

  const handleApply = () => {
    // TODO: 등록 신청 페이지로 이동 또는 모달 열기
    console.log('등록스포츠클럽 신청하기 클릭')
  }
</script>

<template>
  <section
    ref="sectionRef"
    class="bg-grey-100 px-4 py-12"
    style="
      position: relative;
      opacity: 0;
      transform: translateY(50px);
      transition:
        opacity 0.8s ease-out,
        transform 0.8s ease-out;
    "
    :class="{ 'info-reg-process-sec--visible': isVisible }"
  >
    <div class="" style="max-width: 1400px; margin: 0 auto">
      <div class="mb-10" style="text-align: center">
        <h2 class="text-grey-900 m-0 text-3xl font-bold">
          스포츠클럽 등록절차
        </h2>
      </div>
      <div
        class="gap-6"
        style="
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
          align-items: center;
        "
      >
        <div
          v-for="(step, index) in processSteps"
          :key="step.step"
          class="rounded-2xl p-6 shadow"
          :style="{
            '--delay': `${index * 0.1}s`,
            position: 'relative',
            backgroundColor: '#ffffff',
            transition:
              'transform 0.3s ease-out var(--delay), box-shadow 0.3s ease-out var(--delay), opacity 0.3s ease-out var(--delay)',
            opacity: isVisible ? '1' : '0',
            transform: isVisible ? 'translateY(0)' : 'translateY(20px)',
          }"
        >
          <div
            class="mb-4 text-2xl font-bold"
            style="
              display: flex;
              align-items: center;
              justify-content: center;
              width: 48px;
              height: 48px;
              background: linear-gradient(
                135deg,
                var(--color-primary-700) 0%,
                var(--color-primary-800) 100%
              );
              color: #ffffff;
              border-radius: 50%;
              flex-shrink: 0;
            "
          >
            {{ step.step }}
          </div>
          <div class="" style="flex: 1">
            <h3
              class="text-grey-900 text-xl font-bold"
              style="margin: 0 0 0.5rem 0"
            >
              {{ step.title }}
            </h3>
            <p class="text-grey-600 m-0 text-sm" style="line-height: 1.6">
              {{ step.description }}
            </p>
          </div>
          <div
            v-if="index < processSteps.length - 1"
            class="info-reg-process-sec__arrow"
          >
            <v-icon size="24">mdi-arrow-right</v-icon>
          </div>
        </div>
      </div>
      <div
        class="border-grey-200 mt-8 border-t pt-6"
        style="display: flex; justify-content: flex-end"
      >
        <Button
          color="primary"
          size="lg"
          class=""
          style="
            min-width: 200px;
            background-color: var(--color-primary-700) !important;
            color: #ffffff !important;
          "
          append-icon="mdi-arrow-right"
          @click="handleApply"
        >
          스포츠클럽 등록하기
        </Button>
      </div>
    </div>
  </section>
</template>

<style scoped>
  .info-reg-process-sec--visible {
    opacity: 1 !important;
    transform: translateY(0) !important;
  }

  .info-reg-process-sec__arrow {
    position: absolute;
    right: -24px;
    top: 50%;
    transform: translateY(-50%);
    color: var(--color-primary-700);
    display: flex;
    align-items: center;
    justify-content: center;

    @media (max-width: 1024px) {
      position: static;
      transform: none;
      right: auto;
      top: auto;
    }
  }

  @media (max-width: 1024px) {
    .info-reg-process-sec__arrow {
      display: none;
    }
  }
</style>
