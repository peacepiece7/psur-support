<script setup lang="ts">
  import type { RequestStep as StepperStep } from '~/components/molecules/RequestStepper.vue'
  import type { WindowStep } from '~/components/molecules/Window.vue'
  import { usePubGuideStore } from '~/stores/pubGuildStore'
  import PubAtomStep1 from '~/components/views/pub/atom/inputs/PubAtomInputStep1.vue'
  // import PubAtomStep2 from '~/components/views/pub/atom/inputs/PubAtomInputStep2.vue'
  import PubAtomStep3 from '~/components/views/pub/atom/inputs/PubAtomInputStep3.vue'
  import PubAtomStep4 from '~/components/views/pub/atom/inputs/PubAtomInputStep4.vue'
  import PubAtomStep5 from '~/components/views/pub/atom/inputs/PubAtomInputStep5.vue'
  import PubAtomStep6 from '~/components/views/pub/atom/inputs/PubAtomInputStep6.vue'
  import PubAtomStep7 from '~/components/views/pub/atom/inputs/PubAtomInputStep7.vue'
  import PubAtomStep8 from '~/components/views/pub/atom/inputs/PubAtomInputStep8.vue'

  definePageMeta({
    middleware: 'pub-init',
    title: 'Atom Components',
    description: 'Atom 컴포넌트 데모 페이지',
  })

  const pubGuideStore = usePubGuideStore()
  const containerRef = ref<HTMLElement | null>(null)

  const scrollContainerToTop = async () => {
    await nextTick()
    if (!containerRef.value) return
    const top = containerRef.value.getBoundingClientRect().top + window.scrollY
    window.scrollTo({ top, behavior: 'smooth' })
  }

  const STEPS: StepperStep[] = [
    { step: 1, title: 'Input' },
    { step: 2, title: '-' },
    { step: 3, title: 'Picker' },
    { step: 4, title: 'Textarea' },
    { step: 5, title: 'Select' },
    { step: 6, title: 'Divider' },
    { step: 7, title: 'Selection' },
    { step: 8, title: 'Composite' },
  ]

  const WINDOW_STEPS: WindowStep[] = STEPS.map((s) => ({
    step: s.step,
    title: s.title,
  }))

  const STEP_COMPONENTS = [
    PubAtomStep1,
    null,
    PubAtomStep3,
    PubAtomStep4,
    PubAtomStep5,
    PubAtomStep6,
    PubAtomStep7,
    PubAtomStep8,
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
