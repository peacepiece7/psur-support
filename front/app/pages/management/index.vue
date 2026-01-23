<script setup lang="ts">
  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  // 로그인하지 않은 경우 홈으로 리다이렉트
  if (!isLoggedIn.value) {
    await navigateTo('/')
  }
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="관리"
      image-src="/main_banner.png"
      image-alt="관리 페이지 헤더 이미지"
      type="infoPage"
    />

    <div class="bg-neutral-fill-50 min-h-screen p-8">
      <div class="mx-auto max-w-[1400px]">
        <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          <NuxtLink
            to="/management/common-code"
            class="border-neutral-stroke-300 hover:border-new-blue-500 hover:bg-new-blue-50 group block rounded-lg border bg-white p-6 transition-colors"
          >
            <h2
              class="text-neutral-text-title group-hover:text-new-blue-700 mb-2 text-xl font-bold"
            >
              공통코드 관리
            </h2>
            <p class="text-neutral-text-caption text-sm">
              공통코드 그룹 목록을 조회하고 관리합니다.
            </p>
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
