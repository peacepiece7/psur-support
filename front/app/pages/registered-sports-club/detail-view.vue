<script setup lang="ts">
  import type { ApiResponseMeDetailResponse } from '~/types/models/ApiResponseMeDetailResponse'
  import type { ApiResponseVoid } from '~/types/models/ApiResponseVoid'
  import type { ApiResponseRegSportsClubApplicationResponse } from '~/types/models/ApiResponseRegSportsClubApplicationResponse'
  import type { RegSportsClubApplicationResponse } from '~/types/models/RegSportsClubApplicationResponse'
  import type { CommonCodeGroupResponse } from '~/types/models/CommonCodeGroupResponse'
  import type { ApiResponseCommonCodeGroupResponse } from '~/types/models/ApiResponseCommonCodeGroupResponse'
  import type { MeDetailResponse } from '~/types/models/MeDetailResponse'
  import { API_BASE_URL } from '~/constants/url'

  const route = useRoute()
  const router = useRouter()

  type ActionKey = 'receipt' | 'review' | 'approve' | 'reject'
  type ActionOption = {
    key: ActionKey
    label: string
    color: 'primary' | 'warning' | 'success'
    variant?: 'fill' | 'outlined'
  }

  // 신청 정보 데이터
  const applicationData = ref<RegSportsClubApplicationResponse | null>(null)
  const isLoading = ref(false)
  const meDetail = ref<MeDetailResponse | null>(null)
  const isLoadingRoles = ref(false)

  const actionDialogOpen = ref(false)
  const selectedAction = ref<ActionOption | null>(null)
  const actionNote = ref('')
  const isSubmittingAction = ref(false)
  const actionFeedback = ref('')

  // 운영종목 트리 데이터 (운영종목 이름 표시용)
  const operatingSportTree = ref<CommonCodeGroupResponse | null>(null)

  const roleCodes = computed(() =>
    (meDetail.value?.userRoles ?? [])
      .map((item) => item.roleCode ?? '')
      .filter(Boolean),
  )

  const hasAnyRole = (...codes: string[]) =>
    codes.some((code) => roleCodes.value.includes(code))

  const canRejectAtAnyOperatorStep = () =>
    hasAnyRole(
      'RECEIPT_MANAGER',
      'REVIEWER',
      'APPROVER',
      'ADMIN_SYSTEM_MANAGER',
    )

  const actionOptions = computed<ActionOption[]>(() => {
    const statusCode = applicationData.value?.code ?? ''

    if (
      statusCode === 'APPLY' &&
      hasAnyRole('RECEIPT_MANAGER', 'REVIEWER', 'ADMIN_SYSTEM_MANAGER')
    ) {
      return [
        { key: 'receipt', label: '접수 처리', color: 'primary' },
        {
          key: 'reject',
          label: '접수 반려',
          color: 'warning',
          variant: 'outlined',
        },
      ]
    }

    if (statusCode === 'APPLY' && canRejectAtAnyOperatorStep()) {
      return [
        {
          key: 'reject',
          label: '접수 반려',
          color: 'warning',
          variant: 'outlined',
        },
      ]
    }

    if (statusCode === 'RECEIVED' && hasAnyRole('REVIEWER', 'ADMIN_SYSTEM_MANAGER')) {
      return [
        { key: 'review', label: '검토 처리', color: 'primary' },
        {
          key: 'reject',
          label: '검토 반려',
          color: 'warning',
          variant: 'outlined',
        },
      ]
    }

    if (statusCode === 'RECEIVED' && canRejectAtAnyOperatorStep()) {
      return [
        {
          key: 'reject',
          label: '검토 반려',
          color: 'warning',
          variant: 'outlined',
        },
      ]
    }

    if (statusCode === 'REVIEW' && hasAnyRole('APPROVER', 'ADMIN_SYSTEM_MANAGER')) {
      return [
        { key: 'approve', label: '승인 처리', color: 'success' },
        {
          key: 'reject',
          label: '승인 반려',
          color: 'warning',
          variant: 'outlined',
        },
      ]
    }

    return []
  })

  const canManageApplication = computed(() => actionOptions.value.length > 0)

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

  const fetchMyDetail = async () => {
    try {
      isLoadingRoles.value = true
      const response = await $fetch<ApiResponseMeDetailResponse>(
        `${API_BASE_URL}/users/me/detail`,
        {
          method: 'GET',
          credentials: 'include',
        },
      )

      if (response.resultCode === 200 && response.data) {
        meDetail.value = response.data
      }
    } catch (error) {
      console.error('내 상세 권한 조회 실패:', error)
      meDetail.value = null
    } finally {
      isLoadingRoles.value = false
    }
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

  const openActionDialog = (action: ActionOption) => {
    selectedAction.value = action
    actionNote.value = ''
    actionFeedback.value = ''
    actionDialogOpen.value = true
  }

  const closeActionDialog = () => {
    actionDialogOpen.value = false
    selectedAction.value = null
    actionNote.value = ''
  }

  const submitAction = async () => {
    const applyId = applicationData.value?.applyId
    const action = selectedAction.value

    if (!applyId || !action) return

    try {
      isSubmittingAction.value = true
      const payload = actionNote.value.trim()
        ? { note: actionNote.value.trim() }
        : undefined

      const response = await $fetch<ApiResponseVoid>(
        `${API_BASE_URL}/reg-sports-club-applications/${applyId}/actions`,
        {
          method: 'POST',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
          body: {
            action: action.key,
            payload,
          },
        },
      )

      if (response.resultCode !== 200) {
        throw new Error(response.resultMessage || '처리에 실패했습니다.')
      }

      actionFeedback.value = '처리를 완료했습니다. 최신 상태를 다시 불러왔습니다.'
      closeActionDialog()
      if (Number.isFinite(applyId)) {
        await fetchApplicationDetail(applyId)
      }
    } catch (error) {
      console.error('운영자 처리 실패:', error)
      const message =
        error instanceof Error ? error.message : '처리에 실패했습니다.'
      alert(message)
    } finally {
      isSubmittingAction.value = false
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
    await Promise.all([fetchApplicationDetail(applyId), fetchMyDetail()])
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

          <div v-if="actionFeedback" class="mb-8">
            <div class="rounded-2xl border border-blue-200 bg-blue-50 px-5 py-4">
              <p class="text-sm leading-6 text-blue-900">
                {{ actionFeedback }}
              </p>
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

          <!-- 운영자 처리 -->
          <div class="mb-8">
            <div class="mb-4 flex items-center justify-between gap-4">
              <h3 class="text-grey-800 text-xl font-bold">운영자 처리</h3>
              <span
                v-if="isLoadingRoles"
                class="text-grey-600 text-sm"
              >
                권한 확인 중...
              </span>
            </div>

            <div
              class="rounded-2xl border border-slate-200 bg-white px-5 py-5"
            >
              <template v-if="canManageApplication">
                <p class="text-grey-700 mb-4 text-sm leading-6">
                  현재 로그인한 역할 기준으로 가능한 처리 버튼만 표시합니다.
                </p>
                <div class="flex flex-wrap gap-3">
                  <Button
                    v-for="action in actionOptions"
                    :key="action.key"
                    :color="action.color"
                    size="md"
                    :variant="action.variant ?? 'fill'"
                    @click="openActionDialog(action)"
                  >
                    {{ action.label }}
                  </Button>
                </div>
              </template>

              <template v-else>
                <p class="text-grey-600 text-sm leading-6">
                  현재 상태 또는 현재 로그인 역할 기준으로 실행 가능한 운영자 액션이 없습니다.
                </p>
              </template>
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

    <Dialog
      v-model="actionDialogOpen"
      :max-width="640"
      @update:model-value="(value) => !value && closeActionDialog()"
    >
      <template #title>
        <h3 class="text-lg font-bold">
          {{ selectedAction?.label ?? '운영자 처리' }}
        </h3>
      </template>

      <template #content>
        <div class="grid gap-4">
          <p class="text-grey-700 text-sm leading-6">
            이 작업은 BPM user task completion을 호출합니다. 필요하면 메모를 남기고 처리하세요.
          </p>

          <Textarea
            v-model="actionNote"
            label="처리 메모"
            placeholder="선택 사항입니다. 예: 확인 결과 서류 미비로 반려"
            :rows="4"
            size="max"
          />

          <div class="rounded-xl bg-amber-50 px-4 py-3">
            <p class="text-sm leading-6 text-amber-900">
              처리 완료 후 최신 상세 상태를 다시 불러옵니다.
            </p>
          </div>
        </div>
      </template>

      <template #footer>
        <div class="flex w-full justify-end gap-3">
          <Button
            color="secondary"
            variant="outlined"
            @click="closeActionDialog"
          >
            취소
          </Button>
          <Button
            :color="selectedAction?.color ?? 'primary'"
            :disabled="isSubmittingAction"
            @click="submitAction"
          >
            {{ isSubmittingAction ? '처리 중...' : (selectedAction?.label ?? '처리') }}
          </Button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<style scoped></style>
