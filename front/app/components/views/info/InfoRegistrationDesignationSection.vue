<script setup lang="ts">
  const { isMobile } = useDevice()

  const regAreRef = ref<HTMLElement | null>(null)
  const isRegAreaVisible = ref(false)

  // 등록제/지정제 가이드 div 관찰
  useIntersectionObserver(regAreRef, (entry) => {
    console.log(entry)
    if (entry.isIntersecting) isRegAreaVisible.value = true
  })
</script>

<template>
  <section
    ref="regAreRef"
    class="relative bg-white px-4 py-12 transition-all duration-700 ease-out"
    :class="{
      'translate-y-[50px] opacity-0': !isRegAreaVisible,
      'translate-y-0 opacity-100': isRegAreaVisible,
    }"
  >
    <div class="mx-auto max-w-[1400px]">
      <!-- 가이드 div (IntersectionObserver 트리거용) -->
      <div ref="registrationDesignationGuideRef" class="mb-8 h-2.5 w-full" />

      <!-- 등록제/지정제 카드 그룹 -->
      <div class="flex flex-col gap-6">
        <!-- 등록제 -->
        <div
          class="info-reg-area__card--registration mb-0 flex flex-col rounded-2xl bg-white p-6 shadow-[0_2px_8px_rgba(0,0,0,0.1)] transition-all duration-700 ease-out"
          :class="{
            'translate-y-[50px] opacity-0': !isRegAreaVisible,
            'translate-y-0 opacity-100': isRegAreaVisible,
          }"
        >
          <!-- 1행: 이미지와 텍스트 (2단) -->
          <div class="info-reg-area__card-row--top flex w-full gap-6">
            <div
              class="h-[175px] max-h-[175px] w-[240px] shrink-0 overflow-hidden rounded-2xl transition-all delay-300 duration-700 ease-out"
              :class="{
                '-translate-x-[50px] opacity-0': !isRegAreaVisible,
                'translate-x-0 opacity-100': isRegAreaVisible,
              }"
            >
              <img
                src="/skydiving-bg.png"
                alt="등록제 이미지"
                class="h-full w-full object-cover"
              />
            </div>
            <div class="flex max-h-[300px] flex-1 flex-col overflow-y-auto">
              <h3 class="mb-4 text-2xl font-bold text-grey-900">등록제</h3>
              <p
                class="flex-1 overflow-y-auto text-base leading-[1.8] text-grey-600"
              >
                지역사회 내에서 정기적인 체육활동을 하는 단체가 효율적인
                체육활동을 영위하기 위해
                <br v-if="!isMobile" />
                스포츠클럽법 조항에 근거한 설립요건을 갖추어 해당 지자체에
                등록하는 제도
              </p>
            </div>
          </div>

          <!-- 구분선 -->
          <div class="mx-0 my-4 h-px w-full bg-grey-200" />

          <!-- 2행: 등록요건 (1단) -->
          <div class="info-reg-area__card-row--bottom flex w-full">
            <div class="w-full">
              <h4 class="mb-3 text-xl font-semibold text-grey-900">
                등록 요건
              </h4>
              <ul
                class="grid list-disc grid-cols-2 gap-x-4 gap-y-2 pl-6 leading-[1.3] text-grey-600"
              >
                <li>정관 보유</li>
                <li>연간운영계획서 보유</li>
                <li>대표자 및 스포츠클럽회원의 대의기구 보유</li>
                <li>회비 납부‧활동하는 회원 수 종목별 10명 이상 보유</li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 지정제 -->
        <div
          class="info-reg-area__card--designation mb-0 flex flex-col rounded-2xl bg-white p-6 shadow-[0_2px_8px_rgba(0,0,0,0.1)] transition-all duration-700 ease-out"
          :class="{
            'translate-y-[50px] opacity-0': !isRegAreaVisible,
            'translate-y-0 opacity-100': isRegAreaVisible,
          }"
        >
          <!-- 1행: 이미지와 텍스트 (2단) -->
          <div class="info-reg-area__card-row--top flex w-full gap-6">
            <div
              class="h-[175px] max-h-[175px] w-[240px] shrink-0 overflow-hidden rounded-2xl transition-all delay-300 duration-700 ease-out"
              :class="{
                '-translate-x-[50px] opacity-0': !isRegAreaVisible,
                'translate-x-0 opacity-100': isRegAreaVisible,
              }"
            >
              <img
                src="/swimming-bg.png"
                alt="지정제 이미지"
                class="h-full w-full object-cover"
              />
            </div>
            <div class="flex max-h-[300px] flex-1 flex-col overflow-y-auto">
              <h3 class="mb-4 text-2xl font-bold text-grey-900">지정제</h3>
              <p
                class="flex-1 overflow-y-auto text-base leading-[1.8] text-grey-600"
              >
                학교스포츠클럽 및 학교운동부와 연계, <br v-if="!isMobile" />
                종목별 전문선수 육성/연령/지역/성별 스포츠 프로그램의 운영,
                <br v-if="!isMobile" />
                기초 종목 및 비인기 종목 육성 등을 위해
                <br v-if="!isMobile" />
                문화체육관광부가 등록스포츠클럽 중 역량을 갖춘 스포츠클럽을
                지정하는 제도
              </p>
            </div>
          </div>

          <!-- 구분선 -->
          <div class="mx-0 my-4 h-px w-full bg-grey-200" />

          <!-- 2행: 지정요건 (1단) -->
          <div class="info-reg-area__card-row--bottom flex w-full">
            <div class="w-full">
              <h4 class="mb-3 text-xl font-semibold text-grey-900">
                지정 요건
              </h4>
              <ul
                class="grid list-disc grid-cols-2 gap-x-4 gap-y-2 pl-6 leading-[1.3] text-grey-600"
              >
                <li>비영리 법인/단체</li>
                <li>회원이 의결권 행사하는 구조 보유</li>
                <li>설립일로부터 3년 이상 활동실적 보유</li>
                <li>1년 이상 사용 가능한 체육시설 보유</li>
                <li>국가체육지도자 1명 이상 고용</li>
                <li>최근 1년 자체/지방비 수입이 총수입 100분의 50 이상</li>
                <li>종목별 50명 이상 또는 총 100명 이상 회원 보유</li>
                <li>회원 가입 제약이 없을 것</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
