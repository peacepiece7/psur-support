<script setup lang="ts">
  import type { CommonCodeGroupSummaryResponse } from '~/types/models/CommonCodeGroupSummaryResponse'
  import type { CommonCodeGroupResponse } from '~/types/models/CommonCodeGroupResponse'
  import type { ApiResponseListCommonCodeGroupSummaryResponse } from '~/types/models/ApiResponseListCommonCodeGroupSummaryResponse'
  import type { ApiResponseCommonCodeGroupResponse } from '~/types/models/ApiResponseCommonCodeGroupResponse'
  import TreeDisplay from '~/components/molecules/TreeDisplay.vue'
  import { API_BASE_URL } from '~/constants/url'

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  // 로그인하지 않은 경우 홈으로 리다이렉트
  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  // 그룹과 트리 데이터를 함께 저장하는 타입
  type GroupWithTree = {
    group: CommonCodeGroupSummaryResponse
    tree: CommonCodeGroupResponse | null
    isLoading: boolean
  }

  // 데이터 상태
  const groupsWithTrees = ref<GroupWithTree[]>([])
  const isLoading = ref(false)

  // 루트 그룹 목록 조회
  const fetchRootGroups = async () => {
    try {
      isLoading.value = true
      const response = await $fetch<ApiResponseListCommonCodeGroupSummaryResponse>(
        `${API_BASE_URL}/common-codes/groups/root`,
        {
          method: 'GET',
          credentials: 'include',
        },
      )

      if (response.resultCode === 200 && response.data) {
        // 각 그룹에 대해 초기 상태 설정
        groupsWithTrees.value = response.data.map((group) => ({
          group,
          tree: null,
          isLoading: false,
        }))

        // 각 그룹의 tree 데이터를 병렬로 가져오기
        await Promise.all(
          groupsWithTrees.value.map(async (item) => {
            if (!item.group.groupCode) return
            item.isLoading = true
            try {
              const treeResponse = await $fetch<ApiResponseCommonCodeGroupResponse>(
                `${API_BASE_URL}/common-codes/${item.group.groupCode}/tree`,
                {
                  method: 'GET',
                  credentials: 'include',
                  query: {
                    depth: 3,
                    includeCodes: true,
                  },
                },
              )

              if (treeResponse.resultCode === 200 && treeResponse.data) {
                item.tree = treeResponse.data
              }
            } catch (error) {
              console.error(`Failed to fetch tree for ${item.group.groupCode}:`, error)
            } finally {
              item.isLoading = false
            }
          }),
        )
      } else {
        alert(
          response.resultMessage ||
            '루트 그룹 목록을 불러오는데 실패했습니다.',
        )
      }
    } catch (error: unknown) {
      console.error('Fetch root groups error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '루트 그룹 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoading.value = false
    }
  }

  // 페이지 마운트 시 초기 데이터 로드
  onMounted(() => {
    fetchRootGroups()
  })
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="공통코드 관리"
      image-src="/main_banner.png"
      image-alt="공통코드 관리 페이지 헤더 이미지"
      type="infoPage"
    />

    <div class="bg-neutral-fill-50 min-h-screen p-8">
      <div class="mx-auto max-w-[1400px]">
        <!-- 카드 영역 -->
        <div class="space-y-4">
          <!-- 로딩 인디케이터 -->
          <div
            v-if="isLoading"
            class="border-neutral-stroke-300 rounded-lg border bg-white p-4 text-center"
          >
            <span class="text-neutral-text-caption text-sm"
              >데이터를 불러오는 중...</span
            >
          </div>

          <!-- 카드 그리드 -->
          <div
            v-if="!isLoading && groupsWithTrees.length > 0"
            class="grid gap-4 md:grid-cols-2 lg:grid-cols-3"
          >
            <div
              v-for="item in groupsWithTrees"
              :key="item.group.id"
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6 shadow-sm transition-shadow hover:shadow-md"
            >
              <!-- 그룹 헤더 -->
              <div class="mb-4">
                <div
                  class="text-neutral-text-title mb-2 flex items-center justify-between"
                >
                  <h3 class="text-lg font-bold">{{ item.group.groupName || '-' }}</h3>
                  <span class="text-neutral-text-caption text-sm"
                    >ID: {{ item.group.id }}</span
                  >
                </div>
                <div class="text-neutral-text-body mb-1 text-sm">
                  <span class="font-semibold">그룹 코드:</span>
                  <span class="ml-2">{{ item.group.groupCode || '-' }}</span>
                </div>
                <div
                  v-if="item.group.description"
                  class="text-neutral-text-caption mt-2 text-sm"
                >
                  <span class="font-semibold">설명:</span>
                  <p class="mt-1">{{ item.group.description }}</p>
                </div>
              </div>

              <!-- 트리 데이터 표시 -->
              <div class="border-neutral-stroke-200 border-t pt-4">
                <div v-if="item.isLoading" class="py-4 text-center">
                  <span class="text-neutral-text-caption text-sm"
                    >트리 데이터를 불러오는 중...</span
                  >
                </div>
                <div v-else-if="item.tree">
                  <TreeDisplay :tree="item.tree" />
                </div>
                <div v-else class="py-4 text-center">
                  <span class="text-neutral-text-caption text-sm"
                    >트리 데이터가 없습니다.</span
                  >
                </div>
              </div>
            </div>
          </div>

          <!-- 데이터가 없을 때 -->
          <div
            v-if="!isLoading && groupsWithTrees.length === 0"
            class="border-neutral-stroke-300 rounded-lg border bg-white p-12 text-center"
          >
            <span class="text-neutral-text-caption"
              >조회된 데이터가 없습니다.</span
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
