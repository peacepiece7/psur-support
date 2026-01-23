<script setup lang="ts">
  import type { RequestStep as StepperStep } from '~/components/molecules/RequestStepper.vue'
  import type { WindowStep } from '~/components/molecules/Window.vue'
  import { usePubGuideStore } from '~/stores/pubGuildStore'

  definePageMeta({ middleware: 'pub-init' })

  const pubGuideStore = usePubGuideStore()
  const containerRef = ref<HTMLElement | null>(null)

  const scrollContainerToTop = async () => {
    await nextTick()
    if (!containerRef.value) return
    const top = containerRef.value.getBoundingClientRect().top + window.scrollY
    window.scrollTo({ top, behavior: 'smooth' })
  }

  const STEPS: StepperStep[] = [
    { step: 1, title: 'Example 1' },
    { step: 2, title: 'Example 2' },
    { step: 3, title: 'Example 3' },
    { step: 4, title: 'Example 4' },
    { step: 5, title: 'Example 5' },
    { step: 6, title: 'Example 6' },
    { step: 7, title: 'Example 7' },
  ]

  const WINDOW_STEPS: WindowStep[] = STEPS.map((s) => ({
    step: s.step,
    title: s.title,
  }))

  const STEP_COMPONENTS = [null, null, null, null, null, null, null] as const

  const currentStep = computed(() => STEPS[pubGuideStore.currentIndex])

  const goPrev = () => {
    pubGuideStore.step.prev()
    scrollContainerToTop()
  }

  const goNext = () => {
    pubGuideStore.step.next()
    scrollContainerToTop()
  }

  const saveDraft = () => {
    console.log('임시저장')
  }
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="Number Input"
      image-src="/soccer-boy.png"
      image-alt="Number Input 헤더 이미지"
      type="applyPage"
    />

    <!-- 컨텐츠 영역: max-width 1400 / 가운데 정렬 -->
    <div ref="containerRef" class="mx-auto max-w-[1400px] px-4 pt-12 pb-36">
      <section class="mb-10">
        <RequestStepper
          v-model="pubGuideStore.currentIndex"
          :steps="STEPS"
          :readonly="false"
          @click="(i) => pubGuideStore.step.setIndex(i)"
        />
      </section>

      <section class="mx-auto mb-8 max-w-[1400px]">
        <div class="mb-4 font-bold">
          <span class="text-new-blue-700"> {{ currentStep.step }}단계 </span>
          <span class="text-grey-600">/ {{ STEPS.length }}단계</span>
        </div>
        <h2 class="m-0 text-3xl font-extrabold">{{ currentStep.title }}</h2>
      </section>

      <section class="mx-auto w-full max-w-[1400px]">
        <Window v-model="pubGuideStore.currentIndex" :steps="WINDOW_STEPS">
          <template #default="{ index }">
            <component :is="STEP_COMPONENTS[index]" />
          </template>
        </Window>
      </section>
    </div>

    <RequestActionWidget>
      <template #default>
        <RequestActionWidgetDefaultButtonGroupExample
          @prev="goPrev"
          @next="goNext"
          @save="saveDraft"
        />
      </template>
    </RequestActionWidget>
  </div>
</template>

<style scoped></style>
