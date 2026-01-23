<script setup lang="ts">
  import type { RequestStep } from '~/components/molecules/RequestStepper.vue'
  import type { StatusStep } from '~/components/molecules/StatusStepper.vue'
  import type { ProgressStep } from '~/components/molecules/ProgressIndicator.vue'

  // RequestStepper 예제 데이터
  const requestSteps = ref<RequestStep[]>([
    { step: 1, title: '신청 정보' },
    { step: 2, title: '등록 지역' },
    { step: 3, title: '서류 첨부' },
    { step: 4, title: '정보 확인' },
    { step: 5, title: '신청서 첨부' },
    { step: 6, title: '완료' },
  ])
  const requestCurrentStep = ref(0)

  // RequestStepper disabledIndices 예제 데이터
  const requestStepsWithDisabled = ref<RequestStep[]>([
    { step: 1, title: '신청 정보' },
    { step: 2, title: '등록 지역' },
    { step: 3, title: '서류 첨부' },
    { step: 4, title: '정보 확인' },
    { step: 5, title: '신청서 첨부' },
    { step: 6, title: '완료' },
  ])
  const requestCurrentStepWithDisabled = ref(0)
  // 첫 번째(인덱스 0)와 마지막(인덱스 5) 스텝을 클릭 불가능하게 설정
  const requestDisabledIndices = [0, 5]

  // StatusStepper 예제 데이터
  const statusSteps = ref<StatusStep[]>([
    { step: 1, title: '신청' },
    { step: 2, title: '접수' },
    { step: 3, title: '검토' },
    { step: 4, title: '반려' },
  ])
  const statusCurrentStep = ref(0)
  const statusRejectedIndices = computed(() => {
    // 현재 스텝이 반려(인덱스 3)일 때 반려 색상 표시
    return statusCurrentStep.value === 3 ? [3] : []
  })

  // StatusStepper readonly=false 예제 데이터
  const statusStepsEditable = ref<StatusStep[]>([
    { step: 1, title: '신청' },
    { step: 2, title: '접수' },
    { step: 3, title: '검토' },
    { step: 4, title: '반려' },
  ])
  const statusCurrentStepEditable = ref(0)
  const statusRejectedIndicesEditable = computed(() => {
    // 현재 스텝이 반려(인덱스 3)일 때 반려 색상 표시
    return statusCurrentStepEditable.value === 3 ? [3] : []
  })

  // RequestStepper labelMode 예제 데이터
  const requestStepsSide = ref<RequestStep[]>([
    { step: 1, title: '신청 정보' },
    { step: 2, title: '등록 지역' },
    { step: 3, title: '서류 첨부' },
    { step: 4, title: '정보 확인' },
    { step: 5, title: '신청서 첨부' },
    { step: 6, title: '완료' },
  ])
  const requestCurrentStepSide = ref(0)

  const requestStepsDown = ref<RequestStep[]>([
    { step: 1, title: '신청 정보' },
    { step: 2, title: '등록 지역' },
    { step: 3, title: '서류 첨부' },
    { step: 4, title: '정보 확인' },
    { step: 5, title: '신청서 첨부' },
    { step: 6, title: '완료' },
  ])
  const requestCurrentStepDown = ref(0)

  // ProgressIndicator 예제 데이터
  const progressSteps = ref<ProgressStep[]>([
    { step: 1, title: '신청 정보' },
    { step: 2, title: '등록 지역' },
    { step: 3, title: '서류 첨부' },
    { step: 4, title: '정보 확인' },
  ])
  const progressCurrent = ref(1)

  // ProgressIndicator reject 예제 데이터
  const progressStepsReject = ref<ProgressStep[]>([
    { step: 1, title: '신청' },
    { step: 2, title: '접수' },
    { step: 3, title: '검토' },
    { step: 4, title: '반려' },
  ])
  const progressCurrentReject = ref(2)
  const progressReject = computed(() => {
    // 현재 스텝이 반려(인덱스 3)일 때 reject 표시
    return progressCurrentReject.value === 3
  })
