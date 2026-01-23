<script setup lang="ts">
  export type Course = {
    id: string
    name: string
    instructor: string
    schedule: string
    capacity: number
    enrolled: number
    price?: string
  }
  export type SportClubCardData = {
    category: string
    distance: string // "2.5km" 형식
    address: string
    phone: string
    operatingFacilities: string[]
    convenienceFacilities: string[]
    homepageUrl?: string
    courses?: Course[]
  }

  defineProps<{ data: SportClubCardData }>()

  const dialogOpen = ref(false)

  const expanded = ref(false)
</script>

<template>
  <v-card
    class="rounded-2xl shadow"
    style="
      position: relative;
      height: 100%;
      transition:
        box-shadow 200ms ease-in-out,
        height 200ms ease-in-out;
      overflow: hidden;
    "
    :class="{ 'card--expanded': expanded }"
  >
    <!-- 확장 버튼 (왼쪽) -->
    <button
      v-if="data.courses && data.courses.length > 0"
      class="border-grey-300 border-r"
      style="
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        width: 40px;
        background-color: var(--color-grey-100);
        border: none;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1;
        transition:
          background-color 200ms ease-in-out,
          border-color 200ms ease-in-out;
      "
      @click="expanded = !expanded"
    >
      <v-icon
        class="text-grey-900 transition-transform duration-200 ease-in"
        style="font-size: 24px"
        :class="{ 'card__expand-icon--rotated': expanded }"
      >
        mdi-chevron-right
      </v-icon>
    </button>
    <v-card-text
      class="gap-4 p-4"
      style="padding-left: 56px; display: flex; flex-direction: column"
    >
      <!-- 카테고리 및 거리 -->
      <div
        class="gap-2"
        style="display: flex; align-items: center; flex-wrap: wrap"
      >
        <span
          class="text-base font-bold"
          style="color: var(--color-primary-700)"
          >{{ data.category }}</span
        >
        <div class="gap-1" style="display: flex; align-items: center">
          <span class="text-grey-600 text-sm">
            내 현위치에서 {{ data.distance }}
          </span>
          <v-btn
            icon
            variant="text"
            size="small"
            class=""
            style="
              min-width: 24px;
              width: 24px;
              height: 24px;
              color: var(--color-primary-700);
            "
            @click="dialogOpen = true"
          >
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </div>
      </div>
      <!-- 지도 영역 (임시 회색 박스 + 맵 아이콘) -->
      <div
        class="rounded-lg"
        style="
          width: 100%;
          height: 200px;
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
      <!-- 주소 및 전화번호 -->
      <div class="gap-2" style="display: flex; flex-direction: column">
        <div class="gap-2" style="display: flex; align-items: center">
          <v-icon class="text-grey-600" style="font-size: 1.25rem"
            >mdi-map-marker</v-icon
          >
          <span class="text-grey-900 text-sm">{{ data.address }}</span>
        </div>
        <div class="gap-2" style="display: flex; align-items: center">
          <v-icon class="text-grey-600" style="font-size: 1.25rem"
            >mdi-phone</v-icon
          >
          <span class="text-grey-900 text-sm">{{ data.phone }}</span>
        </div>
      </div>
      <!-- 운영시설 -->
      <div class="gap-2" style="display: flex; flex-direction: column">
        <h4 class="text-grey-900 m-0 text-xl font-bold">운영시설</h4>
        <ul
          class="m-0 gap-1 p-0"
          style="list-style: none; display: flex; flex-direction: column"
        >
          <li
            v-for="(facility, index) in data.operatingFacilities"
            :key="index"
            class="text-grey-600 pl-2 text-sm"
          >
            {{ facility }}
          </li>
        </ul>
      </div>
      <!-- 편의시설 -->
      <div class="gap-2" style="display: flex; flex-direction: column">
        <h4 class="text-grey-900 m-0 text-xl font-bold">편의시설</h4>
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
      <!-- 클럽 홈페이지 -->
      <div
        v-if="data.homepageUrl"
        class="border-grey-300 border-t pt-2"
        style="margin-top: auto"
      >
        <a
          :href="data.homepageUrl"
          target="_blank"
          rel="noopener noreferrer"
          class="gap-2 text-sm font-medium transition-colors duration-200 ease-in"
          style="
            display: flex;
            align-items: center;
            color: var(--color-primary-700);
            text-decoration: none;
          "
        >
          <span>클럽 홈페이지</span>
          <v-icon class="" style="font-size: 1rem"> mdi-open-in-new </v-icon>
        </a>
      </div>
      <!-- 확장 영역: 강좌 목록 -->
      <div
        v-if="expanded && data.courses && data.courses.length > 0"
        class="border-grey-300 mt-2 gap-4 border-t-2 pt-4"
        style="display: flex; flex-direction: column"
      >
        <h4 class="text-grey-900 m-0 text-xl font-bold">수강 가능한 강좌</h4>
        <div class="gap-3" style="display: flex; flex-direction: column">
          <div
            v-for="course in data.courses"
            :key="course.id"
            class="border-grey-200 rounded-lg border p-3 transition-shadow duration-200 ease-in"
            style="background-color: var(--color-grey-50)"
          >
            <div
              class="mb-2"
              style="
                display: flex;
                justify-content: space-between;
                align-items: center;
              "
            >
              <h5 class="text-grey-900 m-0 text-base font-bold">
                {{ course.name }}
              </h5>
              <span
                v-if="course.price"
                class="text-base font-bold"
                style="color: var(--color-primary-700)"
              >
                {{ course.price }}
              </span>
            </div>
            <div class="gap-1" style="display: flex; flex-direction: column">
              <div
                class="text-grey-600 gap-2 text-sm"
                style="display: flex; align-items: center"
              >
                <v-icon class="text-grey-600" style="font-size: 1rem"
                  >mdi-account</v-icon
                >
                <span>{{ course.instructor }}</span>
              </div>
              <div
                class="text-grey-600 gap-2 text-sm"
                style="display: flex; align-items: center"
              >
                <v-icon class="text-grey-600" style="font-size: 1rem"
                  >mdi-clock-outline</v-icon
                >
                <span>{{ course.schedule }}</span>
              </div>
              <div
                class="text-grey-600 gap-2 text-sm"
                style="display: flex; align-items: center"
              >
                <v-icon class="text-grey-600" style="font-size: 1rem"
                  >mdi-account-group</v-icon
                >
                <span>{{ course.enrolled }} / {{ course.capacity }}명</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </v-card-text>
    <!-- 상세 정보 Dialog -->
    <Dialog v-model="dialogOpen" max-width="800" scrollable class="dialog">
      <template #title>
        <div class="dialog__title-content">
          <span
            class="text-2xl font-bold"
            style="color: var(--color-primary-700)"
            >{{ data.category }}</span
          >
          <span class="text-grey-600 text-xl">
            내 현위치에서 {{ data.distance }}
          </span>
        </div>
      </template>
      <template #content>
        <!-- 지도 영역 (더 큰 크기) -->
        <div
          class="rounded-lg"
          style="
            width: 100%;
            height: 400px;
            background-color: var(--color-grey-200);
            display: flex;
            align-items: center;
            justify-content: center;
          "
        >
          <v-icon class="" style="font-size: 80px; color: var(--color-grey-400)"
            >mdi-map</v-icon
          >
        </div>
        <!-- 주소 및 전화번호 -->
        <div class="gap-3" style="display: flex; flex-direction: column">
          <div class="dialog__info-item">
            <v-icon class="dialog__info-icon">mdi-map-marker</v-icon>
            <span class="dialog__info-text">{{ data.address }}</span>
          </div>
          <div class="dialog__info-item">
            <v-icon class="dialog__info-icon">mdi-phone</v-icon>
            <span class="dialog__info-text">{{ data.phone }}</span>
          </div>
        </div>
        <!-- 운영시설 -->
        <div class="dialog__section">
          <h4 class="dialog__section-title">운영시설</h4>
          <ul
            class="m-0 gap-2 p-0"
            style="list-style: none; display: flex; flex-direction: column"
          >
            <li
              v-for="(facility, index) in data.operatingFacilities"
              :key="index"
              class="text-grey-600 pl-3 text-base"
            >
              {{ facility }}
            </li>
          </ul>
        </div>
        <!-- 편의시설 -->
        <div class="dialog__section">
          <h4 class="dialog__section-title">편의시설</h4>
          <div class="gap-3 pt-2" style="display: flex; flex-wrap: wrap">
            <span
              v-for="(facility, index) in data.convenienceFacilities"
              :key="index"
              class="text-grey-600 rounded-lg px-3 py-1 text-base"
              style="background-color: var(--color-grey-100)"
            >
              {{ facility }}
            </span>
          </div>
        </div>
        <!-- 추가 정보 (더 풍부한 컨텐츠) -->
        <div class="dialog__section">
          <h4 class="dialog__section-title">운영 시간</h4>
          <p class="dialog__text">
            평일: 09:00 - 22:00<br />
            주말: 10:00 - 20:00<br />
            공휴일: 휴무
          </p>
        </div>
        <div class="dialog__section">
          <h4 class="dialog__section-title">이용 안내</h4>
          <p class="dialog__text">
            회원 및 비회원 모두 이용 가능하며, 사전 예약을 권장합니다. 체육시설
            이용 시 운동화 착용 필수입니다.
          </p>
        </div>
        <!-- 클럽 홈페이지 -->
        <div v-if="data.homepageUrl" class="border-grey-300 border-t pt-4">
          <a
            :href="data.homepageUrl"
            target="_blank"
            rel="noopener noreferrer"
            class="gap-2 text-base font-medium"
            style="
              display: flex;
              align-items: center;
              color: var(--color-primary-700);
              text-decoration: none;
            "
          >
            <span>클럽 홈페이지</span>
            <v-icon class="" style="font-size: 1.25rem">
              mdi-open-in-new
            </v-icon>
          </a>
        </div>
      </template>
      <template #footer="{ close }">
        <v-spacer /> <v-btn variant="text" @click="close">닫기</v-btn>
      </template>
    </Dialog>
  </v-card>
</template>
