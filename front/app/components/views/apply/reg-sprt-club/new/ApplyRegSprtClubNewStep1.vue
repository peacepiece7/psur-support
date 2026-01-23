<script setup lang="ts">
  import ApplyRegSprtClubNewContestContent from './ApplyRegSprtClubNewContestContent.vue'
  import ApplyRegSprtClubNewNoticeSection from './ApplyRegSprtClubNewNoticeSection.vue'
  import type { ApplyRegSprtClubNewContestValues } from './ApplyRegSprtClubNewContestContent.vue'

  const step1Ref = ref<HTMLElement | null>(null)
  const isStep1Visible = ref(false)

  // Step1 컨텐츠 영역 관찰 (InfoRegistrationDesignationSection 패턴 참고)
  useIntersectionObserver(step1Ref, (entry) => {
    if (entry.isIntersecting) isStep1Visible.value = true
  })

  // TODO: 실제 API 연동 시 페이지/스토어에서 주입
  const demoValues: ApplyRegSprtClubNewContestValues = {
    pbancTtl: '등록 스포츠클럽 신청 안내',
    hostOgzNm: '대한체육회',
    spvsOgzNm: '시·도 체육회',
    potdBegnDttm: '2025-01-01',
    potdEndDttm: '2025-12-31',
    sbmsnBegnDttm: '2025-01-01 10:00',
    sbmsnEndDttm: '2025-12-31 23:59',
    pbctOutlCtet:
      '<p>본 단계에서는 공모(신청) 내용과 유의사항을 확인합니다.</p><p>하단 “다음으로” 버튼을 누르기 전에 내용을 충분히 확인해 주세요.</p>',
    chpsGdCtet: '등록/지정 관련 문의: 담당 부서로 연락',
    emailAddrGdCtet: 'support@example.com',
    telnoGdCtet: '02-0000-0000',
  }
</script>

<template>
  <section
    ref="step1Ref"
    class="gap-6 grid"
    :class="{ 'step1--visible': isStep1Visible }"
  >
    <div class="step1__body">
      <ApplyRegSprtClubNewContestContent :values="demoValues" />
      <ApplyRegSprtClubNewNoticeSection />
    </div>
  </section>
</template>

<style scoped>
  .step1__body {
    display: grid;
    gap: 1.5rem;
  }

  /* IntersectionObserver 트리거 전: 살짝 아래에서 올라오며 등장 */
  .step1__body > :deep(*) {
    opacity: 0;
    transform: translateY(14px);
    transition:
      opacity 420ms ease-in-out,
      transform 420ms ease-in-out;
    will-change: opacity, transform;
  }

  .step1--visible .step1__body > :deep(*) {
    opacity: 1;
    transform: translateY(0);
  }

  /* 두 번째 카드(유의사항) 살짝 딜레이 */
  .step1--visible .step1__body > :deep(*:nth-child(2)) {
    transition-delay: 80ms;
  }
</style>
