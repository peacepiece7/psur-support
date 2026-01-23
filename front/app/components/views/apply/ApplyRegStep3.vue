<script setup lang="ts"></script>

<template>
  <section class="apply-reg-step">
    <h3 class="apply-reg-step__title">3단계. 개인 정보 입력</h3>
    <p class="apply-reg-step__desc">(컨텐츠는 추후 단계별로 채웁니다)</p>
  </section>
</template>

<style scoped>
  .apply-reg-step {
    background: var(--color-grey-50);
    border: 1px solid var(--color-grey-200);
    border-radius: 1.5rem;
    padding: 2.5rem;
    min-height: 420px;
  }

  .apply-reg-step__title {
    margin: 0 0 1rem 0;
    font-size: 1.5rem;
    font-weight: 700;
  }

  .apply-reg-step__desc {
    margin: 0;
    color: var(--color-grey-600);
  }
</style>
