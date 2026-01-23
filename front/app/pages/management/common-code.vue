<script setup lang="ts">
  import { watchDebounced } from '@vueuse/core'
  import { useIntersectionObserver } from '~/composables/useIntersectionObserver'
  import type { CommonCodeGroupSummaryResponse } from '~/types/models/CommonCodeGroupSummaryResponse'
  import type { ApiResponseCommonCodeGroupListResponse } from '~/types/models/ApiResponseCommonCodeGroupListResponse'
  import TextField from '~/components/atoms/TextField.vue'
  import SelectV2 from '~/components/atoms/SelectV2.vue'
  import Button from '~/components/atoms/Button.vue'
  import { API_BASE_URL } from '~/constants/url'

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  // 로그인하지 않은 경우 홈으로 리다이렉트
  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  // 데이터 상태
  const items = ref<CommonCodeGroupSummaryResponse[]>([])
  const isLoading = ref(false)
  const hasMore = ref(true)
  const currentOffset = ref(0)
  const limit = 20

  // 검색 조건
  const searchName = ref('')
  const includeInactive = ref(false)
  const sortBy = ref('groupCode')
  const sortDir = ref<'asc' | 'desc'>('asc')

  // 공통코드 그룹 목록 조회
  const fetchCommonCodeGroups = async (offset: number, reset = false) => {
    if (isLoading.value) return

    try {
      isLoading.value = true

      const response = await $fetch<ApiResponseCommonCodeGroupListResponse>(
        `${API_BASE_URL}/common-codes/groups`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            name: searchName.value || '',
            includeInactive: includeInactive.value,
            sortBy: sortBy.value,
            sortDir: sortDir.value,
            offset,
            limit,
          },
        },
      )

      if (response.resultCode === 200 && response.data) {
        const newItems = response.data.items || []

        if (reset) {
          items.value = newItems
        } else {
          items.value = [...items.value, ...newItems]
        }

        hasMore.value = response.data.hasNext ?? false
        currentOffset.value = response.data.nextOffset ?? offset + limit
      } else {
        alert(
          response.resultMessage ||
            '공통코드 그룹 목록을 불러오는데 실패했습니다.',
        )
      }
    } catch (error: unknown) {
      console.error('Fetch common code groups error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '공통코드 그룹 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoading.value = false
    }
  }

  // 초기 데이터 로드
  const loadInitialData = async () => {
    items.value = []
    currentOffset.value = 0
    hasMore.value = true
    await fetchCommonCodeGroups(0, true)
  }

  // 더 많은 데이터 로드 (무한 스크롤)
  const loadMore = async () => {
    console.log('loadMore')
    if (!hasMore.value || isLoading.value) return
    await fetchCommonCodeGroups(currentOffset.value, false)
  }

  // 검색 실행
  const handleSearch = async () => {
    await loadInitialData()
  }

  // 하단 감지 요소 참조
  const loadMoreTrigger = ref<HTMLElement | null>(null)

  // 페이지 마운트 시 초기 데이터 로드
  onMounted(() => {
    loadInitialData()
  })

  // Intersection Observer를 사용한 무한 스크롤 (간단한 버전)
  useIntersectionObserver(
    loadMoreTrigger,
    (entry) => {
      if (entry.isIntersecting && hasMore.value && !isLoading.value) {
        loadMore()
      }
    },
    {
      rootMargin: '100px',
      threshold: 0.1,
    },
  )

  // 검색어 변경 시 자동 검색 (debounce)
  const stopWatchSearch = watchDebounced(
    [searchName, includeInactive, sortBy, sortDir],
    () => {
      loadInitialData()
    },
    { debounce: 500 },
  )

  // 컴포넌트 언마운트 시 정리
  onUnmounted(() => {
    stopWatchSearch()
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
        <!-- 검색 영역 -->
        <div
          class="border-neutral-stroke-300 mb-6 rounded-lg border bg-white p-6"
        >
          <div class="grid gap-4 md:grid-cols-4">
            <div>
              <label
                class="text-neutral-text-title mb-2 block text-sm font-semibold"
              >
                그룹명/코드 검색
              </label>
              <TextField
                v-model="searchName"
                label=""
                placeholder="그룹명 또는 코드를 입력하세요"
                size="md"
              />
            </div>

            <div>
              <label
                class="text-neutral-text-title mb-2 block text-sm font-semibold"
              >
                정렬 기준
              </label>
              <SelectV2
                v-model="sortBy"
                :items="[
                  { title: '그룹 코드', value: 'groupCode' },
                  { title: '그룹명', value: 'groupName' },
                  { title: '정렬 순서', value: 'sortOrder' },
                ]"
                size="md"
              />
            </div>

            <div>
              <label
                class="text-neutral-text-title mb-2 block text-sm font-semibold"
              >
                정렬 방향
              </label>
              <SelectV2
                v-model="sortDir"
                :items="[
                  { title: '오름차순', value: 'asc' },
                  { title: '내림차순', value: 'desc' },
                ]"
                size="md"
              />
            </div>

            <div class="flex items-end">
              <label
                class="text-neutral-text-title mb-2 flex items-center gap-2"
              >
                <input
                  v-model="includeInactive"
                  type="checkbox"
                  class="h-4 w-4"
                />
                <span class="text-sm font-semibold">비활성 포함</span>
              </label>
            </div>
          </div>

          <div class="mt-4 flex justify-end">
            <Button color="primary" size="md" @click="handleSearch">
              검색
            </Button>
          </div>
        </div>

        <!-- 카드 영역 -->
        <div class="space-y-4">
          <!-- 카드 그리드 -->
          <div
            v-if="items.length > 0"
            class="grid gap-4 md:grid-cols-2 lg:grid-cols-3"
          >
            <div
              v-for="item in items"
              :key="item.id"
              class="border-neutral-stroke-300 rounded-lg border bg-white p-6 shadow-sm transition-shadow hover:shadow-md"
            >
              <div class="mb-4">
                <div
                  class="text-neutral-text-title mb-2 flex items-center justify-between"
                >
                  <h3 class="text-lg font-bold">{{ item.groupName || '-' }}</h3>
                  <span class="text-neutral-text-caption text-sm"
                    >ID: {{ item.id }}</span
                  >
                </div>
                <div class="text-neutral-text-body mb-1 text-sm">
                  <span class="font-semibold">그룹 코드:</span>
                  <span class="ml-2">{{ item.groupCode || '-' }}</span>
                </div>
              </div>

              <div class="border-neutral-stroke-200 space-y-2 border-t pt-4">
                <div
                  class="text-neutral-text-body flex items-center justify-between text-sm"
                >
                  <span class="font-semibold">레벨:</span>
                  <span>{{ item.level || '-' }}</span>
                </div>
                <div
                  class="text-neutral-text-body flex items-center justify-between text-sm"
                >
                  <span class="font-semibold">부모 그룹 ID:</span>
                  <span>{{ item.parentGroupId || '-' }}</span>
                </div>
                <div
                  class="text-neutral-text-body flex items-center justify-between text-sm"
                >
                  <span class="font-semibold">정렬 순서:</span>
                  <span>{{ item.sortOrder || '-' }}</span>
                </div>
                <div
                  v-if="item.description"
                  class="text-neutral-text-caption mt-3 text-sm"
                >
                  <span class="font-semibold">설명:</span>
                  <p class="mt-1">{{ item.description }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 로딩 인디케이터 -->
          <div
            v-if="isLoading && items.length > 0"
            class="border-neutral-stroke-300 rounded-lg border bg-white p-4 text-center"
          >
            <span class="text-neutral-text-caption text-sm"
              >데이터를 불러오는 중...</span
            >
          </div>

          <!-- 더 이상 데이터가 없을 때 -->
          <div
            v-if="!hasMore && items.length > 0 && !isLoading"
            class="border-neutral-stroke-300 rounded-lg border bg-white p-4 text-center"
          >
            <span class="text-neutral-text-caption text-sm"
              >모든 데이터를 불러왔습니다.</span
            >
          </div>

          <!-- 데이터가 없을 때 -->
          <div
            v-if="!isLoading && items.length === 0"
            class="border-neutral-stroke-300 rounded-lg border bg-white p-12 text-center"
          >
            <span class="text-neutral-text-caption"
              >조회된 데이터가 없습니다.</span
            >
          </div>

          <!-- 무한 스크롤 트리거 요소 -->
          <div
            v-show="hasMore && items.length > 0"
            ref="loadMoreTrigger"
            class="h-10 w-full"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
