<script setup lang="ts">
  import type { DataTableHeader } from '~/components/molecules/Table.vue'
  import Table from '~/components/molecules/Table.vue'
  import Pagination from '~/components/molecules/Pagination.vue'

  // 테이블 헤더 정의
  const headers: DataTableHeader[] = [
    { title: 'ID', key: 'id', width: '80px' },
    { title: '이름', key: 'name', width: '150px' },
    { title: '이메일', key: 'email', width: '200px' },
    { title: '부서', key: 'department', width: '120px' },
    { title: '직급', key: 'position', width: '100px' },
    { title: '입사일', key: 'joinDate', width: '120px' },
    { title: '상태', key: 'status', width: '100px' },
  ]

  // 더미 데이터 타입
  type TableItem = {
    id: string
    name: string
    email: string
    department: string
    position: string
    joinDate: string
    status: '재직' | '휴직' | '퇴사'
  }

  // 더미 데이터 생성 함수
  const generateDummyData = (count: number): TableItem[] => {
    const names = [
      '김철수',
      '이영희',
      '박민수',
      '정수진',
      '최동욱',
      '한소영',
      '윤태호',
      '강미영',
    ]
    const departments = ['개발팀', '디자인팀', '기획팀', '마케팅팀', '인사팀']
    const positions = ['사원', '대리', '과장', '차장', '부장']
    const statuses: ('재직' | '휴직' | '퇴사')[] = ['재직', '휴직', '퇴사']

    return Array.from({ length: count }, (_, i) => {
      const index = i + 1
      const name = names[index % names.length]
      const department = departments[index % departments.length]
      const position = positions[index % positions.length]
      const status = statuses[index % statuses.length]

      return {
        id: `EMP-${String(index).padStart(4, '0')}`,
        name: `${name}${index > names.length ? index : ''}`,
        email: `${name.toLowerCase()}${index}@example.com`,
        department,
        position,
        joinDate: `202${index % 4}-${String((index % 12) + 1).padStart(2, '0')}-${String((index % 28) + 1).padStart(2, '0')}`,
        status,
      }
    })
  }

  // 상태 관리
  const isLoading = ref(false)
  const currentPage = ref(1)
  const itemsPerPage = ref(10)
  const totalItems = ref(150) // 전체 데이터 수
  const tableItems = ref<TableItem[]>([])
  const selectedItems = ref<TableItem[]>([])

  // 데이터 로드 함수
  const loadData = async () => {
    isLoading.value = true

    // API 호출 시뮬레이션
    await new Promise((resolve) => setTimeout(resolve, 1000))

    const startIndex = (currentPage.value - 1) * itemsPerPage.value
    const endIndex = startIndex + itemsPerPage.value
    const data = generateDummyData(totalItems.value)
    tableItems.value = data.slice(startIndex, endIndex)

    isLoading.value = false
  }

  // 페이지 변경 핸들러
  const handlePageChange = (page: number) => {
    currentPage.value = page
    loadData()
  }

  // 항목 수 변경 핸들러
  const handleItemsPerPageChange = (value: number) => {
    itemsPerPage.value = value
    currentPage.value = 1
    loadData()
  }

  // 행 클릭 핸들러
  const handleRowClick = (item: TableItem, index: number) => {
    console.log('Row clicked:', item, index)
  }

  // 초기 데이터 로드
  onMounted(() => {
    loadData()
  })

  // 로딩 시뮬레이션 버튼
  const simulateLoading = () => {
    isLoading.value = true
    setTimeout(() => {
      isLoading.value = false
    }, 2000)
  }
</script>

<template>
  <div class="bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-[1400px]">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">
        Table + Pagination 예제
      </h1>

      <section
        class="border-grey-300 min-h-[420px] rounded-3xl border bg-white p-10"
      >
        <div class="mb-6 flex items-center justify-between">
          <h2 class="m-0 text-2xl font-bold">직원 목록</h2>
          <button
            type="button"
            class="border-grey-300 text-grey-700 hover:bg-grey-50 rounded-lg border bg-white px-4 py-2 text-sm font-semibold transition-colors"
            @click="simulateLoading"
          >
            로딩 시뮬레이션
          </button>
        </div>

        <!-- 테이블 -->
        <div class="mb-6">
          <Table
            :headers="headers"
            :items="tableItems"
            :loading="isLoading"
            :items-per-page="itemsPerPage"
            :page="currentPage"
            :total-items="totalItems"
            @update:page="handlePageChange"
            @update:items-per-page="handleItemsPerPageChange"
            @click:row="handleRowClick"
          >
            <!-- 상태 컬럼 커스터마이징 -->
            <template #[`item.status`]="{ item }">
              <span
                :class="{
                  'text-green-600': item.status === '재직',
                  'text-yellow-600': item.status === '휴직',
                  'text-red-600': item.status === '퇴사',
                }"
                class="font-semibold"
              >
                {{ item.status }}
              </span>
            </template>
          </Table>
        </div>

        <!-- 페이지네이션 -->
        <Pagination
          v-model="currentPage"
          :length="Math.ceil(totalItems / itemsPerPage)"
          :items-per-page="itemsPerPage"
          :total-items="totalItems"
          :loading="isLoading"
          @update:model-value="handlePageChange"
        />

        <!-- 선택된 항목 표시 (디버깅용) -->
        <div
          v-if="selectedItems.length > 0"
          class="bg-grey-100 mt-6 rounded-lg p-4"
        >
          <h3 class="text-grey-700 mb-2 text-sm font-semibold">
            선택된 항목 ({{ selectedItems.length }}개):
          </h3>
          <div class="text-grey-600 text-xs">
            {{ selectedItems.map((item) => item.name).join(', ') }}
          </div>
        </div>
      </section>

      <!-- 사용법 설명 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-4 text-2xl font-bold">사용법</h2>
        <div class="text-grey-700 space-y-4 text-sm">
          <div>
            <h3 class="text-grey-900 mb-2 font-semibold">Table 컴포넌트</h3>
            <ul class="list-inside list-disc space-y-1">
              <li>Vuetify의 v-data-table을 래핑한 컴포넌트</li>
              <li>로딩 시 자동으로 스켈레톤 UI 표시</li>
              <li>페이지네이션, 정렬, 선택 기능 지원</li>
              <li>슬롯을 통해 컬럼 커스터마이징 가능</li>
            </ul>
          </div>
          <div>
            <h3 class="text-grey-900 mb-2 font-semibold">
              Pagination 컴포넌트
            </h3>
            <ul class="list-inside list-disc space-y-1">
              <li>Vuetify의 v-pagination을 래핑한 컴포넌트</li>
              <li>둥근 모서리 스타일 지원</li>
              <li>전체 항목 수와 현재 표시 범위 표시</li>
              <li>로딩 상태 표시 지원</li>
            </ul>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>
