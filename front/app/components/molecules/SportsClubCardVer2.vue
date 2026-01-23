<script setup lang="ts">
  import type { SlideGroupItem } from '~/components/molecules/SlideGroup.vue'

  export type Course = {
    id: string
    name: string
    instructor: string
    schedule: string
    capacity: number
    enrolled: number
    price?: string
    operatingSport: string // 운영종목
  }

  export type SportClubCardData = {
    name: string // 스포츠클럽 이름
    badgeType: 'designated' | 'registered' // 지정 또는 등록
    distance: string // "2.5km" 형식
    address: string
    phone: string
    operatingFacilities: string[]
    convenienceFacilities: string[]
    homepageUrl?: string
    courses?: Course[]
  }

  const props = defineProps<{ data: SportClubCardData }>()

  // 운영시설 표시 로직: 첫 번째만 표시하고 나머지가 있으면 "외 n개"
  const displayedOperatingFacilities = computed(() => {
    const facilities = props.data.operatingFacilities
    if (facilities.length === 0) return []
    if (facilities.length === 1) return [facilities[0]]
    return [facilities[0], `외 ${facilities.length - 1}개`]
  })

  // 카드 뒤집기 상태
  const isFlipped = ref(false)

  // 선택된 운영종목 (SlideGroup용)
  const selectedOperatingSport = ref<string>('')

  // 모든 운영종목을 추출하여 SlideGroup 아이템으로 변환
  const operatingSportItems = computed<SlideGroupItem[]>(() => {
    if (!props.data.courses || props.data.courses.length === 0) return []
    const sports = new Set<string>()
    props.data.courses.forEach((course) => {
      if (course.operatingSport) {
        sports.add(course.operatingSport)
      }
    })
    return Array.from(sports).map((sport) => ({
      id: sport,
      title: sport,
      icon: undefined,
      subheaders: [],
      noticeTitle: '',
      noticeContent: [],
    }))
  })

  // 선택된 운영종목에 해당하는 강좌 목록
  const filteredCourses = computed(() => {
    if (!props.data.courses || !selectedOperatingSport.value) return []
    return props.data.courses.filter(
      (course) => course.operatingSport === selectedOperatingSport.value,
    )
  })

  // 선택된 강좌 정보 (첫 번째 강좌)
  const selectedCourse = computed(() => {
    if (filteredCourses.value.length === 0) return null
    return filteredCourses.value[0]
  })

  // 카드 뒤집기 토글
  const toggleFlip = () => {
    if (props.data.courses && props.data.courses.length > 0) {
      isFlipped.value = !isFlipped.value
      // 뒤집힐 때 첫 번째 운영종목 선택
      if (
        isFlipped.value &&
        !selectedOperatingSport.value &&
        operatingSportItems.value.length > 0
      ) {
        selectedOperatingSport.value = operatingSportItems.value[0].id
      }
    }
  }

  // 뒤로 가기 (뒷면에서 앞면으로)
  const goBack = () => {
    isFlipped.value = false
  }

  // 스크롤 이벤트 전파 차단
  const handleWheel = (event: WheelEvent) => {
    const target = event.currentTarget as HTMLElement
    const { scrollTop, scrollHeight, clientHeight } = target

    // 스크롤 가능한 영역인지 확인
    const isScrollable = scrollHeight > clientHeight

    if (!isScrollable) {
      // 스크롤 불가능한 경우 이벤트 전파 차단
      event.stopPropagation()
      return
    }

    // 위쪽 끝에 있고 위로 스크롤하려는 경우
    const isAtTop = scrollTop === 0
    const isScrollingUp = event.deltaY < 0

    // 아래쪽 끝에 있고 아래로 스크롤하려는 경우
    const isAtBottom = scrollTop + clientHeight >= scrollHeight - 1
    const isScrollingDown = event.deltaY > 0

    // 스크롤 가능한 영역 내에서 스크롤하거나, 끝에 도달한 경우 이벤트 전파 차단
    if (
      (isAtTop && isScrollingUp) ||
      (isAtBottom && isScrollingDown) ||
      (!isAtTop && !isAtBottom)
    ) {
      event.stopPropagation()
    }
  }
