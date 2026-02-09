<script setup lang="ts">
  import type { ApiResponseRegSportsClubApplicationResponse } from '~/types/models/ApiResponseRegSportsClubApplicationResponse'
  import type { RegSportsClubApplicationResponse } from '~/types/models/RegSportsClubApplicationResponse'
  import type { CommonCodeGroupResponse } from '~/types/models/CommonCodeGroupResponse'
  import type { ApiResponseCommonCodeGroupResponse } from '~/types/models/ApiResponseCommonCodeGroupResponse'
  import { API_BASE_URL } from '~/constants/url'

  const route = useRoute()
  const router = useRouter()

  // 신청 정보 데이터
  const applicationData = ref<RegSportsClubApplicationResponse | null>(null)
  const isLoading = ref(false)

  // 운영종목 트리 데이터 (운영종목 이름 표시용)
  const operatingSportTree = ref<CommonCodeGroupResponse | null>(null)

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

  // 운영종목 이름 가져오기
  const getOperatingSportName = (
    parentId?: number,
    childId?: number,
  ): string => {
    if (!operatingSportTree.value || !parentId) return '-'

    const parentCode = operatingSportTree.value.codes?.find(
      (code) => code.id === parentId,
    )

    if (!parentCode) return '-'

    const parentName = parentCode.codeName || parentCode.code || '-'

    if (!childId) return parentName

    const childGroup = operatingSportTree.value.children?.find(
      (group) => group.groupCode === parentCode.groupCode,
    )

    const childCode = childGroup?.codes?.find((code) => code.id === childId)
    const childName = childCode?.codeName || childCode?.code || ''

    return childName ? `${parentName} > ${childName}` : parentName
  }

  // 운영종목 트리 조회
  const fetchOperatingSportTree = async () => {
    try {
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/OPERATING_SPORT/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 3,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data) {
        operatingSportTree.value = response.data
      }
    } catch (error) {
      console.error('운영종목 트리 조회 실패:', error)
    }
  }

  // 신청 정보 조회
  const fetchApplicationDetail = async (applyId: number) => {
    try {
      isLoading.value = true
      const response =
        await $fetch<ApiResponseRegSportsClubApplicationResponse>(
          `${API_BASE_URL}/reg-sports-club-applications/${applyId}`,
          {
            method: 'GET',
            credentials: 'include',
          },
        )

      if (response.resultCode === 200 && response.data) {
        applicationData.value = response.data
        // 운영종목 트리도 함께 로드
        await fetchOperatingSportTree()
      } else {
        throw new Error(
          response.resultMessage || '신청 정보 조회에 실패했습니다.',
        )
      }
    } catch (error) {
      console.error('신청 정보 조회 실패:', error)
      const errorMessage =
        error instanceof Error
          ? error.message
          : '신청 정보 조회에 실패했습니다.'
      alert(errorMessage)
      await router.push('/registered-sports-club/list')
    } finally {
      isLoading.value = false
    }
  }

  // applyId 확인 및 처리
  const handleApplyId = async () => {
    const applyIdParam = route.query.applyId

    if (!applyIdParam) {
      alert('신청 ID가 없습니다.')
      await router.push('/registered-sports-club/list')
      return
    }

    const applyId =
      typeof applyIdParam === 'string'
        ? parseInt(applyIdParam, 10)
        : Array.isArray(applyIdParam)
          ? parseInt(applyIdParam[0], 10)
          : applyIdParam

    if (!Number.isFinite(applyId)) {
      alert('유효하지 않은 신청 ID입니다.')
      await router.push('/registered-sports-club/list')
      return
    }

    // 신청 정보 조회
    await fetchApplicationDetail(applyId)
  }

  onMounted(() => {
    handleApplyId()
  })
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="등록 스포츠클럽 신청 상세"
      image-src="/soccer-boy.png"
      image-alt="등록 스포츠클럽 신청 상세 헤더 이미지"
      type="applyPage"
    />

    <!-- 컨텐츠 영역: max-width 1400 / 가운데 정렬 -->
    <div class="mx-auto max-w-[1400px] px-4 pt-12 pb-36">
      <section class="mx-auto mb-8 max-w-[1400px]">
        <h2 class="m-0 text-3xl font-extrabold">등록 스포츠클럽 신청 상세</h2>
      </section>

      <section v-if="isLoading" class="mx-auto w-full max-w-[1400px]">
        <div class="border-grey-300 bg-grey-50 rounded-3xl border p-10">
          <div class="flex items-center justify-center py-20">
            <span class="text-grey-600">로딩 중...</span>
          </div>
        </div>
      </section>

      <section
        v-else-if="applicationData"
        class="mx-auto w-full max-w-[1400px]"
      >
        <div class="border-grey-300 bg-grey-50 rounded-3xl border p-10">
          <!-- 기본 정보 -->
          <div class="mb-8">
            <h3 class="text-grey-800 mb-4 text-xl font-bold">기본 정보</h3>
            <div class="grid gap-4 md:grid-cols-2">
              <div>
                <span class="text-grey-600 text-sm font-medium">신청번호</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.applicationId ?? '-' }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium">신청일시</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{ formatDate(applicationData.appliedAt) }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium">상태</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.codeName ?? '-' }}
                </p>
              </div>
            </div>
          </div>

          <!-- 신청자 정보 -->
          <div class="mb-8">
            <h3 class="text-grey-800 mb-4 text-xl font-bold">신청자 정보</h3>
            <div class="grid gap-4 md:grid-cols-2">
              <div>
                <span class="text-grey-600 text-sm font-medium">신청자명</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.applicantName ?? '-' }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium"
                  >신청자 연락처</span
                >
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.applicantTelno ?? '-' }}
                </p>
              </div>
              <div class="md:col-span-2">
                <span class="text-grey-600 text-sm font-medium"
                  >신청자 이메일</span
                >
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.applicantEmail ?? '-' }}
                </p>
              </div>
            </div>
          </div>

          <!-- 클럽 정보 -->
          <div class="mb-8">
            <h3 class="text-grey-800 mb-4 text-xl font-bold">클럽 정보</h3>
            <div class="grid gap-4 md:grid-cols-2">
              <div>
                <span class="text-grey-600 text-sm font-medium">클럽명</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.clubName ?? '-' }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium">위치</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.location ?? '-' }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium">대표자명</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.representativeName ?? '-' }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium"
                  >대표자 연락처</span
                >
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.representativeTelno ?? '-' }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium"
                  >사업자번호</span
                >
                <p class="text-grey-900 mt-1 text-base">
                  {{ applicationData.businessNo ?? '-' }}
                </p>
              </div>
              <div>
                <span class="text-grey-600 text-sm font-medium">운영종목</span>
                <p class="text-grey-900 mt-1 text-base">
                  {{
                    getOperatingSportName(
                      applicationData.operatingSportParentCodeId,
                      applicationData.operatingSportChildCodeId,
                    )
                  }}
                </p>
              </div>
            </div>
          </div>

          <!-- 하단 버튼 -->
          <div class="border-grey-300 flex justify-end gap-4 border-t pt-6">
            <Button
              color="secondary"
              size="md"
              variant="outlined"
              @click="router.push('/registered-sports-club/list')"
            >
              목록으로
            </Button>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped></style>
