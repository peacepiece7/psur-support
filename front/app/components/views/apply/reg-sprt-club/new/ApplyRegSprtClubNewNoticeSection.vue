<script setup lang="ts">
  type Props = {
    items?: string[]
  }

  const props = defineProps<Props>()

  const defaultItems = [
    '지정스포츠클럽 평가는 평가기간 내 각 스포츠클럽이 작성하여 제출한 수행평가지 및 증빙자료를 기준으로 진행됩니다.',
    '평가 자료 제출 기간 이후에는 제출된 자료에 대한 수정 등이 불가하니 이점 참고 바랍니다.',
    '각 클럽이 작성한 수행평가지 및 증빙자료를 통해 평가 내용을 확인할 수 없는 경우 평가 점수에 반영되지 않으며, 이에 대한 책임은 수행평가지를 작성 및 증빙자료를 제출한 클럽에 있습니다.',
    '평가자료 제출은 1차(재정 제외)와 2차(재정 관련 자료)로 구분되며, 1차 제출 기간에는 2차 재정 관련 자료 제출이 불가합니다.',
    '평가 자료는 “수행평가지”와 수행평가지 內 기입 내용을 증빙하기 위한 “증빙자료”로 구분되어 있습니다.',
    '“수행평가지”는 평가 각 영역 별 양식을 다운로드 받은 뒤 필수로 작성 후 제출해야 최종 제출이 진행됩니다.',
    '“증빙자료”는 영역 별 필수 제출자료와 해당 시 제출자료로 구분되어 있으니, 각 영역 별 안내를 참고하여 제출해 주세요.',
  ]

  const noticeItems = computed(() =>
    props.items?.length ? props.items : defaultItems,
  )
</script>

<template>
  <aside
    class="rounded-3xl p-8 max-md:p-6"
    style="
      background: color-mix(in srgb, var(--color-primary-300) 12%, transparent);
      border: 1px solid
        color-mix(in srgb, var(--color-primary-300) 25%, transparent);
    "
  >
    <div class="notice__header">
      <div
        class="font-extrabold"
        style="
          width: 28px;
          height: 28px;
          border-radius: 999px;
          display: grid;
          place-items: center;
          background: var(--color-primary-700);
          color: #ffffff;
        "
        aria-hidden="true"
      >
        !
      </div>
      <h4 class="notice__title">유의사항</h4>
    </div>

    <ul class="notice__list">
      <li v-for="(t, idx) in noticeItems" :key="idx" class="notice__item">
        {{ t }}
      </li>
    </ul>
  </aside>
</template>

<style scoped>
  .notice__header {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    margin-bottom: 1.5rem;
  }

  .notice__title {
    margin: 0;
    font-size: 1.25rem;
    font-weight: 700;
    color: var(--color-grey-900);
  }

  .notice__list {
    margin: 0;
    padding-left: 1.1rem;
    display: grid;
    gap: 0.75rem;
  }

  .notice__item {
    color: var(--color-grey-900);
    line-height: 1.5;
  }
</style>
