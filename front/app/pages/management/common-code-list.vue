<script setup lang="ts">
  import { useCommonCodeStore } from '~/stores/useCommonCodeStore'

  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)
  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  const commonCodeStore = useCommonCodeStore()

  watch(
    () => commonCodeStore.selectedGroupCode,
    (newGroup) => {
      commonCodeStore.actions.onSelectedGroupCodeChange(newGroup ?? null)
    },
    { immediate: true },
  )
  watch(
    () => commonCodeStore.selectedCode,
    (newCode) => {
      commonCodeStore.actions.onSelectedCodeChange(newCode ?? null)
    },
    { immediate: true },
  )

  onMounted(() => {
    commonCodeStore.actions.fetchRootGroups()
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
        <!-- 최상위 그룹 목록: 리스트 | 에디터 -->
        <div class="mb-6">
          <h2 class="text-neutral-text-title mb-4 text-xl font-bold">
            최상위 그룹 목록
          </h2>
          <div class="grid gap-6 lg:grid-cols-3">
            <CommonCodeTopList />
            <CommonCodeTopEditor />
          </div>
        </div>

        <!-- 그룹별 코드 관리: 그룹코드 리스트 | 그룹코드 에디터 -->
        <div class="mb-6">
          <h2 class="text-neutral-text-title mb-4 text-xl font-bold">
            그룹별 코드 관리
          </h2>
          <div class="grid gap-6 lg:grid-cols-3">
            <CommonGroupCodeList />
            <CommonGroupCodeEditor />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
