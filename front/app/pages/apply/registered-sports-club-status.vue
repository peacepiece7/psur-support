<script setup lang="ts">
  import type { RegSportsClubApplicationResponse } from '~/types/models/RegSportsClubApplicationResponse'
  import type { ApiResponseListRegSportsClubApplicationResponse } from '~/types/models/ApiResponseListRegSportsClubApplicationResponse'
  import type { DataTableHeader } from '~/components/molecules/Table.vue'
  import Table from '~/components/molecules/Table.vue'
  import { API_BASE_URL } from '~/constants/url'

  // 데이터 상태
  const applications = ref<RegSportsClubApplicationResponse[]>([])
  const isLoading = ref(false)

  // 테이블 헤더 정의
  const headers: DataTableHeader[] = [
    {
      title: '신청번호',
      key: 'applicationId',
      width: '120px',
      align: 'center',
    },
    {
      title: '신청일시',
      key: 'appliedAt',
      width: '180px',
      align: 'center',
    },
    {
      title: '클럽명',
      key: 'clubName',
      width: '200px',
    },
    {
      title: '신청자명',
      key: 'applicantName',
      width: '120px',
      align: 'center',
    },
    {
      title: '대표자명',
      key: 'representativeName',
      width: '120px',
      align: 'center',
    },
    {
      title: '위치',
      key: 'location',
      width: '200px',
    },
    {
      title: '상태',
      key: 'codeName',
      width: '120px',
      align: 'center',
    },
  ]

  // 신청일시 포맷팅
  const formatDate = (dateString?: string) => {
    if (!dateString) return '-'
    try {
      const date = new Date(dateString)
      return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
      })
    } catch {
      return dateString
    }
  }

  // 테이블 아이템 변환 (날짜 포맷팅 등)
  const tableItems = computed(() => {
    return applications.value.map((app) => ({
      ...app,
      appliedAt: formatDate(app.appliedAt),
      applicationId: app.applicationId ?? '-',
      clubName: app.clubName ?? '-',
      applicantName: app.applicantName ?? '-',
      representativeName: app.representativeName ?? '-',
      location: app.location ?? '-',
      codeName: app.codeName ?? '-',
    }))
  })

  // 신청 현황 목록 조회
  const fetchApplications = async () => {
    try {
      isLoading.value = true
      const response = await $fetch<ApiResponseListRegSportsClubApplicationResponse>(
        `${API_BASE_URL}/reg-sports-club-applications`,
        {
          method: 'GET',
          credentials: 'include',
        },
      )

      if (response.resultCode === 200 && response.data) {
        applications.value = response.data
      } else {
        console.error('신청 현황 조회 실패:', response.resultMessage)
        applications.value = []
      }
    } catch (error) {
      console.error('신청 현황 조회 중 오류 발생:', error)
      applications.value = []
    } finally {
      isLoading.value = false
    }
  }

  // 페이지 마운트 시 데이터 로드
  onMounted(() => {
    fetchApplications()
  })
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="등록스포츠클럽 신청 현황"
      image-src="/soccer-boy.png"
      image-alt="등록스포츠클럽 신청 현황 헤더 이미지"
      type="applyPage"
    />

    <!-- 컨텐츠 영역: max-width 1400 / 가운데 정렬 -->
    <div class="mx-auto max-w-[1400px] px-4 pt-12 pb-36">
      <section class="mx-auto mb-8 max-w-[1400px]">
        <h2 class="m-0 text-3xl font-extrabold">등록스포츠클럽 신청 현황</h2>
      </section>

      <section class="mx-auto w-full max-w-[1400px]">
        <div class="rounded-3xl border border-grey-300 bg-grey-50 p-10">
          <Table
            :headers="headers"
            :items="tableItems"
            :loading="isLoading"
            :items-per-page="-1"
            :total-items="applications.length"
          />
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped></style>
