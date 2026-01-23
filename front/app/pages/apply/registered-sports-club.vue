<script setup lang="ts">
  import { useNewRegSprtClubStore } from '~/stores/newRegSprtClubStore'
  import type { RequestStep as StepperStep } from '~/components/molecules/RequestStepper.vue'
  import type { WindowStep } from '~/components/molecules/Window.vue'
  import ApplyRegSprtClubNewStep1 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep1.vue'
  import ApplyRegSprtClubNewStep2 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep2.vue'
  import ApplyRegSprtClubNewStep3 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep3.vue'
  import ApplyRegSprtClubNewStep4 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep4.vue'
  import ApplyRegSprtClubNewStep5 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep5.vue'
  import ApplyRegSprtClubNewStep6 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep6.vue'
  import ApplyRegSprtClubNewStep7 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep7.vue'

  const newRegSprtClubStore = useNewRegSprtClubStore()
  const applyContainerRef = ref<HTMLElement | null>(null)

  const scrollApplyContainerToTop = async () => {
    await nextTick()
    if (!applyContainerRef.value) return
    const top =
      applyContainerRef.value.getBoundingClientRect().top + window.scrollY
    window.scrollTo({ top, behavior: 'smooth' })
  }

  const STEPS: StepperStep[] = [
    { step: 1, title: '유의사항 확인' },
    { step: 2, title: '필수 서류 준비' },
    { step: 3, title: '클럽 정보 입력' },
    { step: 4, title: '클럽 회원 정보 입력' },
    { step: 5, title: '서류 제출' },
    { step: 6, title: '등록증 확인' },
    { step: 7, title: '신청 완료' },
  ]

  const WINDOW_STEPS: WindowStep[] = STEPS.map((s) => ({
    step: s.step,
    title: s.title,
  }))

  const STEP_COMPONENTS = [
    ApplyRegSprtClubNewStep1,
    ApplyRegSprtClubNewStep2,
    ApplyRegSprtClubNewStep3,
    ApplyRegSprtClubNewStep4,
    ApplyRegSprtClubNewStep5,
    ApplyRegSprtClubNewStep6,
    ApplyRegSprtClubNewStep7,
  ] as const

  const currentStep = computed(() => STEPS[newRegSprtClubStore.currentIndex])

  const canPrev = computed(() => newRegSprtClubStore.currentIndex > 0)
  const canNext = computed(
    () => newRegSprtClubStore.currentIndex < STEPS.length - 1,
  )

  const goPrev = () => {
    if (!canPrev.value) return
    newRegSprtClubStore.actions.step.prev()
    scrollApplyContainerToTop()
  }

  const goNext = () => {
    if (!canNext.value) return
    newRegSprtClubStore.actions.step.next()
    scrollApplyContainerToTop()
  }

  const noticeAgreed = ref(false)
  const canProceedNotice = computed(() => noticeAgreed.value && canNext.value)

  const saveDraft = () => {
    // TODO: 임시저장 API 연결
    console.log('임시저장')
  }
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="등록 스포츠클럽 신청"
      image-src="/soccer-boy.png"
      image-alt="등록 스포츠클럽 신청 헤더 이미지"
      type="applyPage"
    />

    <!-- 컨텐츠 영역: max-width 1400 / 가운데 정렬 -->
    <div
      ref="applyContainerRef"
      class="mx-auto max-w-[1400px] px-4 pt-12 pb-36"
    >
      <section class="mb-10">
        <RequestStepper
          v-model="newRegSprtClubStore.currentIndex"
          :steps="STEPS"
          :disabled-indices="[0, 6]"
          :readonly="false"
          @click="(i) => newRegSprtClubStore.actions.step.setIndex(i)"
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
        <Window
          v-model="newRegSprtClubStore.currentIndex"
          :steps="WINDOW_STEPS"
        >
          <template #default="{ index }">
            <component :is="STEP_COMPONENTS[index]" />
          </template>
        </Window>
      </section>
    </div>

    <!-- step1(유의사항 확인) 전용 하단 액션 -->
    <div
      v-if="newRegSprtClubStore.currentIndex === 0"
      class="w-full px-4 pb-12"
    >
      <div class="mx-auto max-w-[1400px]">
        <div class="flex items-center justify-end gap-4">
          <Button color="primary" size="lg" @click="goNext">
            <!-- :disabled="!canProceedNotice" -->
            <span class="tracking-[0.1rem]">진행하기</span>
          </Button>
        </div>
      </div>
    </div>

    <!-- ! @TODO: 유의사항 컨텐츠가 출력될 경우 하단 위젯 비노출 -->
    <!-- <template v-if="newRegSprtClubStore.currentIndex !== 0"> -->
    <!-- 공중에 떠있는 하단 위젯 (컨텐츠 변경에도 위치 고정) -->
    <RequestActionWidget
      v-show="newRegSprtClubStore.currentIndex !== 0"
      :disconnect="newRegSprtClubStore.currentIndex === 6"
    >
      <template #default>
        <RequestActionWidgetDefaultButtonGroupExample
          :can-prev="canPrev"
          :can-next="canNext"
          @prev="goPrev"
          @next="goNext"
          @save="saveDraft"
        />
      </template>
    </RequestActionWidget>
    <!-- </template> -->
  </div>
</template>

<style scoped></style>