</script>

<template>
  <div
    class=""
    style="
      width: 100%;
      perspective: 1000px;
      overflow: visible;
      position: relative;
      box-sizing: border-box;
    "
    @mouseleave="goBack"
  >
    <div
      class="transition-transform duration-300 ease-in"
      style="
        position: relative;
        width: 75%;
        height: 100%;
        margin: 0 auto;
        transform-style: preserve-3d;
      "
      :class="{ '': isFlipped }"
    >
      <!-- 앞면 -->
      <v-card
        class="club-card--front rounded-2xl shadow transition-shadow duration-200 ease-in"
        style="width: 100%; overflow: hidden; backface-visibility: hidden"
      >
        <!-- 맵 지도 영역 (맨 위, 패딩/마진 없음) -->
        <div
          class="m-0 p-0"
          style="
            width: 100%;
            min-height: 200px;
            background-color: var(--color-grey-200);
            display: flex;
            align-items: center;
            justify-content: center;
          "
        >
          <v-icon class="" style="font-size: 48px; color: var(--color-grey-400)"
            >mdi-map</v-icon
          >
        </div>

        <v-card-text
          class="gap-3 p-4"
          style="display: flex; flex-direction: column"
        >
          <!-- 뱃지 및 거리 -->
          <div
            class="gap-2"
            style="
              display: flex;
              justify-content: space-between;
              align-items: center;
            "
          >
            <span
              class="rounded px-2 py-1 text-sm font-medium"
              style="
                color: var(--color-primary-700);
                background-color: var(--color-primary-300);
              "
            >
              {{ data.badgeType === 'designated' ? '지정' : '등록' }}
            </span>
            <span class="text-grey-600 text-sm">
              내 위치로부터 {{ data.distance }}
            </span>
          </div>

          <!-- 스포츠클럽 이름 -->
          <h3
            class="text-grey-900 m-0 text-2xl font-bold"
            style="text-align: left"
          >
            {{ data.name }}
          </h3>

          <!-- 위치 -->
          <div class="gap-2" style="display: flex; align-items: center">
            <v-icon
              class="text-grey-600"
              style="font-size: 1.25rem; flex-shrink: 0"
              >mdi-map-marker</v-icon
            >
            <span class="text-grey-900 text-sm">{{ data.address }}</span>
          </div>

          <!-- 전화번호 -->
          <div class="gap-2" style="display: flex; align-items: center">
            <v-icon
              class="text-grey-600"
              style="font-size: 1.25rem; flex-shrink: 0"
              >mdi-phone</v-icon
            >
            <span class="text-grey-900 text-sm">{{ data.phone }}</span>
          </div>

          <!-- 운영시설 -->
          <div class="gap-2" style="display: flex; flex-direction: column">
            <h4 class="text-grey-900 m-0 text-base font-bold">운영시설</h4>
            <div class="gap-2" style="display: flex; flex-wrap: wrap">
              <span
                v-for="(facility, index) in displayedOperatingFacilities"
                :key="index"
                class="text-grey-600 text-sm"
              >
                {{ facility }}
              </span>
            </div>
          </div>

          <!-- 편의시설 -->
          <div class="gap-2" style="display: flex; flex-direction: column">
            <h4 class="text-grey-900 m-0 text-base font-bold">편의시설</h4>
            <div class="gap-2" style="display: flex; flex-wrap: wrap">
              <span
                v-for="(facility, index) in data.convenienceFacilities"
                :key="index"
                class="text-grey-600 text-sm"
              >
                {{ facility }}
              </span>
            </div>
          </div>

          <!-- 하단 버튼 영역 -->
          <div
            class="border-grey-300 mt-2 gap-2 border-t pt-3"
            style="display: flex; justify-content: space-between"
          >
            <v-btn
              v-if="data.courses && data.courses.length > 0"
              variant="text"
              class="font-medium"
              style="flex: 1; color: var(--color-primary-700)"
              @click="toggleFlip"
            >
              운영 종목/강좌 보기
              <v-icon class="ml-1">mdi-chevron-right</v-icon>
            </v-btn>
            <v-btn
              v-if="data.homepageUrl"
              variant="text"
              class="font-medium"
              style="flex: 1; color: var(--color-primary-700)"
              :href="data.homepageUrl"
              target="_blank"
              rel="noopener noreferrer"
            >
              클럽 홈페이지
            </v-btn>
          </div>
        </v-card-text>
      </v-card>

      <!-- 뒷면 -->
      <v-card
        class="club-card--back rounded-2xl shadow transition-shadow duration-200 ease-in"
        style="width: 100%; overflow: hidden; backface-visibility: hidden"
      >
        <v-card-text
          class="gap-4 p-8"
          style="
            display: flex;
            flex-direction: column;
            height: 100%;
            background-color: #000000;
            color: #ffffff;
          "
        >
          <!-- 제목 -->
          <h3 class="m-0 text-xl font-bold" style="color: #ffffff">
            운영 종목/강좌
          </h3>

          <!-- 운영 종목 SlideGroup -->
          <div
            v-if="operatingSportItems.length > 0"
            class=""
            style="width: 100%"
          >
            <SlideGroup
              v-model="selectedOperatingSport"
              :items="operatingSportItems"
              btn-size="small"
            />
          </div>

          <!-- 수강일정 -->
          <h4 class="m-0 text-lg font-bold" style="color: #ffffff">수강일정</h4>

          <!-- 강좌 상세 정보 (스크롤 가능) -->
          <div
            class=""
            style="flex: 1; overflow-y: auto; min-height: 0"
            @wheel="handleWheel"
          >
            <div
              v-for="course in filteredCourses"
              :key="course.id"
              class="gap-3 rounded-lg p-4"
              style="
                background-color: rgba(255, 255, 255, 0.1);
                border: 1px solid rgba(255, 255, 255, 0.2);
                display: flex;
                flex-direction: column;
              "
            >
              <!-- 제목 -->
              <h4 class="m-0 text-xl font-bold" style="color: #ffffff">
                {{ course.name }}
              </h4>

              <!-- 시간 및 인원 -->
              <div class="gap-2" style="display: flex; flex-direction: column">
                <div
                  class="gap-2 text-base"
                  style="
                    display: flex;
                    align-items: center;
                    color: rgba(255, 255, 255, 0.8);
                  "
                >
                  <v-icon
                    class=""
                    style="font-size: 1.25rem; color: rgba(255, 255, 255, 0.8)"
                    >mdi-clock-outline</v-icon
                  >
                  <span>{{ course.schedule }}</span>
                </div>
                <div
                  class="gap-2 text-base"
                  style="
                    display: flex;
                    align-items: center;
                    color: rgba(255, 255, 255, 0.8);
                  "
                >
                  <v-icon
                    class=""
                    style="font-size: 1.25rem; color: rgba(255, 255, 255, 0.8)"
                    >mdi-account-group</v-icon
                  >
                  <span>{{ course.enrolled }} / {{ course.capacity }}명</span>
                </div>
              </div>

              <!-- 가격 -->
              <div
                v-if="course.price"
                class="text-2xl font-bold"
                style="color: var(--color-primary-700)"
              >
                {{ course.price }}
              </div>
            </div>
          </div>

          <!-- 하단 버튼 -->
          <div
            class="gap-2 pt-3"
            style="
              display: flex;
              border-top: 1px solid rgba(255, 255, 255, 0.2);
            "
          >
            <v-btn
              class=""
              style="flex: 1"
              color="primary"
              variant="elevated"
              @click="() => {}"
            >
              수강신청
            </v-btn>
            <v-btn
              variant="text"
              class=""
              style="color: rgba(255, 255, 255, 0.8)"
              @click="goBack"
            >
              뒤로가기
            </v-btn>
          </div>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>