</script>

<template>
  <div class="bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-[1400px]">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">Stepper 예제</h1>

      <!-- RequestStepper 예제 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">RequestStepper</h2>

        <div class="mb-6">
          <RequestStepper
            v-model="requestCurrentStep"
            :steps="requestSteps"
            @click="(index) => (requestCurrentStep = index)"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="requestCurrentStep === 0"
            @click="requestCurrentStep = Math.max(0, requestCurrentStep - 1)"
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="requestCurrentStep === requestSteps.length - 1"
            @click="
              requestCurrentStep = Math.min(
                requestSteps.length - 1,
                requestCurrentStep + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ requestCurrentStep + 1 }} / {{ requestSteps.length }}
        </p>
      </section>

      <!-- RequestStepper disabledIndices 예제 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">
          RequestStepper (disabledIndices 사용)
        </h2>

        <div class="mb-6">
          <RequestStepper
            v-model="requestCurrentStepWithDisabled"
            :steps="requestStepsWithDisabled"
            :disabled-indices="requestDisabledIndices"
            @click="(index) => (requestCurrentStepWithDisabled = index)"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="requestCurrentStepWithDisabled === 0"
            @click="
              requestCurrentStepWithDisabled = Math.max(
                0,
                requestCurrentStepWithDisabled - 1,
              )
            "
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="
              requestCurrentStepWithDisabled ===
              requestStepsWithDisabled.length - 1
            "
            @click="
              requestCurrentStepWithDisabled = Math.min(
                requestStepsWithDisabled.length - 1,
                requestCurrentStepWithDisabled + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ requestCurrentStepWithDisabled + 1 }} /
          {{ requestStepsWithDisabled.length }}
        </p>
        <p class="text-grey-500 mt-2 text-xs">
          첫 번째(1단계)와 마지막(6단계) 스텝은 클릭 불가능 (disabledIndices=[0,
          5])
        </p>
      </section>

      <!-- StatusStepper 예제 (readonly=true, 기본값) -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">
          StatusStepper (readonly=true, 기본값)
        </h2>

        <div class="mb-6">
          <StatusStepper
            v-model="statusCurrentStep"
            :steps="statusSteps"
            :rejected-indices="statusRejectedIndices"
            @click="(index) => (statusCurrentStep = index)"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="statusCurrentStep === 0"
            @click="statusCurrentStep = Math.max(0, statusCurrentStep - 1)"
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="statusCurrentStep === statusSteps.length - 1"
            @click="
              statusCurrentStep = Math.min(
                statusSteps.length - 1,
                statusCurrentStep + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ statusCurrentStep + 1 }} / {{ statusSteps.length }}
        </p>
        <p class="text-grey-500 mt-2 text-xs">
          클릭 불가 (readonly=true가 기본값)
        </p>
      </section>

      <!-- StatusStepper 예제 (readonly=false, 클릭 가능) -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">
          StatusStepper (readonly=false, 클릭 가능)
        </h2>

        <div class="mb-6">
          <StatusStepper
            v-model="statusCurrentStepEditable"
            :steps="statusStepsEditable"
            :rejected-indices="statusRejectedIndicesEditable"
            :readonly="false"
            @click="(index) => (statusCurrentStepEditable = index)"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="statusCurrentStepEditable === 0"
            @click="
              statusCurrentStepEditable = Math.max(
                0,
                statusCurrentStepEditable - 1,
              )
            "
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="
              statusCurrentStepEditable === statusStepsEditable.length - 1
            "
            @click="
              statusCurrentStepEditable = Math.min(
                statusStepsEditable.length - 1,
                statusCurrentStepEditable + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ statusCurrentStepEditable + 1 }} /
          {{ statusStepsEditable.length }}
        </p>
        <p class="text-grey-500 mt-2 text-xs">
          스텝을 직접 클릭하여 이동 가능 (readonly=false)
        </p>
      </section>

      <!-- RequestStepper labelMode="side" 예제 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">
          RequestStepper (labelMode="side")
        </h2>

        <div class="mb-6">
          <RequestStepper
            v-model="requestCurrentStepSide"
            :steps="requestStepsSide"
            label-mode="side"
            @click="(index) => (requestCurrentStepSide = index)"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="requestCurrentStepSide === 0"
            @click="
              requestCurrentStepSide = Math.max(0, requestCurrentStepSide - 1)
            "
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="requestCurrentStepSide === requestStepsSide.length - 1"
            @click="
              requestCurrentStepSide = Math.min(
                requestStepsSide.length - 1,
                requestCurrentStepSide + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ requestCurrentStepSide + 1 }} /
          {{ requestStepsSide.length }}
        </p>
        <p class="text-grey-500 mt-2 text-xs">
          레이블이 dot 옆에 표시됨 (labelMode="side")
        </p>
      </section>

      <!-- RequestStepper labelMode="down" 예제 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">
          RequestStepper (labelMode="down")
        </h2>

        <div class="mb-6">
          <RequestStepper
            v-model="requestCurrentStepDown"
            :steps="requestStepsDown"
            label-mode="down"
            @click="(index) => (requestCurrentStepDown = index)"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="requestCurrentStepDown === 0"
            @click="
              requestCurrentStepDown = Math.max(0, requestCurrentStepDown - 1)
            "
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="requestCurrentStepDown === requestStepsDown.length - 1"
            @click="
              requestCurrentStepDown = Math.min(
                requestStepsDown.length - 1,
                requestCurrentStepDown + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ requestCurrentStepDown + 1 }} /
          {{ requestStepsDown.length }}
        </p>
        <p class="text-grey-500 mt-2 text-xs">
          레이블이 dot 아래에 표시됨 (labelMode="down", 기본값)
        </p>
      </section>

      <!-- ProgressIndicator 예제 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">ProgressIndicator</h2>

        <div class="mb-6">
          <ProgressIndicator
            :steps="progressSteps"
            :current="progressCurrent"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="progressCurrent === 0"
            @click="progressCurrent = Math.max(0, progressCurrent - 1)"
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="progressCurrent === progressSteps.length - 1"
            @click="
              progressCurrent = Math.min(
                progressSteps.length - 1,
                progressCurrent + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ progressCurrent + 1 }} / {{ progressSteps.length }}
        </p>
        <p class="text-grey-500 mt-2 text-xs">
          원형 progress indicator와 현재/다음 단계 라벨 표시
        </p>
      </section>

      <!-- ProgressIndicator reject 예제 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">
          ProgressIndicator (reject 상태)
        </h2>

        <div class="mb-6">
          <ProgressIndicator
            :steps="progressStepsReject"
            :current="progressCurrentReject"
            :reject="progressReject"
          />
        </div>

        <div class="mt-6 flex gap-4">
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="progressCurrentReject === 0"
            @click="
              progressCurrentReject = Math.max(0, progressCurrentReject - 1)
            "
          >
            이전
          </button>
          <button
            type="button"
            class="bg-primary-600 hover:bg-primary-700 rounded-lg px-4 py-2 text-white"
            :disabled="progressCurrentReject === progressStepsReject.length - 1"
            @click="
              progressCurrentReject = Math.min(
                progressStepsReject.length - 1,
                progressCurrentReject + 1,
              )
            "
          >
            다음
          </button>
        </div>

        <p class="text-grey-600 mt-4 text-sm">
          현재 단계: {{ progressCurrentReject + 1 }} /
          {{ progressStepsReject.length }}
        </p>
        <p class="text-grey-500 mt-2 text-xs">
          반려 단계(4단계)일 때 빨간색으로 표시됨 (reject=true)
        </p>
      </section>
    </div>
  </div>
</template>

<style scoped lang="scss"></style>
