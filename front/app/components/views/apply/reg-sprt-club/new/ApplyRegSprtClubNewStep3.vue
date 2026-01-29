<script setup lang="ts">
  import { useFormContext } from 'vee-validate'
  import { useNewRegSprtClubStore } from '~/stores/newRegSprtClubStore'
  import type { StatusStep } from '~/components/molecules/StatusStepper.vue'
  import StatusStepper from '~/components/molecules/StatusStepper.vue'

  const newRegSprtClubStore = useNewRegSprtClubStore()

  // Step2 폼 데이터 가져오기
  type FormValues = {
    applicantName: string
    applicantTelno: string
    applicantEmail: string
    name: string
    location: string
    representativeName: string
    representativeTelno: string
    businessNo: string
    operatingSportParentCodeId: string | null
    operatingSportChildCodeId: string | null
  }

  const { values } = useFormContext<FormValues>()

  // 프로세스 단계 정의
  const processSteps: StatusStep[] = [
    {
      step: 1,
      title: '신청',
    },
    {
      step: 2,
      title: '접수',
    },
    {
      step: 3,
      title: '검토',
    },
    {
      step: 4,
      title: '승인',
    },
  ]

  // 현재 단계는 신청 완료 상태 (0-based index)
  const currentProcessStep = computed(() => 0)

  // 신청 ID
  const applicationId = computed(() => {
    return newRegSprtClubStore.applicationResponse?.applicationId || null
  })

  // 신청일시
  const appliedAt = computed(() => {
    if (!newRegSprtClubStore.applicationResponse?.appliedAt) {
      return new Date().toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    }
    return new Date(
      newRegSprtClubStore.applicationResponse.appliedAt,
    ).toLocaleString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  })
</script>

<template>
  <section
    class="border-grey-300 bg-grey-50 min-h-[420px] rounded-3xl border p-10"
  >
    <!-- 성공 메시지 -->
    <div class="mb-8 text-center">
      <div
        class="mb-4 inline-flex size-16 items-center justify-center rounded-full bg-[var(--color-primary-700)]"
      >
        <v-icon size="32" color="white">mdi-check</v-icon>
      </div>
      <h3 class="text-grey-900 m-0 mb-2 text-3xl font-bold">
        신청에 성공했습니다!
      </h3>
      <p class="text-grey-600 m-0 mb-2">신청일시: {{ appliedAt }}</p>
      <p v-if="applicationId" class="text-grey-500 m-0 text-sm">
        신청번호: {{ applicationId }}
      </p>
    </div>

    <!-- 프로세스 단계 표시 -->
    <div class="mb-8">
      <h4 class="text-grey-900 mb-4 text-xl font-bold">신청 진행 단계</h4>
      <StatusStepper
        :steps="processSteps"
        :model-value="currentProcessStep"
        :readonly="true"
      />
      <div class="mt-6 grid gap-4 rounded-lg bg-white p-6">
        <div class="flex items-start gap-3">
          <div
            class="flex size-8 shrink-0 items-center justify-center rounded-full bg-[var(--color-primary-700)] text-sm font-bold text-white"
          >
            1
          </div>
          <div class="flex-1">
            <div class="text-grey-900 mb-1 font-bold">신청</div>
            <div class="text-grey-600 text-sm">완료</div>
          </div>
        </div>
        <div class="flex items-start gap-3">
          <div
            class="text-grey-600 flex size-8 shrink-0 items-center justify-center rounded-full bg-[var(--color-grey-200)] text-sm font-bold"
          >
            2
          </div>
          <div class="flex-1">
            <div class="text-grey-900 mb-1 font-bold">접수</div>
            <div class="text-grey-600 text-sm">
              영업일 15일 이내, 시군구체육회 접수
            </div>
          </div>
        </div>
        <div class="flex items-start gap-3">
          <div
            class="text-grey-600 flex size-8 shrink-0 items-center justify-center rounded-full bg-[var(--color-grey-200)] text-sm font-bold"
          >
            3
          </div>
          <div class="flex-1">
            <div class="text-grey-900 mb-1 font-bold">검토</div>
            <div class="text-grey-600 text-sm">
              영업일 15일 이내, 시군구체육회 검토
            </div>
          </div>
        </div>
        <div class="flex items-start gap-3">
          <div
            class="text-grey-600 flex size-8 shrink-0 items-center justify-center rounded-full bg-[var(--color-grey-200)] text-sm font-bold"
          >
            4
          </div>
          <div class="flex-1">
            <div class="text-grey-900 mb-1 font-bold">승인</div>
            <div class="text-grey-600 text-sm">
              시도체육회, 영업일 15일 이내
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 신청 정보 요약 -->
    <div>
      <h4 class="text-grey-900 mb-4 text-xl font-bold">신청 정보</h4>
      <div class="grid gap-4 rounded-lg bg-white p-6">
        <div
          class="border-grey-200 grid grid-cols-[140px_1fr] gap-4 border-b pb-4"
        >
          <div class="text-grey-700 font-semibold">신청자명</div>
          <div class="text-grey-900">{{ values.applicantName || '-' }}</div>
        </div>
        <div
          class="border-grey-200 grid grid-cols-[140px_1fr] gap-4 border-b pb-4"
        >
          <div class="text-grey-700 font-semibold">신청자 전화번호</div>
          <div class="text-grey-900">{{ values.applicantTelno || '-' }}</div>
        </div>
        <div
          class="border-grey-200 grid grid-cols-[140px_1fr] gap-4 border-b pb-4"
        >
          <div class="text-grey-700 font-semibold">신청자 이메일</div>
          <div class="text-grey-900">{{ values.applicantEmail || '-' }}</div>
        </div>
        <div
          class="border-grey-200 grid grid-cols-[140px_1fr] gap-4 border-b pb-4"
        >
          <div class="text-grey-700 font-semibold">클럽명</div>
          <div class="text-grey-900">{{ values.name || '-' }}</div>
        </div>
        <div
          class="border-grey-200 grid grid-cols-[140px_1fr] gap-4 border-b pb-4"
        >
          <div class="text-grey-700 font-semibold">위치</div>
          <div class="text-grey-900">{{ values.location || '-' }}</div>
        </div>
        <div
          class="border-grey-200 grid grid-cols-[140px_1fr] gap-4 border-b pb-4"
        >
          <div class="text-grey-700 font-semibold">대표자명</div>
          <div class="text-grey-900">
            {{ values.representativeName || '-' }}
          </div>
        </div>
        <div
          class="border-grey-200 grid grid-cols-[140px_1fr] gap-4 border-b pb-4"
        >
          <div class="text-grey-700 font-semibold">대표자 전화번호</div>
          <div class="text-grey-900">
            {{ values.representativeTelno || '-' }}
          </div>
        </div>
        <div class="grid grid-cols-[140px_1fr] gap-4">
          <div class="text-grey-700 font-semibold">사업자등록번호</div>
          <div class="text-grey-900">{{ values.businessNo || '-' }}</div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped></style>
