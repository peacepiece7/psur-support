<script setup lang="ts">
  import type { CommonCodeResponse } from '~/types/models/CommonCodeResponse'
  import type { ApiResponseCommonCodeGroupResponse } from '~/types/models/ApiResponseCommonCodeGroupResponse'
  import SelectV2 from '~/components/atoms/SelectV2.vue'
  import { API_BASE_URL } from '~/constants/url'

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  // 로그인하지 않은 경우 홈으로 리다이렉트
  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  // 시도/시군구 관련 상태
  const regionSidoList = ref<CommonCodeResponse[]>([])
  const regionSigunguList = ref<CommonCodeResponse[]>([])
  const selectedSidoCode = ref<string | null>(null)
  const selectedSigunguCode = ref<string | null>(null)
  const isLoadingRegion = ref(false)

  // 운영종목 관련 상태
  const operatingSportUpperList = ref<CommonCodeResponse[]>([])
  const operatingSportLowerList = ref<CommonCodeResponse[]>([])
  const selectedOperatingSportUpperCode = ref<string | null>(null)
  const selectedOperatingSportLowerCode = ref<string | null>(null)
  const isLoadingOperatingSport = ref(false)

  // 시도 리스트 조회
  const fetchRegionSidoList = async () => {
    try {
      isLoadingRegion.value = true
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/REGION/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 1,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data?.codes) {
        regionSidoList.value = response.data.codes
      } else {
        alert(
          response.resultMessage ||
            '시도 목록을 불러오는데 실패했습니다.',
        )
      }
    } catch (error: unknown) {
      console.error('Fetch region sido list error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '시도 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoadingRegion.value = false
    }
  }

  // 시군구 리스트 조회
  const fetchRegionSigunguList = async (sidoCode: string) => {
    try {
      isLoadingRegion.value = true
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/${sidoCode}/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 1,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data?.codes) {
        regionSigunguList.value = response.data.codes
      } else {
        alert(
          response.resultMessage ||
            '시군구 목록을 불러오는데 실패했습니다.',
        )
      }
    } catch (error: unknown) {
      console.error('Fetch region sigungu list error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '시군구 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoadingRegion.value = false
    }
  }

  // 상위 운영종목 리스트 조회
  const fetchOperatingSportUpperList = async () => {
    try {
      isLoadingOperatingSport.value = true
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/OPERATING_SPORT/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 1,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data?.codes) {
        operatingSportUpperList.value = response.data.codes
      } else {
        alert(
          response.resultMessage ||
            '상위 운영종목 목록을 불러오는데 실패했습니다.',
        )
      }
    } catch (error: unknown) {
      console.error('Fetch operating sport upper list error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '상위 운영종목 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoadingOperatingSport.value = false
    }
  }

  // 하위 운영종목 리스트 조회
  const fetchOperatingSportLowerList = async (upperCode: string) => {
    try {
      isLoadingOperatingSport.value = true
      const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
        `${API_BASE_URL}/common-codes/${upperCode}/tree`,
        {
          method: 'GET',
          credentials: 'include',
          query: {
            depth: 1,
            includeCodes: true,
          },
        },
      )

      if (response.resultCode === 200 && response.data?.codes) {
        operatingSportLowerList.value = response.data.codes
      } else {
        alert(
          response.resultMessage ||
            '하위 운영종목 목록을 불러오는데 실패했습니다.',
        )
      }
    } catch (error: unknown) {
      console.error('Fetch operating sport lower list error:', error)
      const errorMessage =
        (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
        (error as { message?: string })?.message ||
        '하위 운영종목 목록을 불러오는 중 오류가 발생했습니다.'
      alert(errorMessage)
    } finally {
      isLoadingOperatingSport.value = false
    }
  }

  // 시도 선택 시 시군구 리스트 조회
  watch(selectedSidoCode, (newValue) => {
    const groupCode = regionSidoList.value.find(item => item.code === newValue)?.groupCode
    if (groupCode) {
      selectedSigunguCode.value = null
      regionSigunguList.value = []
      fetchRegionSigunguList(groupCode)
    } else {
      selectedSigunguCode.value = null
      regionSigunguList.value = []
    }
  })

  // 상위 운영종목 선택 시 하위 운영종목 리스트 조회
  watch(selectedOperatingSportUpperCode, (newValue) => {
    const groupCode = operatingSportUpperList.value.find(item => item.code === newValue)?.groupCode
    if (groupCode) {
      selectedOperatingSportLowerCode.value = null
      operatingSportLowerList.value = []
      fetchOperatingSportLowerList(groupCode)
    } else {
      selectedOperatingSportLowerCode.value = null
      operatingSportLowerList.value = []
    }
  })

  // 페이지 마운트 시 초기 데이터 로드
  onMounted(() => {
    fetchRegionSidoList()
    fetchOperatingSportUpperList()
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
        <!-- 시도/시군구 선택 영역 -->
        <div
          class="border-neutral-stroke-300 mb-6 rounded-lg border bg-white p-6"
        >
          <h2 class="text-neutral-text-title mb-4 text-lg font-bold">
            시도/시군구
          </h2>
          <div class="grid gap-4 md:grid-cols-2">
            <div>
              <label
                class="text-neutral-text-title mb-2 block text-sm font-semibold"
              >
                시도
              </label>
              <SelectV2
                v-model="selectedSidoCode"
                :items="regionSidoList"
                item-text="codeName"
                item-value="code"
                placeholder="시도를 선택하세요"
                size="md"
                :disabled="isLoadingRegion"
                clearable
              />
            </div>

            <div>
              <label
                class="text-neutral-text-title mb-2 block text-sm font-semibold"
              >
                시군구
              </label>
              <SelectV2
                v-model="selectedSigunguCode"
                :items="regionSigunguList"
                item-text="codeName"
                item-value="code"
                placeholder="시군구를 선택하세요"
                size="md"
                :disabled="isLoadingRegion || !selectedSidoCode"
                clearable
              />
            </div>
          </div>

          <!-- 선택된 값 표시 -->
          <div
            v-if="selectedSidoCode || selectedSigunguCode"
            class="border-neutral-stroke-200 mt-4 space-y-2 rounded border bg-neutral-fill-50 p-4"
          >
            <div
              v-if="selectedSidoCode"
              class="text-neutral-text-body text-sm"
            >
              <span class="font-semibold">선택된 시도:</span>
              <span class="ml-2">
                {{
                  regionSidoList.find((item) => item.code === selectedSidoCode)
                    ?.codeName || selectedSidoCode
                }}
                ({{ selectedSidoCode }})
              </span>
            </div>
            <div
              v-if="selectedSigunguCode"
              class="text-neutral-text-body text-sm"
            >
              <span class="font-semibold">선택된 시군구:</span>
              <span class="ml-2">
                {{
                  regionSigunguList.find(
                    (item) => item.code === selectedSigunguCode,
                  )?.codeName || selectedSigunguCode
                }}
                ({{ selectedSigunguCode }})
              </span>
            </div>
          </div>
        </div>

        <!-- 운영종목 선택 영역 -->
        <div
          class="border-neutral-stroke-300 mb-6 rounded-lg border bg-white p-6"
        >
          <h2 class="text-neutral-text-title mb-4 text-lg font-bold">
            운영종목
          </h2>
          <div class="grid gap-4 md:grid-cols-2">
            <div>
              <label
                class="text-neutral-text-title mb-2 block text-sm font-semibold"
              >
                상위 운영종목
              </label>
              <SelectV2
                v-model="selectedOperatingSportUpperCode"
                :items="operatingSportUpperList"
                item-text="codeName"
                item-value="code"
                placeholder="상위 운영종목을 선택하세요"
                size="md"
                :disabled="isLoadingOperatingSport"
                clearable
              />
            </div>

            <div>
              <label
                class="text-neutral-text-title mb-2 block text-sm font-semibold"
              >
                하위 운영종목
              </label>
              <SelectV2
                v-model="selectedOperatingSportLowerCode"
                :items="operatingSportLowerList"
                item-text="codeName"
                item-value="code"
                placeholder="하위 운영종목을 선택하세요"
                size="md"
                :disabled="
                  isLoadingOperatingSport || !selectedOperatingSportUpperCode
                "
                clearable
              />
            </div>
          </div>

          <!-- 선택된 값 표시 -->
          <div
            v-if="
              selectedOperatingSportUpperCode ||
              selectedOperatingSportLowerCode
            "
            class="border-neutral-stroke-200 mt-4 space-y-2 rounded border bg-neutral-fill-50 p-4"
          >
            <div
              v-if="selectedOperatingSportUpperCode"
              class="text-neutral-text-body text-sm"
            >
              <span class="font-semibold">선택된 상위 운영종목:</span>
              <span class="ml-2">
                {{
                  operatingSportUpperList.find(
                    (item) => item.code === selectedOperatingSportUpperCode,
                  )?.codeName || selectedOperatingSportUpperCode
                }}
                ({{ selectedOperatingSportUpperCode }})
              </span>
            </div>
            <div
              v-if="selectedOperatingSportLowerCode"
              class="text-neutral-text-body text-sm"
            >
              <span class="font-semibold">선택된 하위 운영종목:</span>
              <span class="ml-2">
                {{
                  operatingSportLowerList.find(
                    (item) => item.code === selectedOperatingSportLowerCode,
                  )?.codeName || selectedOperatingSportLowerCode
                }}
                ({{ selectedOperatingSportLowerCode }})
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
