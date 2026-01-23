<script setup lang="ts">
  // Breadcrumb 예제용 데이터
  const breadcrumbPath1 = ref('1Depth')
  const breadcrumbPath2 = ref('1Depth > 2Depth')
  const breadcrumbPath3 = ref('1Depth > 2Depth > 3Depth')
  const breadcrumbPath4 = ref('1Depth > 2Depth > 3Depth > 4Depth')
  const breadcrumbPath5 = ref('1Depth > 2Depth > 3Depth > 4Depth > 5Depth')
  const breadcrumbPath6 = ref(
    '1Depth > 2Depth > 3Depth > 4Depth > 5Depth > 6Depth',
  )
  const breadcrumbEmpty = ref('')
</script>

<template>
  <div class="bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-[1400px]">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">Breadcrumb 예제</h1>

      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">Breadcrumb</h2>

        <!-- 유의사항 -->
        <div
          class="mb-6 rounded-lg border border-orange-300 bg-orange-50 p-4 text-sm"
        >
          <p class="mb-1 font-semibold text-orange-800">⚠️ 유의사항</p>
          <p class="text-orange-700">
            현재 Breadcrumb은 클릭 불가능한 텍스트로 표시됩니다. 향후 각 경로에
            링크 기능이 추가될 예정입니다.
          </p>
        </div>

        <div class="grid gap-6">
          <!-- 빈 경로 -->
          <div>
            <h3 class="text-grey-900 mb-3 text-lg font-semibold">빈 경로</h3>
            <Breadcrumb :menu-nm-path="breadcrumbEmpty" />
          </div>

          <!-- 1개 경로 -->
          <div>
            <h3 class="text-grey-900 mb-3 text-lg font-semibold">1개 경로</h3>
            <Breadcrumb :menu-nm-path="breadcrumbPath1" />
          </div>

          <!-- 2개 경로 -->
          <div>
            <h3 class="text-grey-900 mb-3 text-lg font-semibold">2개 경로</h3>
            <Breadcrumb :menu-nm-path="breadcrumbPath2" />
          </div>

          <!-- 3개 경로 -->
          <div>
            <h3 class="text-grey-900 mb-3 text-lg font-semibold">3개 경로</h3>
            <Breadcrumb :menu-nm-path="breadcrumbPath3" />
          </div>

          <!-- 4개 경로 (Truncate) -->
          <div>
            <h3 class="text-grey-900 mb-3 text-lg font-semibold">
              4개 경로 (Truncate)
            </h3>
            <Breadcrumb :menu-nm-path="breadcrumbPath4" />
            <p class="text-grey-600 mt-2 text-sm">
              home > 1Depth > ... > 4Depth 형태로 표시됩니다
            </p>
          </div>

          <!-- 5개 경로 (Truncate) -->
          <div>
            <h3 class="text-grey-900 mb-3 text-lg font-semibold">
              5개 경로 (Truncate)
            </h3>
            <Breadcrumb :menu-nm-path="breadcrumbPath5" />
            <p class="text-grey-600 mt-2 text-sm">
              home > 1Depth > ... > 5Depth 형태로 표시됩니다
            </p>
          </div>

          <!-- 6개 경로 (Truncate) -->
          <div>
            <h3 class="text-grey-900 mb-3 text-lg font-semibold">
              6개 경로 (Truncate)
            </h3>
            <Breadcrumb :menu-nm-path="breadcrumbPath6" />
            <p class="text-grey-600 mt-2 text-sm">
              home > 1Depth > ... > 6Depth 형태로 표시됩니다
            </p>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped lang="scss"></style>
