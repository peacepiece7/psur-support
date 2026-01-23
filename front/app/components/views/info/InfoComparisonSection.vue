<script setup lang="ts">
  const comparisonRef = ref<HTMLElement | null>(null)
  const isComparisonVisible = ref(false)

  // 비교 테이블 섹션 관찰
  useIntersectionObserver(comparisonRef, (entry) => {
    if (entry.isIntersecting) isComparisonVisible.value = true
  })
</script>

<template>
  <section
    ref="comparisonRef"
    class="relative bg-white px-4 py-12 transition-all duration-[800ms] ease-out"
    :class="{
      'translate-y-[50px] opacity-0': !isComparisonVisible,
      'translate-y-0 opacity-100': isComparisonVisible,
    }"
  >
    <div class="mx-auto max-w-[1400px]">
      <h2 class="mb-6 text-center text-2xl font-bold text-grey-900">
        등록스포츠클럽과 지정스포츠클럽 비교
      </h2>
      <div
        class="mb-6 overflow-x-auto rounded-2xl shadow-[0_2px_8px_rgba(0,0,0,0.1)]"
      >
        <table class="w-full border-collapse overflow-hidden bg-white">
          <thead>
            <tr class="bg-new-blue-700">
              <th
                class="px-6 py-4 text-left text-base font-bold text-white first:rounded-tl-2xl last:rounded-tr-2xl"
              >
                구분
              </th>
              <th
                class="border-l border-white/20 px-6 py-4 text-left text-base font-bold text-white"
              >
                등록스포츠클럽
              </th>
              <th
                class="border-l border-white/20 px-6 py-4 text-left text-base font-bold text-white last:rounded-tr-2xl"
              >
                지정스포츠클럽
              </th>
            </tr>
          </thead>
          <tbody>
            <tr
              class="border-b border-grey-200 transition-colors duration-150 hover:bg-grey-50"
            >
              <td
                class="bg-grey-50 px-6 py-4 text-base font-semibold text-grey-900"
              >
                대상
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                체육활동 법인 또는 단체
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                등록스포츠클럽 중 지정
              </td>
            </tr>
            <tr
              class="border-b border-grey-200 transition-colors duration-150 hover:bg-grey-50"
            >
              <td
                class="bg-grey-50 px-6 py-4 text-base font-semibold text-grey-900"
              >
                최종권한
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                지방자치단체장(특별자치시·도지사, 시·군·구청장)
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                문화체육관광부 장관
              </td>
            </tr>
            <tr
              class="border-b border-grey-200 transition-colors duration-150 hover:bg-grey-50"
            >
              <td
                class="bg-grey-50 px-6 py-4 text-base font-semibold text-grey-900"
              >
                권한 위임·위탁
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                지방체육회(시·군·구체육회)
              </td>
              <td class="px-6 py-4 text-base text-grey-700">대한체육회</td>
            </tr>
            <tr
              class="border-b border-grey-200 transition-colors duration-150 hover:bg-grey-50"
            >
              <td
                class="bg-grey-50 px-6 py-4 text-base font-semibold text-grey-900"
              >
                관련조항
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                스포츠클럽법 제6조
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                스포츠클럽법 제9조
              </td>
            </tr>
            <tr
              class="border-b border-grey-200 transition-colors duration-150 hover:bg-grey-50"
            >
              <td
                class="bg-grey-50 px-6 py-4 text-base font-semibold text-grey-900"
              >
                신청주기
              </td>
              <td class="px-6 py-4 text-base text-grey-700">
                매월 1~15일(15일간)
              </td>
              <td class="px-6 py-4 text-base text-grey-700">연 1회</td>
            </tr>
            <tr class="transition-colors duration-150 hover:bg-grey-50">
              <td
                class="rounded-bl-2xl bg-grey-50 px-6 py-4 text-base font-semibold text-grey-900"
              >
                유효기간
              </td>
              <td class="px-6 py-4 text-base text-grey-700">없음</td>
              <td class="rounded-br-2xl px-6 py-4 text-base text-grey-700">
                3년
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="flex flex-wrap justify-center gap-4">
        <v-btn
          color="primary"
          variant="outlined"
          size="large"
          class="min-w-[200px]"
        >
          지정스포츠클럽 바로가기
        </v-btn>
        <v-btn
          color="primary"
          variant="outlined"
          size="large"
          class="min-w-[200px]"
        >
          등록스포츠클럽 바로가기
        </v-btn>
      </div>
    </div>
  </section>
</template>
