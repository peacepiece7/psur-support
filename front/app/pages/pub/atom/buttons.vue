<script setup lang="ts">
  import type { RequestStep as StepperStep } from '~/components/molecules/RequestStepper.vue'
  import type { WindowStep } from '~/components/molecules/Window.vue'
  import { usePubGuideStore } from '~/stores/pubGuildStore'
  import PubAtomButtonStep1 from '~/components/views/pub/atom/buttons/PubAtomButtonStep1.vue'
  import PubAtomButtonStep2 from '~/components/views/pub/atom/buttons/PubAtomButtonStep2.vue'
  import PubAtomButtonStep3 from '~/components/views/pub/atom/buttons/PubAtomButtonStep3.vue'

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
    { step: 1, title: 'Button' },
    { step: 2, title: 'Badge' },
    { step: 3, title: 'Icon' },
  ]

  const WINDOW_STEPS: WindowStep[] = STEPS.map((s) => ({
    step: s.step,
    title: s.title,
  }))

  const STEP_COMPONENTS = [
    PubAtomButtonStep1,
    PubAtomButtonStep2,
    PubAtomButtonStep3,
  ] as const

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
      title="Atom Components"
      image-src="/soccer-boy.png"
      image-alt="Atom 헤더 이미지"
      type="applyPage"
    />

    <!-- 컨텐츠 영역: max-width 1400 / 가운데 정렬 -->
    <div ref="containerRef" class="mx-auto max-w-[1400px] px-4 pt-12 pb-36">
      <section class="mx-auto">
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

      <ClientOnly>
        <section class="mx-auto w-full max-w-[1400px]">
          <Window v-model="pubGuideStore.currentIndex" :steps="WINDOW_STEPS">
            <template #default="{ index }">
              <component :is="STEP_COMPONENTS[index]" />
            </template>
          </Window>
        </section>
      </ClientOnly>
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
