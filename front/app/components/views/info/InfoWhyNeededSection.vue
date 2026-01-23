<script setup lang="ts">
  const whyNeededRef = ref<HTMLElement | null>(null)
  const isWhyNeededVisible = ref(false)

  // 카드별 가시성
  const cardVisibilities = ref<boolean[]>(new Array(6).fill(false))

  // 왜 필요한가요? 섹션 관찰
  useIntersectionObserver(whyNeededRef, (entry) => {
    if (entry.isIntersecting) {
      isWhyNeededVisible.value = true
      cardVisibilities.value.forEach((_, index) => {
        setTimeout(() => (cardVisibilities.value[index] = true), index * 100)
      })
    }
  })

  const whyNeededCards = [
    {
      icon: 'mdi-heart-pulse',
      title: '지역 주민 건강 증진',
      description:
        '규칙적인 운동 기회 제공\n\n스포츠클럽은 지역 주민들에게 꾸준한 운동 기회를 제공하여 주민들의 신체적, 정신적 건강 향상에 기여합니다. 특히, 근로자, 노인, 여성 등 운동 기회가 부족한 계층의 건강 증진에 효과적입니다.',
    },
    {
      icon: 'mdi-account-group',
      title: '사회적 통합 및 소통 강화',
      description:
        '다양성 존중 및 소통 기회 확대\n\n다양한 연령대, 배경을 가진 사람들이 모이는 스포츠클럽은 사회적 연결망 확대, 서로 다른 문화와 경험 공유의 풍부한 기회를 제공합니다. 이는 사회 구성원 간 소통과 이해를 증진하고, 사회적 통합을 강화하는 데 기여합니다.',
    },
    {
      icon: 'mdi-chart-line',
      title: '지역 경제 활성화',
      description:
        '지역 경제 활성화\n\n스포츠클럽 운영과 관련된 다양한 상품 및 서비스는 지역 경제 활성화에 크게 기여합니다. 스포츠클럽에서 주최하는 행사나 경기는 지역 관광활성화를 통해 추가적인 경제적 이익을 가져올 수 있습니다.',
    },
    {
      icon: 'mdi-school',
      title: '청소년의 건강한 성장 촉진',
      description:
        '청소년의 건강한 성장 촉진\n\n스포츠클럽은 청소년들에게 규칙 준수, 팀워크, 리더십, 의사소통, 목표 설정 등 핵심 생활 기술을 함양할 수 있는 중요한 장을 제공합니다. 이는 청소년들의 건강한 성장과 사회 적응 능력 향상에 기여합니다.',
    },
    {
      icon: 'mdi-stadium',
      title: '지역 스포츠 문화 발전 및 참여 확대',
      description:
        '스포츠 인프라 구축\n\n적절한 정책 지원은 스포츠클럽의 장기적인 생존 및 발전을 보장합니다. 이는 지역 스포츠 문화의 지속성을 확보하고, 지역 주민들의 스포츠 참여를 지속적으로 증진시키는 데 기여합니다.',
    },
    {
      icon: 'mdi-trophy',
      title: '지역 스포츠 문화 발전',
      description:
        '지역 스포츠 문화 발전\n\n스포츠클럽은 지역의 스포츠 문화를 발전시키고, 주민들의 스포츠 참여를 확대하는 중요한 역할을 합니다.',
    },
  ]
</script>

<template>
  <section
    ref="whyNeededRef"
    class="relative bg-grey-50 px-4 py-12 transition-all duration-800 ease-out"
    :class="{
      'translate-y-[50px] opacity-0': !isWhyNeededVisible,
      'translate-y-0 opacity-100': isWhyNeededVisible,
    }"
  >
    <div class="mx-auto max-w-[1400px]">
      <h2 class="mb-6 text-center text-2xl font-bold text-grey-900">
        스포츠클럽은 왜 필요한가요?
      </h2>
      <p
        class="mx-auto mb-8 max-w-[900px] text-center text-base leading-[1.8] text-grey-600"
      >
        정부는 생활체육을 기반으로 우수 선수 육성과 은퇴 선수들의 지도자 활용을
        통한 학교체육 및 생활체육 발전 선순환 구조를 구축하기 위해 스포츠클럽
        정책을 마련했습니다. 이 정책은 단순히 신체활동 촉진을 넘어 지역 사회에
        다양한 긍정적 영향을 미칩니다.
      </p>
      <div class="mb-8 grid grid-cols-2 gap-6">
        <div
          v-for="(card, index) in whyNeededCards"
          :key="index"
          class="rounded-2xl bg-white p-6 shadow-[0_2px_8px_rgba(0,0,0,0.1)] transition-all duration-500 ease-out"
          :class="{
            'translate-y-[30px] opacity-0': !cardVisibilities[index],
            'translate-y-0 opacity-100': cardVisibilities[index],
          }"
        >
          <div
            class="mb-4 flex h-20 w-20 items-center justify-center rounded-full bg-new-blue-700 text-white"
          >
            <v-icon
              :icon="card.icon"
              size="48"
              class="text-white"
              style="animation: shake 0.5s ease-in-out 0.5s"
            />
          </div>
          <h3 class="mb-3 text-xl font-bold text-grey-900">
            {{ card.title }}
          </h3>
          <p class="text-sm leading-[1.8] whitespace-pre-line text-grey-600">
            {{ card.description }}
          </p>
        </div>
      </div>
      <p
        class="mx-auto mt-8 max-w-[900px] border-t border-grey-200 pt-6 text-center text-base leading-[1.8] text-grey-600"
      >
        스포츠클럽 정책은 지역 주민들의 건강 증진, 사회적 통합 강화, 지역 경제
        활성화, 청소년 건강한 성장 촉진, 지역 스포츠 문화 발전 및 참여 확대 등
        다양한 긍정적 효과를 가져옵니다. 지속적인 정책 지원과 적극적인 운영을
        통해 공공스포츠클럽 정책은 지역 사회 발전에 기여하는 중요한 역할을 할 수
        있을 것입니다.
      </p>
    </div>
  </section>
</template>
