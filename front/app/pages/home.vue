<script setup lang="ts">
  import { HOME_META, HOME_META_HEADER, HOME_SEO_META } from '~/constants/meta'

  // 디바이스 타입 감지
  const { isDesktop } = useDevice()

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  // 👉 Nuxt 라우터가 사용하는 페이지 메타 정보.
  definePageMeta({
    title: HOME_META.TITLE,
    description: HOME_META.DESCRIPTION,
  })

  // 이거는 head 태그에 들어가는 모든 요소 직접 작성하는 함수.
  useHead(HOME_META_HEADER)

  // SEO 최적화 meta 자동 생성기
  // SEO 규약에 맞게 필요한 메타태그를 생성
  useSeoMeta(HOME_SEO_META)
</script>

<template>
  <div>
    <div class="relative w-full">
      <!-- 배너 섹션 (viewport 100% width, height) -->
      <HomeBannerSection />

      <!-- <HomePictogramBackground /> -->

      <!-- 스포츠클럽 한눈에보기 및 기타 섹션 -->
      <div class="relative z-1 mx-auto w-full px-4 py-20">
        <!-- <HomeGlanceSection v-if="!isLoggedIn" /> -->
        <!-- <HomeMainServiceGoTo v-else /> -->
      </div>

      <!-- 스포츠클럽 탐색 -->
      <div
        class="relative w-full rounded-br-2xl bg-blue-100/30 px-4 py-20"
        style="margin-top: 4rem"
      >
        <!-- <HomeExplorSprtClub v-if="!isLoggedIn" /> -->
        <!-- <template v-else> -->
        <!-- <HomeClubRegisteredOverview /> -->
        <!-- <div class="bg-grey-100 h-px w-full" style="margin: 2.5rem 0" /> -->
        <!-- <HomeNoticeAndScheduler /> -->
        <!-- </template> -->
      </div>

      <!-- 스포츠클럽 비디오 슬라이더 -->
      <div class="sports-club-section">
        <!-- <HomeVideoSliderVer3 /> -->
      </div>
    </div>

    <!-- <HomePopupDialog /> -->
  </div>
</template>
