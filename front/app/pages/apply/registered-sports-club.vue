<script setup lang="ts">
  import { useForm, useFormContext } from 'vee-validate'
  import { useNewRegSprtClubStore } from '~/stores/newRegSprtClubStore'
  import type { RequestStep as StepperStep } from '~/components/molecules/RequestStepper.vue'
  import type { WindowStep } from '~/components/molecules/Window.vue'
  import ApplyRegSprtClubNewStep1 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep1.vue'
  import ApplyRegSprtClubNewStep2 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep2.vue'
  import ApplyRegSprtClubNewStep3 from '~/components/views/apply/reg-sprt-club/new/ApplyRegSprtClubNewStep3.vue'

  const route = useRoute()
  const newRegSprtClubStore = useNewRegSprtClubStore()
  const applyContainerRef = ref<HTMLElement | null>(null)

  onMounted(() => {
    if (route.query.applyId != null) {
      newRegSprtClubStore.actions.setApplyId(route.query.applyId)
    }
  })

  // 폼 타입 정의
  type FormValues = {
    applicantName: string
    applicantTelno: string
    applicantEmail: string
    name: string
    location: string
    representativeName: string
    representativeTelno: string
    businessNo: string
    operatingSportParentCodeId: { title: string; value: string; id?: number } | null
    operatingSportChildCodeId: { title: string; value: string; id?: number } | null
  }

  // useForm 설정 (validation 없이)
  const { values } = useForm<FormValues>({
    initialValues: {
      applicantName: '',
      applicantTelno: '',
      applicantEmail: '',
      name: '',
      location: '',
      representativeName: '',
      representativeTelno: '',
      businessNo: '',
      operatingSportParentCodeId: null,
      operatingSportChildCodeId: null,
    },
  })

  const scrollApplyContainerToTop = async () => {
    await nextTick()
    if (!applyContainerRef.value) return
    const top =
      applyContainerRef.value.getBoundingClientRect().top + window.scrollY
    window.scrollTo({ top, behavior: 'smooth' })
  }

  const STEPS: StepperStep[] = [
    { step: 1, title: '유의사항 확인' },
    { step: 2, title: '클럽 정보 입력' },
    { step: 3, title: '신청 완료' },
  ]

  const WINDOW_STEPS: WindowStep[] = STEPS.map((s) => ({
    step: s.step,
    title: s.title,
  }))

  const STEP_COMPONENTS = [
    ApplyRegSprtClubNewStep1,
    ApplyRegSprtClubNewStep2,
    ApplyRegSprtClubNewStep3,
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

  const formSnapshot = () => ({
    applicantName: values.applicantName,
    applicantTelno: values.applicantTelno,
    applicantEmail: values.applicantEmail,
    name: values.name,
    location: values.location,
    representativeName: values.representativeName,
    representativeTelno: values.representativeTelno,
    businessNo: values.businessNo,
    operatingSportParentCodeId: values.operatingSportParentCodeId,
    operatingSportChildCodeId: values.operatingSportChildCodeId,
  })

  // step2에서 임시저장 (저장 API)
  const saveDraft = async () => {
    try {
      await newRegSprtClubStore.actions.saveApplication(formSnapshot())
      alert('저장되었습니다.')
    } catch (error) {
      console.error('저장 실패:', error)
      alert('저장에 실패했습니다. 다시 시도해주세요.')
    }
  }

  // step2에서 신청하기 (신청 API)
  const handleSubmit = async () => {
    try {
      await newRegSprtClubStore.actions.applyApplication(formSnapshot())
      newRegSprtClubStore.actions.step.setIndex(2)
      scrollApplyContainerToTop()
    } catch (error) {
      console.error('신청 실패:', error)
      alert('신청에 실패했습니다. 다시 시도해주세요.')
    }
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
          :readonly="true"
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

    <!-- 공중에 떠있는 하단 위젯 (컨텐츠 변경에도 위치 고정) -->
    <RequestActionWidget
      v-show="
        newRegSprtClubStore.currentIndex !== 0 &&
        newRegSprtClubStore.currentIndex !== 2
      "
      :disconnect="newRegSprtClubStore.currentIndex === 2"
    >
      <template #default>
        <div class="flex w-full items-center gap-3">
          <!-- 왼쪽: 이전 버튼 -->
          <Button
            color="secondary"
            size="md"
            :disabled="!canPrev"
            @click="goPrev"
          >
            이전
          </Button>

          <!-- 왼쪽 spacer -->
          <div class="relative z-[1] flex-1" />

          <!-- 가운데: 임시저장, 신청 버튼 -->
          <div class="flex items-center gap-3">
            <Button
              color="tertiary"
              size="md"
              variant="outlined"
              @click="saveDraft"
            >
              임시저장
            </Button>
            <Button
              v-if="newRegSprtClubStore.currentIndex === 1"
              color="primary"
              size="md"
              @click="handleSubmit"
            >
              <span class="tracking-[0.1rem]">신청</span>
            </Button>
          </div>

          <!-- 오른쪽: 다음 버튼 -->
          <Button
            color="primary"
            size="md"
            :disabled="!canNext"
            @click="goNext"
          >
            다음
          </Button>
        </div>
      </template>
    </RequestActionWidget>
  </div>
</template>

<style scoped></style>
