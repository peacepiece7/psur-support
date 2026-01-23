<script setup lang="ts">
  const currentYear = new Date().getFullYear()
  const selectedSite = ref('')

  const sisterSites = [
    { title: '스포츠클럽협회', value: '스포츠클럽협회' },
    { title: '스포츠클럽연맹', value: '스포츠클럽연맹' },
  ]
</script>

<template>
  <v-footer class="border-grey-300 bg-grey-100 w-full border-t px-4 py-6">
    <div class="mx-auto w-full max-w-[1400px]">
      <!-- 상단 영역 -->
      <div
        class="border-grey-300 mb-4 flex flex-wrap items-center justify-between gap-4 border-b pb-4"
      >
        <div class="flex flex-wrap items-center gap-2">
          <span
            class="text-grey-900 cursor-pointer text-sm transition-colors duration-200 ease-in-out"
            >스포츠행정지원</span
          >
          <span class="text-grey-300 text-sm">|</span>
          <span
            class="text-grey-900 cursor-pointer text-sm transition-colors duration-200 ease-in-out"
            >스포츠클럽관리</span
          >
          <span class="text-grey-300 text-sm">|</span>
          <span
            class="text-grey-900 cursor-pointer text-sm transition-colors duration-200 ease-in-out"
            >스포츠클럽대회관리</span
          >
          <span class="text-grey-300 text-sm">|</span>
          <span
            class="text-grey-900 cursor-pointer text-sm transition-colors duration-200 ease-in-out"
            >정보처리방침</span
          >
          <span class="text-grey-300 text-sm">|</span>
          <span
            class="text-grey-900 cursor-pointer text-sm transition-colors duration-200 ease-in-out"
            >이용약관</span
          >
        </div>
        <div class="flex items-center">
          <Select
            id="footer-sister-site-select"
            v-model="selectedSite"
            select-class="footer__dropdown"
            label="자매사이트 선택"
            aria-label="자매사이트 선택"
            placeholder="자매사이트 선택"
            :options="sisterSites"
          />
        </div>
      </div>

      <!-- 중간 영역: 전화번호 -->
      <div class="border-grey-300 mb-4 border-b pb-4">
        <div class="flex flex-wrap items-center gap-2">
          <span class="text-grey-900 text-base font-bold">체육회</span>
          <span class="text-grey-600 text-base">123-1234-1234</span>
          <span class="text-sm" style="color: var(--color-grey-300)">/</span>
          <span class="text-grey-600 text-base">123-2345-2345</span>
        </div>
      </div>

      <!-- 하단 영역: Copyright & SNS -->
      <div class="flex flex-wrap items-start justify-between gap-4">
        <div class="min-w-0 flex-1">
          <p class="text-grey-600 m-0 mb-1 text-sm leading-[1.6]">
            Copyright © {{ currentYear }} 스포츠클럽포털. All rights reserved.
          </p>
          <p class="text-grey-600 m-0 mb-1 text-sm leading-[1.6]">
            서울특별시 송파구 올림픽로 424 (방이동)
          </p>
        </div>
        <div class="flex items-center">
          <div class="flex items-center gap-2">
            <Button
              size="icon"
              shape="circle"
              variant="outlined"
              color="surface"
              class="border-grey-300 text-grey-900 hover:bg-grey-50 hover:border-grey-400 bg-white transition-colors duration-200 ease-in-out"
              aria-label="유튜브"
            >
              <v-icon>mdi-youtube</v-icon>
            </Button>
            <Button
              size="icon"
              shape="circle"
              variant="outlined"
              color="surface"
              class="border-grey-300 text-grey-900 hover:bg-grey-50 hover:border-grey-400 bg-white transition-colors duration-200 ease-in-out"
              aria-label="X (트위터)"
            >
              <v-icon>mdi-twitter</v-icon>
            </Button>
            <Button
              size="icon"
              shape="circle"
              variant="outlined"
              color="surface"
              class="border-grey-300 text-grey-900 hover:bg-grey-50 hover:border-grey-400 bg-white transition-colors duration-200 ease-in-out"
              aria-label="인스타그램"
            >
              <v-icon>mdi-instagram</v-icon>
            </Button>
          </div>
        </div>
      </div>
    </div>
  </v-footer>
</template>
